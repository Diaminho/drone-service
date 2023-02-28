package test.drone.entity.event;

import lombok.Data;

/**
 * Event for drone's battery level check
 */
@Data
public class DroneBatteryCheckLevelEvent extends Event {
    private String droneSerialNumber;
    private Short currentBatteryLevel;
}