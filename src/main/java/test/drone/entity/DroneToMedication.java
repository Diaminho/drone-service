package test.drone.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "drones_to_medications")
@Data
public class DroneToMedication implements Serializable {
    @EmbeddedId
    private DroneMedicationKey id;

    @ManyToOne
    @MapsId("droneSerialNumber")
    @JoinColumn(name = "drone_serial_number")
    private Drone drone;

    @ManyToOne
    @MapsId("medicationId")
    @JoinColumn(name = "medication_id")
    private Medication medication;

    @Column(name = "count")
    @Min(1)
    private Short count;
}
