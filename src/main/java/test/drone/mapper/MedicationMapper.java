package test.drone.mapper;
import org.springframework.stereotype.Service;
import test.drone.dto.CreateDroneToMedicationDto;
import test.drone.dto.MedicationDto;
import test.drone.entity.Medication;

/**
 * Mapper for Medication object transformation
 */
@Service
public class MedicationMapper {

    /**
     * Transforms database object to dto
     * @param medication database entity
     * @return Medication dto
     */
    public MedicationDto toDto(Medication medication) {
        return new MedicationDto(
                medication.getId(),
                medication.getName(),
                medication.getWeight(),
                medication.getCode(),
                medication.getImageUrl()
        );
    }

    /**
     * Creates dto to create DroneToMedication Entity
     * @param medication Medication information
     * @param count Medication count
     * @return dto
     */
    public CreateDroneToMedicationDto toCreateDroneToMedication(Medication medication, Short count) {
        var medicationDto = toDto(medication);
        return new CreateDroneToMedicationDto(medicationDto, count);
    }
}
