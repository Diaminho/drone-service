package test.drone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class DroneMedicationKey implements Serializable {

    @Column(name = "drone_serial_number")
    String droneSerialNumber;

    @Column(name = "course_id")
    Long courseId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DroneMedicationKey that = (DroneMedicationKey) o;
        return Objects.equals(droneSerialNumber, that.droneSerialNumber) && Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(droneSerialNumber, courseId);
    }
}
