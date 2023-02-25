package test.drone.entity.event;

import lombok.Data;

@Data
public class DroneBatteryCheckLevelEvent extends Event {
    private String droneSerialNumber;
    private Short currentBatteryLevel;
}