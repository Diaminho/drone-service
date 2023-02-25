package test.drone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import test.drone.dto.*;
import test.drone.entity.Drone;
import test.drone.entity.DroneMedicationKey;
import test.drone.entity.DroneToMedication;

import java.util.List;

@Mapper
public interface DroneMapper {

    @Mapping(target = "state", constant = "IDLE")
    Drone fromCreateDto(CreateDroneDto dto);

    @Mapping(target = "currentLevel", source = "batteryCapacity")
    BatteryLevelDto toBatteryLevelDto(Drone drone);

    @Mapping(target = "currentLevel", source = "batteryCapacity")
    default DroneLoadInformation toDroneLoadInformation(Drone drone, List<LoadMedicationDto> medications) {
        double currentWeight = 0;

        for (var info : medications) {
            currentWeight += info.weight() * info.count();
        }

        return new DroneLoadInformation(medications, currentWeight, drone.getWeightLimit());
    }

    @Mapping(target = "loadedMedications", expression = "java(null)")
    DroneDto droneToDto(Drone drone);
}
