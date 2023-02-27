package test.drone.service;

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

/**
 * Service to manipulate Drone
 */
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

    /**
     * Create a new drone
     * @param createDroneDto information to create drone
     * @return saved drone as dto representation
     */
    @Transactional(rollbackFor = Exception.class)
    public DroneDto registerDrone(CreateDroneDto createDroneDto) {
        // validate uniqeu
        try {
            getDrone(createDroneDto.serialNumber());
        } catch (DroneNotFoundException e) {
            //it is ok
        }

        log.info("Registering a drone: {}", createDroneDto);
        Drone drone = droneMapper.fromCreateDto(createDroneDto);
        var saved = droneRepository.save(drone);

        var result =  droneMapper.droneToDto(saved);

        log.info("Drone is registered: {}", result);

        return result;
    }

    /**
     * Receives a battery information for specific drone
     * @param droneSerialNumber of drone
     * @return Drone's battery information
     */
    public BatteryLevelDto getBatteryLevel(String droneSerialNumber) {
        return droneMapper.toBatteryLevelDto(getDrone(droneSerialNumber));
    }

    /**
     * Find all drones available for loading
     * @return List of available drones
     */
    public List<DroneDto> getAvailableForLoadingDrones() {
        var foundDrones = droneRepository
                .findAllAvailableForLoading();

        return foundDrones
                .stream()
                .map(droneMapper::droneToDto)
                .collect(Collectors.toList());
    }

    /**
     * Load drone with medications
     * @param loadDroneInformation medication and drone information
     * @return drone information
     */
    @Transactional(rollbackFor = Exception.class)
    public DroneDto loadDrone(LoadDroneDto loadDroneInformation) {
        var serialNumber = loadDroneInformation.serialNumber();
        log.info("Medications loading process is started for drone: {}", serialNumber);

        var foundDrone = getDrone(serialNumber);

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

        // add images

        var saved = droneRepository.save(foundDrone);
        var result = droneMapper.droneToDto(saved);

        log.info("Drone is loading: {}", result);

        return result;
    }

    /**
     * Check loaded medications for drone
     * @param droneSerialNumber of drone
     * @return information about loaded medications
     */
    public DroneLoadInformationDto checkLoadingDrone(String droneSerialNumber) {
        var foundDrone = getDrone(droneSerialNumber);

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
                    log.info("Medication {} is loading to drone: {}", item.medication(), entity.getDrone());
                });
    }

    private List<CreateDroneToMedicationDto> getMedicationInformationForLoad(LoadDroneDto loadDroneInformation) {
        return loadDroneInformation
                .loadedMedications()
                .stream()
                .map(item -> {
                    var found = medicationService.findById(item.id());
                    return medicationService.toCreateDroneToMedication(found, item.count());
                })
                .toList();
    }

    private Drone getDrone(String droneSerialNumber) {
        return droneRepository.findById(droneSerialNumber).orElseThrow(() -> new DroneNotFoundException(droneSerialNumber));
    }
}
