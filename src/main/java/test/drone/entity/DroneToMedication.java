package test.drone.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "drones_to_medications")
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

    public DroneMedicationKey getId() {
        return id;
    }

    public Drone getDrone() {
        return drone;
    }

    public Medication getMedication() {
        return medication;
    }

    public Short getCount() {
        return count;
    }

    public void setId(DroneMedicationKey id) {
        this.id = id;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public void setCount(Short count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DroneToMedication that = (DroneToMedication) obj;
        return Objects.equals(id, that.id) && Objects.equals(drone, that.drone) && Objects.equals(medication, that.medication) && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, drone, medication, count);
    }
}
