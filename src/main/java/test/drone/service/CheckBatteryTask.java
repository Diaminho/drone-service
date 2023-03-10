package test.drone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import test.drone.entity.Drone;
import test.drone.entity.event.DroneBatteryCheckLevelEvent;
import test.drone.repository.DroneRepository;

/**
 * Task for periodic drone's battery level check
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CheckBatteryTask {
    @Value("${kafka.topic.name}")
    private String topicName;

    private final DroneRepository droneRepository;
    private final KafkaTemplate<String, DroneBatteryCheckLevelEvent> kafkaTemplate;

    @Scheduled(cron = "${cron-expression}")
    public void checkBatteryStatusForDrone() {
        var existingDrones = droneRepository.findAll();

        existingDrones
                .parallelStream()
                .forEach(this::logBatteryStatus);
    }


    private void logBatteryStatus(Drone drone) {
        var event = new DroneBatteryCheckLevelEvent();
        event.setCurrentBatteryLevel(drone.getBatteryCapacity());
        event.setDroneSerialNumber(drone.getSerialNumber());

        // don't check result for current implementation
        kafkaTemplate.send(topicName, event);
    }
}
