package test.drone.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import test.drone.entity.event.Event;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka producer config for different events
 * @param <E> Event
 */
@Configuration
@ConditionalOnProperty(prefix = "kafka", name = "enabled", havingValue = "true")
public class KafkaProducerConfig<E extends Event> {
    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public ProducerFactory<String, E> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, E> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}