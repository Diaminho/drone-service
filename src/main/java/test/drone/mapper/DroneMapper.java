package test.drone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import test.drone.dto.*;
import test.drone.entity.Drone;

import java.util.List;

/**
 * Mapper for Drone entity transformation
 */
@Mapper
public interface DroneMapper {
    /**
     * Generate Drone Entity
     * @param dto Drone information to create
     * @return Drone Entity
     */
    @Mapping(target = "state", constant = "IDLE")
    Drone fromCreateDto(CreateDroneDto dto);

    /**
     * Generate Drone battery information dto
     * @param drone Drone database information
     * @return Drone's batteryInformation
     */
    @Mapping(target = "currentLevel", source = "batteryCapacity")
    BatteryLevelDto toBatteryLevelDto(Drone drone);

    /**
     * Map drone entity and medication load information to drone load information
     * @param drone entity
     * @param medications loaded medication information
     * @return Information about loaded to Drone Medications
     */
    @Mapping(target = "currentLevel", source = "batteryCapacity")
    default DroneLoadInformationDto toDroneLoadInformation(Drone drone, List<LoadMedicationDto> medications) {
        double currentWeight = 0;

        for (var info : medications) {
            currentWeight += info.weight() * info.count();
        }

        return new DroneLoadInformationDto(medications, currentWeight, drone.getWeightLimit());
    }

    /**
     * Drone Entity to Dto
     * @param drone DB entity
     * @return Drone Dto
     */
    DroneDto droneToDto(Drone drone);
}
