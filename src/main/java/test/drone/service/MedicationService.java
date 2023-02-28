package test.drone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MedicationService {
    private final MedicationRepository medicationRepository;
    private final DroneToMedicationRepository droneToMedicationRepository;
    private final MedicationMapper medicationMapper;
    private final DroneToMedicationMapper droneToMedicationMapper;

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

        return found
                .stream()
                .map(droneToMedicationMapper::toLoadedMedicationDto)
                .collect(Collectors.toList());
    }
}
