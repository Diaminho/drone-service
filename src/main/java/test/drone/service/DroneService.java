package test.drone.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import test.drone.dto.*;
import test.drone.entity.Drone;
import test.drone.entity.DroneToMedication;
import test.drone.entity.State;
import test.drone.mapper.DroneMapper;
import test.drone.repository.DroneRepository;
import test.drone.repository.DroneToMedicationRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DroneService {
    public static final Short MINIMAL_CHARGING_FOR_WORKING = 25;
    private final DroneRepository droneRepository;

    private final DroneToMedicationRepository droneToMedicationRepository;

    private final DroneMapper droneMapper;

    private final MedicationService medicationService;

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
            // TODO custom exception
            throw new RuntimeException("Cannot found drone by serial number: " + droneSerialNumber);
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

    public DroneDto loadDrone(LoadDroneDto loadDroneInformation) {
        // check is droneAvailableForLoading
        // STATE == LOADING
        // battery level - 5
        // MedicationList add
        var foundDrone = droneRepository
                .getReferenceById(loadDroneInformation.serialNumber());
        if (!isAvailableForLoading(foundDrone)) {
            //TODO move to custom exception
            throw new RuntimeException("Drone is busy");
        }

        var foundMedications = loadDroneInformation
                .loadedMedications()
                .stream()
                .map(item -> {
                    var found = medicationService.getByIdEntity(item.id());
                    return medicationService.toCreateDroneToMeditation(found, item.count());
                })
                .toList();

        if (!isLoadingMedicationsAreFitInDrone(foundDrone, foundMedications)) {
            //TODO move to custom exception
            throw new RuntimeException("Drone max limit is --- attempted loading --");
        }

        foundDrone.setState(State.LOADING);

        // generate droneToMedication
        foundMedications
                .forEach(item -> {
                    var newOne = new DroneToMedication();
                    newOne.setDrone(foundDrone);
                    newOne.setMedication(item.medication());
                    newOne.setCount(item.count());

                    droneToMedicationRepository.save(newOne);
                });

        var saved = droneRepository.save(foundDrone);

        //TODO add 404 check
        return droneMapper.droneToDto(saved);
    }

    public List<LoadedMedicationDto> checkLoadingDrone(String droneSerialNumber) {
        var foundDrone = droneRepository
                .getReferenceById(droneSerialNumber);

        return medicationService.findAllLoadedMedicationsForDrone(foundDrone);
    }

    private boolean isAvailableForLoading(Drone drone) {
        boolean isChargedEnough = drone.getBatteryCapacity() > MINIMAL_CHARGING_FOR_WORKING;
        boolean isFree = drone.getState() == State.IDLE;
        return isChargedEnough && isFree;
    }

    private boolean isLoadingMedicationsAreFitInDrone(Drone drone, List<CreateDroneToMedicationDto> medicationInfo) {
        var capacity = drone.getWeightLimit();

        AtomicLong currentWeight = new AtomicLong();
        medicationInfo.forEach(info -> currentWeight.addAndGet(info.medication().getWeight() * info.count()));

        return currentWeight.get() > capacity;
    }
}
