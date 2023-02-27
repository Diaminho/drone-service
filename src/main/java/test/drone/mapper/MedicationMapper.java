package test.drone.mapper;

import org.mapstruct.Mapper;
import test.drone.dto.CreateDroneToMedicationDto;
import test.drone.dto.MedicationDto;
import test.drone.entity.Medication;

/**
 * Mapper for Medication object transformation
 */
@Mapper
public interface MedicationMapper {

    /**
     * Transforms database object to dto
     * @param medication database entity
     * @return Medication dto
     */
    MedicationDto toDto(Medication medication);

    /**
     * Creates dto to create DroneToMedication Entity
     * @param medication Medication information
     * @param count Medication count
     * @return dto
     */
    CreateDroneToMedicationDto toCreateDroneToMedication(Medication medication, Short count);
}
