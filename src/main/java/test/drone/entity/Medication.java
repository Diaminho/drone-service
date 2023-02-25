package test.drone.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "medications")
@Data
public class Medication implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Pattern(regexp = "^[\\w-]*$")
    private String name;

    @Column(name = "weight")
    private Long weight;

    @Column(name = "code")
    @Pattern(regexp = "^[A-Z\\d_]*$")
    private String code;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "medication")
    Set<DroneToMedication> drones;
}
