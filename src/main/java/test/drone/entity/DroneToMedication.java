package test.drone.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "drones_to_medications")
@Data
public class DroneToMedication implements Serializable {
    @EmbeddedId
    private DroneMedicationKey id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "drone_serial_number")
    private Drone drone;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "medication_id")
    private Medication medication;

    @Column(name = "count")
    @Min(1)
    private Short count;
}
