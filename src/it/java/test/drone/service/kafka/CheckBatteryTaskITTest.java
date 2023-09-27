package test.drone.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
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

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@Import({CheckBatteryTaskITTest.KafkaTestContainersConfiguration.class, KafkaProducerConfig.class})
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@ExtendWith(SpringExtension.class)
@DirtiesContext
class CheckBatteryTaskITTest {
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
    private KafkaConsumer<String, BatteryCheckLevelEvent> consumer;

    @Autowired
    private CheckBatteryTask checkBatteryTask;

    @Test
    void sendEventTest() {
        var drone = new Drone();
        short battery = 55;
        drone.setBatteryCapacity(battery);
        drone.setSerialNumber("1A");
        drone.setState(State.IDLE);
        drone.setWeightLimit(123d);
        drone.setModel(Model.CRUISERWEIGHT);

        droneRepository.save(drone);

        assertDoesNotThrow(() -> checkBatteryTask.checkBatteryStatusForDrone());
        verify(kafkaTemplate, times(1)).send(eq("ww"), any(BatteryCheckLevelEvent.class));
    }

    @DynamicPropertySource
    private static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);

        registry.add("spring.kafka.producer.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
    }

    @TestConfiguration
    static class KafkaTestContainersConfiguration {
        @Bean
        ConcurrentKafkaListenerContainerFactory<String, BatteryCheckLevelEvent> kafkaListenerContainerFactory() {
            ConcurrentKafkaListenerContainerFactory<String, BatteryCheckLevelEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory());
            return factory;
        }
        @Bean
        public ConsumerFactory<String, BatteryCheckLevelEvent> consumerFactory() {
            return new DefaultKafkaConsumerFactory<>(consumerConfigs());
        }

        @Bean
        public Map<String, Object> consumerConfigs() {
            Map<String, Object> props = new HashMap<>();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CONTAINER.getBootstrapServers());
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            props.put(ConsumerConfig.GROUP_ID_CONFIG, "1");
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            return props;
        }
    }
}
