package test.drone.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.drone.dto.*;
import test.drone.entity.Drone;
import test.drone.entity.State;
import test.drone.exception.DroneBusyException;
import test.drone.exception.DroneDischargedException;
import test.drone.exception.DroneNotFoundException;
import test.drone.exception.DroneOverweightException;
import test.drone.mapper.DroneMapper;
import test.drone.mapper.DroneToMedicationMapper;
import test.drone.repository.DroneRepository;
import test.drone.repository.DroneToMedicationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DroneService {
    public static final Short MINIMAL_CHARGING_FOR_WORKING = 25;
    private final DroneRepository droneRepository;
    private final DroneToMedicationRepository droneToMedicationRepository;

    private final DroneToMedicationMapper droneToMedicationMapper;
    private final DroneMapper droneMapper;
    private final MedicationService medicationService;

    @Transactional(rollbackFor = Exception.class)
    public DroneDto registerDrone(@Valid CreateDroneDto createDroneDto) {
        Drone drone = droneMapper.fromCreateDto(createDroneDto);
        var saved = droneRepository.save(drone);

        return droneMapper.droneToDto(saved);
    }

    public BatteryLevelDto getBatteryLevel(String droneSerialNumber) {
        try {
            var found = droneRepository.getReferenceById(droneSerialNumber);
            return droneMapper.toBatteryLevelDto(found);
        } catch (EntityNotFoundException e) {
            throw new DroneNotFoundException(droneSerialNumber);
        }
    }

    public List<DroneDto> getAvailableDrones() {
        var foundDrones = droneRepository
                .findAllAvailableForLoading();

        return foundDrones
                .stream()
                .map(droneMapper::droneToDto)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public DroneDto loadDrone(LoadDroneDto loadDroneInformation) {
        var foundDrone = droneRepository.getReferenceById(loadDroneInformation.serialNumber());

        // check battery level
        if (foundDrone.getBatteryCapacity() < MINIMAL_CHARGING_FOR_WORKING) {
            throw new DroneDischargedException();
        }

        // check state
        if (foundDrone.getState() != State.IDLE) {
            throw new DroneBusyException();
        }

        var foundMedications = getMedicationInformationForLoad(loadDroneInformation);

        // calculate total load weight
        var maxWeight = foundDrone.getWeightLimit();
        var totalWeight = calculateTotalWeight(foundMedications);

        // check totalWeight
        if (totalWeight > maxWeight) {
            throw new DroneOverweightException(maxWeight, totalWeight);
        }

        foundDrone.setState(State.LOADING);

        // generate droneToMedication
        generateDroneToMedicationInformation(foundDrone, foundMedications);

        var saved = droneRepository.save(foundDrone);
        return droneMapper.droneToDto(saved);
    }

    public DroneLoadInformation checkLoadingDrone(String droneSerialNumber) {
        var foundDrone = droneRepository
                .getReferenceById(droneSerialNumber);

        var loadedMedications = medicationService.findAllLoadedMedicationsForDrone(foundDrone);
        return droneMapper.toDroneLoadInformation(foundDrone, loadedMedications);
    }

    private Double calculateTotalWeight(List<CreateDroneToMedicationDto> medicationInfo) {
        double currentWeight = 0;

        for (var info : medicationInfo) {
            currentWeight += info.medication().getWeight() * info.count();
        }

        return currentWeight;
    }

    private void generateDroneToMedicationInformation (Drone foundDrone, List<CreateDroneToMedicationDto> foundMedications) {
        foundMedications
                .forEach(item -> {
                    var entity = droneToMedicationMapper.toDroneToMediation(foundDrone, item);
                    droneToMedicationRepository.save(entity);
                });
    }

    private List<CreateDroneToMedicationDto> getMedicationInformationForLoad(LoadDroneDto loadDroneInformation) {
        return loadDroneInformation
                .loadedMedications()
                .stream()
                .map(item -> {
                    var found = medicationService.getByIdEntity(item.id());
                    return medicationService.toCreateDroneToMeditation(found, item.count());
                })
                .toList();
    }
}
