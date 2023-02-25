package test.drone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class DroneMedicationKey implements Serializable {
    @Column(name = "drone_serial_number")
    private String droneSerialNumber;

    @Column(name = "medication_id")
    private Long medicationId;

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
