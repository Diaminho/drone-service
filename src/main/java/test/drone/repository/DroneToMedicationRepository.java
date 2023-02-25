package test.drone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.drone.entity.Drone;
import test.drone.entity.DroneMedicationKey;
import test.drone.entity.DroneToMedication;

import java.util.List;

@Repository
public interface DroneToMedicationRepository extends JpaRepository<DroneToMedication, DroneMedicationKey> {
    List<DroneToMedication> findAllByDrone(Drone drone);
}
