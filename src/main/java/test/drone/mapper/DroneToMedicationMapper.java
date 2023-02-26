package test.drone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import test.drone.dto.CreateDroneToMedicationDto;
import test.drone.dto.LoadMedicationDto;
import test.drone.entity.Drone;
import test.drone.entity.DroneMedicationKey;
import test.drone.entity.DroneToMedication;

/**
 * Mapper for link between Drone and Medication (Loaded Medications to Drone)
 */
@Mapper
public interface DroneToMedicationMapper {


    /**
     * Generates DroneToMedication Entity to link loaded medications and drone
     * @param drone Drone information
     * @param item Loaded Medication information
     * @return DroneToMedication Entity
     */
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

    /**
     * Dto representation of loaded Medications for specific Drone
     * @param droneToMedication Drone to Loaded Medication database information
     * @return Loaded medications as dto
     */
    @Mapping(target = "id", source = "medication.id")
    @Mapping(target = "name", source = "medication.name")
    @Mapping(target = "weight", source = "medication.weight")
    @Mapping(target = "image", source = "medication.image")
    @Mapping(target = "code", source = "medication.code")
    LoadMedicationDto toLoadedMedicationDto(DroneToMedication droneToMedication);
}
