package test.drone.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "drones")
@Data
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
    Set<DroneToMedication> medications;
}
