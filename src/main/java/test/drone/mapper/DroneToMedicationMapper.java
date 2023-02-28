package test.drone.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import test.drone.dto.CreateDroneToMedicationDto;
import test.drone.dto.LoadMedicationDto;
import test.drone.entity.Drone;
import test.drone.entity.DroneMedicationKey;
import test.drone.entity.DroneToMedication;
import test.drone.service.MinioService;

/**
 * Mapper for link between Drone and Medication (Loaded Medications to Drone)
 */
@Component
@RequiredArgsConstructor
public class DroneToMedicationMapper {
    private final MinioService minioService;

    /**
     * Generates DroneToMedication Entity to link loaded medications and drone
     * @param drone Drone information
     * @param item Loaded Medication information
     * @return DroneToMedication Entity
     */
    public DroneToMedication toDroneToMediation(Drone drone, CreateDroneToMedicationDto item) {
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
    public LoadMedicationDto toLoadedMedicationDto(DroneToMedication droneToMedication) {
        var medication = droneToMedication.getMedication();

        // download image from minio
        var base64Image = minioService.downloadFile(medication.getImage());

        return new LoadMedicationDto(
                medication.getId(),
                droneToMedication.getCount(),
                medication.getName(),
                medication.getWeight(),
                medication.getCode(),
                base64Image
        );
    }
}
