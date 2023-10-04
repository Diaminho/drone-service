package test.drone.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import test.drone.configuration.KafkaProducerConfig;
import test.drone.entity.Drone;
import test.drone.entity.Model;
import test.drone.entity.State;
import test.drone.entity.event.BatteryCheckLevelEvent;
import test.drone.repository.DroneRepository;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@Import({CheckBatteryTaskIT.KafkaTestContainersConfiguration.class,
        CheckBatteryTaskIT.KafkaConsumer.class,
        KafkaProducerConfig.class, CheckBatteryTask.class})
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@ExtendWith({SpringExtension.class, OutputCaptureExtension.class})
@DirtiesContext
class CheckBatteryTaskIT {
    @Container
    private static final PostgreSQLContainer POSTGRESQL_CONTAINER = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("test")
            .withUsername("sa")
            .withPassword("sa");

    @Container
    private static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    public KafkaTemplate<String, BatteryCheckLevelEvent> kafkaTemplate;

    @Autowired
    private KafkaConsumer consumer;

    @Autowired
    private CheckBatteryTask checkBatteryTask;

    @Test
    void sendEventTest(CapturedOutput output) {
        var drone = new Drone();
        short battery = 55;
        drone.setBatteryCapacity(battery);
        drone.setSerialNumber("1A");
        drone.setState(State.IDLE);
        drone.setWeightLimit(123d);
        drone.setModel(Model.CRUISERWEIGHT);

        droneRepository.save(drone);

        assertDoesNotThrow(() -> checkBatteryTask.checkBatteryStatusForDrone());

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(5, SECONDS)
                .untilAsserted(() -> assertTrue(output.getAll().contains("Received payload:")));
    }

    @DynamicPropertySource
    private static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);

        registry.add("spring.kafka.producer.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
        registry.add("spring.kafka.consumer.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
        registry.add("kafka.topic.name", () -> "test");
    }

    @TestConfiguration
    @EnableKafka
    static class KafkaTestContainersConfiguration {
        @Bean
        public ConsumerFactory<String, BatteryCheckLevelEvent> consumerFactory() {
            return new DefaultKafkaConsumerFactory<>(
                    consumerConfigs(),
                    new StringDeserializer(),
                    new JsonDeserializer<>(BatteryCheckLevelEvent.class));
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, BatteryCheckLevelEvent> kafkaListenerContainerFactory() {

            ConcurrentKafkaListenerContainerFactory<String, BatteryCheckLevelEvent> factory =
                    new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory());
            return factory;
        }

        @Bean
        public Map<String, Object> consumerConfigs() {
            Map<String, Object> props = new HashMap<>();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CONTAINER.getBootstrapServers());
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            props.put(ConsumerConfig.GROUP_ID_CONFIG, "1");
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
            return props;
        }
    }

    @TestComponent
    @EnableKafka
    static class KafkaConsumer {
        private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

        @KafkaListener(topics = "${kafka.topic.name}")
        public void receive(BatteryCheckLevelEvent payload) {
            LOGGER.info("Received payload: '{}'", payload);
        }
    }
}
