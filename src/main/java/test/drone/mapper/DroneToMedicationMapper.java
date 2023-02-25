package test.drone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import test.drone.dto.CreateDroneToMedicationDto;
import test.drone.dto.LoadMedicationDto;
import test.drone.entity.Drone;
import test.drone.entity.DroneMedicationKey;
import test.drone.entity.DroneToMedication;

@Mapper
public interface DroneToMedicationMapper {

    default DroneToMedication toDroneToMediation(Drone drone, CreateDroneToMedicationDto item) {
        var newOne = new DroneToMedication();
        newOne.setDrone(drone);
        newOne.setMedication(item.medication());
        newOne.setCount(item.count());

        var id = new DroneMedicationKey();
        id.setMedicationId(item.medication().getId());
        id.setDroneSerialNumber(drone.getSerialNumber());

        newOne.setId(id);

        return newOne;
    }

    @Mapping(target = "id", source = "medication.id")
    @Mapping(target = "name", source = "medication.name")
    @Mapping(target = "weight", source = "medication.weight")
    @Mapping(target = "image", source = "medication.image")
    @Mapping(target = "code", source = "medication.code")
    LoadMedicationDto toLoadedMedicationDto(DroneToMedication droneToMedication);
}
