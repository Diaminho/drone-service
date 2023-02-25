package test.drone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import test.drone.dto.CreateDroneToMedicationDto;
import test.drone.dto.LoadedMedicationDto;
import test.drone.dto.MedicationDto;
import test.drone.entity.DroneToMedication;
import test.drone.entity.Medication;

@Mapper
public interface MedicationMapper {

    MedicationDto toDto(Medication medication);

    CreateDroneToMedicationDto toCreateDroneToMeditation(Medication medication, Short count);


    @Mapping(target = "id", source = "medication.id")
    @Mapping(target = "name", source = "medication.name")
    @Mapping(target = "weight", source = "medication.weight")
    @Mapping(target = "image", source = "medication.image")
    LoadedMedicationDto toLoadedMedicationDto(DroneToMedication droneToMedication);
}
