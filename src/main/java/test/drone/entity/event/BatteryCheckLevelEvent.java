package test.drone.entity.event;


import java.util.Objects;

/**
 * Event for drone's battery level check
 */
public class BatteryCheckLevelEvent extends Event {
    private final String droneSerialNumber;
    private final Short currentBatteryLevel;

    public BatteryCheckLevelEvent(String droneSerialNumber, Short currentBatteryLevel) {
        super();
        this.droneSerialNumber = droneSerialNumber;
        this.currentBatteryLevel = currentBatteryLevel;
    }


    // TODO read jackson and private fields serialization
    public String getDroneSerialNumber() {
        return droneSerialNumber;
    }

    public Short getCurrentBatteryLevel() {
        return currentBatteryLevel;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BatteryCheckLevelEvent that = (BatteryCheckLevelEvent) obj;
        return Objects.equals(droneSerialNumber, that.droneSerialNumber) && Objects.equals(currentBatteryLevel, that.currentBatteryLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(droneSerialNumber, currentBatteryLevel);
    }

    @Override
    public String toString() {
        return "DroneBatteryCheckLevelEvent{" +
                "droneSerialNumber='" + droneSerialNumber + '\'' +
                ", currentBatteryLevel=" + currentBatteryLevel +
                ", created=" + getCreated() +
                '}';
    }
}