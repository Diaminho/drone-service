package test.drone.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "medications")
public class Medication implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Pattern(regexp = "^[\\w-]*$")
    private String name;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "code")
    @Pattern(regexp = "^[A-Z\\d_]*$")
    private String code;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "medication")
    private Set<DroneToMedication> drones;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getWeight() {
        return weight;
    }

    public String getCode() {
        return code;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Set<DroneToMedication> getDrones() {
        return drones;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setImageUrl(String image) {
        this.imageUrl = image;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Medication that = (Medication) obj;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(weight, that.weight)
                && Objects.equals(code, that.code) && Objects.equals(imageUrl, that.imageUrl) && Objects.equals(drones, that.drones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, weight, code, imageUrl, drones);
    }
}
