package test.drone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DroneMedicationKey implements Serializable {
    @Column(name = "drone_serial_number")
    private String droneSerialNumber;

    @Column(name = "medication_id")
    private Long medicationId;

    public String getDroneSerialNumber() {
        return droneSerialNumber;
    }

    public Long getMedicationId() {
        return medicationId;
    }

    public void setDroneSerialNumber(String droneSerialNumber) {
        this.droneSerialNumber = droneSerialNumber;
    }

    public void setMedicationId(Long medicationId) {
        this.medicationId = medicationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DroneMedicationKey that = (DroneMedicationKey) o;
        return Objects.equals(droneSerialNumber, that.droneSerialNumber) && Objects.equals(medicationId, that.medicationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(droneSerialNumber, medicationId);
    }
}
