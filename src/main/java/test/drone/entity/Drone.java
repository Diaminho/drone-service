package test.drone.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "drones")
public class Drone implements Serializable {
    @Id
    @Size(min = 1, max = 100)
    private String serialNumber;

    @Column(name = "model", nullable = false)
    @Enumerated(EnumType.STRING)
    private Model model;

    @Min(0)
    @Max(500)
    @Column(name = "weight_limit", nullable = false)
    private Double weightLimit;

    @Min(0)
    @Max(100)
    @Column(name = "battery_capacity", nullable = false)
    private Short batteryCapacity;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @OneToMany(mappedBy = "drone")
    private Set<DroneToMedication> medications;

    public String getSerialNumber() {
        return serialNumber;
    }

    public Model getModel() {
        return model;
    }

    public Double getWeightLimit() {
        return weightLimit;
    }

    public Short getBatteryCapacity() {
        return batteryCapacity;
    }

    public State getState() {
        return state;
    }

    public Set<DroneToMedication> getMedications() {
        return medications;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setWeightLimit(Double weightLimit) {
        this.weightLimit = weightLimit;
    }

    public void setBatteryCapacity(Short batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Drone drone = (Drone) obj;
        return Objects.equals(serialNumber, drone.serialNumber)
                && model == drone.model
                && Objects.equals(weightLimit, drone.weightLimit)
                && Objects.equals(batteryCapacity, drone.batteryCapacity)
                && state == drone.state
                && Objects.equals(medications, drone.medications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNumber, model, weightLimit, batteryCapacity, state, medications);
    }
}
