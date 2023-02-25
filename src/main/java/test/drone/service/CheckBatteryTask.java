package test.drone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import test.drone.entity.Drone;
import test.drone.entity.event.DroneBatteryCheckLevelEvent;
import test.drone.repository.DroneRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckBatteryTask {
    private final DroneRepository droneRepository;

    @Scheduled(cron = "${cron-expression}")
    public void checkBatteryStatusForDrone() {
        log.info("checkBatteryStatusForDrone is started");
        var existingDrones = droneRepository.findAll();

        existingDrones
                .parallelStream()
                .forEach(this::logBatteryStatus);

        log.info("checkBatteryStatusForDrone is finished");
    }


    // TODO think about persistence, kafka etc
    private void logBatteryStatus(Drone drone) {
        var event = new DroneBatteryCheckLevelEvent();
        event.setCurrentBatteryLevel(drone.getBatteryCapacity());
        event.setDroneSerialNumber(drone.getSerialNumber());

        log.info("Event: " + event);
    }
}
