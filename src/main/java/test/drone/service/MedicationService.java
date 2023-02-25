package test.drone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import test.drone.dto.CreateDroneToMedicationDto;
import test.drone.dto.LoadedMedicationDto;
import test.drone.dto.MedicationDto;
import test.drone.entity.Drone;
import test.drone.entity.DroneToMedication;
import test.drone.entity.Medication;
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

    public MedicationDto getById(Long id) {
        var found = getByIdEntity(id);
        return medicationMapper.toDto(found);
    }

    public Medication getByIdEntity(Long id) {
        return medicationRepository.getReferenceById(id);
    }

    public List<MedicationDto> findAll() {
        var found = medicationRepository.findAll();

        return found
                .stream()
                .map(medicationMapper::toDto)
                .collect(Collectors.toList());
    }

    public CreateDroneToMedicationDto toCreateDroneToMeditation (Medication medication, Short count) {
        return medicationMapper.toCreateDroneToMeditation(medication, count);
    }

    public List<LoadedMedicationDto> findAllLoadedMedicationsForDrone(Drone drone) {
        List<DroneToMedication> found = droneToMedicationRepository.findAllByDrone(drone);

        return found
                .stream()
                .map(medicationMapper::toLoadedMedicationDto)
                .collect(Collectors.toList());
    }
}
