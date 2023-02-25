package test.drone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import test.drone.entity.Drone;

import java.util.List;

@Repository
public interface DroneRepository extends JpaRepository<Drone, String> {
    @Query("SELECT d FROM Drone d WHERE d.state = 'IDLE' AND d.batteryCapacity > 25")
    List<Drone> findAllAvailableForLoading();
}
