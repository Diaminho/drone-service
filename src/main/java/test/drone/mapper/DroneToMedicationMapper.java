package test.drone.mapper;

import org.springframework.stereotype.Service;
import test.drone.dto.LoadMedicationDto;
import test.drone.entity.Drone;
import test.drone.entity.DroneMedicationKey;
import test.drone.entity.DroneToMedication;
import test.drone.entity.Medication;

/**
 * Mapper for link between Drone and Medication (Loaded Medications to Drone)
 */
@Service
public class DroneToMedicationMapper {
    /**
     * Generates Entity for Medication - Drone relation with medication count
     * @param drone drone info
     * @param medication medication info
     * @param count medication count
     * @return entity
     */
    public DroneToMedication generateDroneToMedication(Drone drone, Medication medication, Short count) {
        var newOne = new DroneToMedication();
        newOne.setDrone(drone);

        newOne.setMedication(medication);
        newOne.setCount(count);

        var id = new DroneMedicationKey();
        id.setMedicationId(medication.getId());
        id.setDroneSerialNumber(drone.getSerialNumber());

        newOne.setId(id);

        return newOne;
    }

    /**
     * Dto representation of loaded Medications for specific Drone
     * @param droneToMedication Drone to Loaded Medication database information
     * @param imageBase64 Medication image base64 encoded
     * @return Loaded medications as dto
     */
    public LoadMedicationDto toLoadedMedicationDto(DroneToMedication droneToMedication, String imageBase64) {
        var medication = droneToMedication.getMedication();

        return new LoadMedicationDto(
                medication.getId(),
                droneToMedication.getCount(),
                medication.getName(),
                medication.getWeight(),
                medication.getCode(),
                imageBase64
        );
    }
}
