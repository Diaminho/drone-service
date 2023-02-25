package test.drone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.drone.entity.Medication;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
}
