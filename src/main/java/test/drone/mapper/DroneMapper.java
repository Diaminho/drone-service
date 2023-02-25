package test.drone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import test.drone.dto.BatteryLevelDto;
import test.drone.dto.CreateDroneDto;
import test.drone.dto.DroneDto;
import test.drone.entity.Drone;

@Mapper
public interface DroneMapper {

    @Mapping(target = "state", constant = "IDLE")
    Drone fromCreateDto(CreateDroneDto dto);

    @Mapping(target = "currentLevel", source = "batteryCapacity")
    BatteryLevelDto toBatteryLevelDto(Drone drone);

    @Mapping(target = "loadedMedications", expression = "java(null)")
    DroneDto droneToDto(Drone drone);
}
