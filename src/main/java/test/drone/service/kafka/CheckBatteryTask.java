package test.drone.service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import test.drone.entity.Drone;
import test.drone.entity.event.BatteryCheckLevelEvent;
import test.drone.repository.DroneRepository;

/**
 * Task for periodic drone's battery level check
 */
@Component
public class CheckBatteryTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckBatteryTask.class);

    @Value("${kafka.topic.name}")
    private String topicName;

    private final DroneRepository droneRepository;
    private final KafkaTemplate<String, BatteryCheckLevelEvent> kafkaTemplate;

    public CheckBatteryTask(
            DroneRepository droneRepository,
            @Autowired(required = false) KafkaTemplate<String, BatteryCheckLevelEvent> kafkaTemplate) {
        this.droneRepository = droneRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(cron = "${cron-expression}")
    public void checkBatteryStatusForDrone() {
        var existingDrones = droneRepository.findAll();

        existingDrones
                .parallelStream()
                .forEach(this::logBatteryStatus);
    }


    private void logBatteryStatus(Drone drone) {
        var event = new BatteryCheckLevelEvent(drone.getSerialNumber(), drone.getBatteryCapacity());

        // don't check result for current implementation
        if (kafkaTemplate != null) {
            kafkaTemplate.send(topicName, event);

            LOGGER.info("Log battery status sent: {}", event);
        } else {
            LOGGER.info("Kafka is disabled. Outputting event to log: {}", event);
        }
    }
}
