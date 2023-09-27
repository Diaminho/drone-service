package test.drone.service;

import org.springframework.stereotype.Service;
import test.drone.dto.CreateDroneToMedicationDto;
import test.drone.dto.LoadMedicationDto;
import test.drone.entity.Drone;
import test.drone.entity.DroneToMedication;
import test.drone.entity.Medication;
import test.drone.exception.MedicationNotFoundException;
import test.drone.mapper.DroneToMedicationMapper;
import test.drone.mapper.MedicationMapper;
import test.drone.repository.DroneToMedicationRepository;
import test.drone.repository.MedicationRepository;

import java.util.List;

@Service
public class MedicationService {
    private final MedicationRepository medicationRepository;
    private final DroneToMedicationRepository droneToMedicationRepository;
    private final MedicationMapper medicationMapper;
    private final DroneToMedicationMapper droneToMedicationMapper;

    private final MinioService minioService;

    public MedicationService(
            MedicationRepository medicationRepository,
            DroneToMedicationRepository droneToMedicationRepository,
            MedicationMapper medicationMapper,
            DroneToMedicationMapper droneToMedicationMapper,
            MinioService minioService) {

        this.medicationRepository = medicationRepository;
        this.droneToMedicationRepository = droneToMedicationRepository;
        this.medicationMapper = medicationMapper;
        this.droneToMedicationMapper = droneToMedicationMapper;
        this.minioService = minioService;
    }

    public Medication findById(Long id) {
        return medicationRepository.findById(id).orElseThrow(() -> new MedicationNotFoundException(id));
    }

    /**
     * Returns information about current medication weight.
     * @param medication Medication information
     * @param count Medication count
     * @return information about current medication weight.
     */
    public CreateDroneToMedicationDto toCreateDroneToMedication(Medication medication, Short count) {
        return medicationMapper.toCreateDroneToMedication(medication, count);
    }

    /**
     * Method for load all information about loaded to drone medications
     * @param drone Drone object to search medications
     * @return List of loaded medication information
     */
    public List<LoadMedicationDto> findAllLoadedMedicationsForDrone(Drone drone) {
        List<DroneToMedication> found = droneToMedicationRepository.findAllByDrone(drone);

        // TODO check max threads for MinIO
        return found
                .parallelStream()
                .map(droneToMedication -> {
                    var base64Image = minioService.downloadFileAsBase64(droneToMedication.getMedication().getImageUrl());
                    return droneToMedicationMapper.toLoadedMedicationDto(droneToMedication, base64Image);
                })
                .toList();
    }
}
