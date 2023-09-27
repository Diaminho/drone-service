package test.drone.mapper;

import org.springframework.stereotype.Service;
import test.drone.dto.*;
import test.drone.entity.Drone;
import test.drone.entity.State;

import java.util.List;

/**
 * Mapper for Drone entity transformation
 */
@Service
public class DroneMapper {
    /**
     * Generate Drone Entity
     * @param dto Drone information to create
     * @return Drone Entity
     */
    public Drone fromCreateDto(CreateDroneDto dto) {
       var drone =  new Drone();

       drone.setSerialNumber(dto.serialNumber());
       drone.setModel(dto.model());
       drone.setWeightLimit(dto.weightLimit());
       // The default state of the new drone
       drone.setState(State.IDLE);

       drone.setBatteryCapacity(dto.batteryCapacity());

       return drone;
    }

    /**
     * Generate Drone battery information dto
     * @param drone Drone database information
     * @return Drone's batteryInformation
     */
    public BatteryLevelDto toBatteryLevelDto(Drone drone) {
        return new BatteryLevelDto(drone.getBatteryCapacity());
    }

    /**
     * Map drone entity and medication load information to drone load information
     * @param drone entity
     * @param medications loaded medication information
     * @return Information about loaded to Drone Medications
     */
    public DroneLoadInformationDto toDroneLoadInformation(Drone drone, List<LoadMedicationDto> medications) {
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
    public DroneDto droneToDto(Drone drone) {
        return new DroneDto(
                drone.getSerialNumber(), drone.getModel(), drone.getWeightLimit(),
                drone.getBatteryCapacity(), drone.getState()
        );
    }
}
