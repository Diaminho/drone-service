package test.drone.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.drone.dto.*;
import test.drone.entity.Drone;
import test.drone.entity.DroneToMedication;
import test.drone.entity.Medication;
import test.drone.entity.State;
import test.drone.exception.DroneBusyException;
import test.drone.exception.DroneDischargedException;
import test.drone.exception.DroneNotFoundException;
import test.drone.exception.DroneOverweightException;
import test.drone.mapper.DroneMapper;
import test.drone.mapper.DroneToMedicationMapper;
import test.drone.repository.DroneRepository;
import test.drone.repository.DroneToMedicationRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DroneServiceTest {
    @Mock
    private DroneRepository droneRepository;

    @Mock
    private DroneMapper droneMapper;

    @Mock
    private MedicationService medicationService;

    @Mock
    private DroneToMedicationMapper droneToMedicationMapper;

    @Mock
    private DroneToMedicationRepository droneToMedicationRepository;
    @InjectMocks
    private DroneService droneService;

    @Test
    void batteryLevelOkTest() {
        var droneSerialNumber = "212";

        var drone = mock(Drone.class);
        when(droneRepository.findById(droneSerialNumber)).thenReturn(Optional.of(drone));

        var batteryLevelDto = mock(BatteryLevelDto.class);
        when(droneMapper.toBatteryLevelDto(drone)).thenReturn(batteryLevelDto);

        var response = droneService.getBatteryLevel(droneSerialNumber);

        assertEquals(batteryLevelDto, response);
    }

    @Test
    void batteryLevelNotFoundTest() {
        var droneSerialNumber = "212";

        when(droneRepository.findById(droneSerialNumber)).thenThrow(new DroneNotFoundException("test"));

        assertThrows(DroneNotFoundException.class, () -> droneService.getBatteryLevel(droneSerialNumber));

        verify(droneMapper, times(0)).toBatteryLevelDto(any());
    }

    @Test
    void availableDronesForLoadingTest() {
        var drone = mock(Drone.class);
        when(droneRepository.findAllAvailableForLoading()).thenReturn(Collections.singletonList(drone));

        var droneDto = mock(DroneDto.class);
        when(droneMapper.droneToDto(drone)).thenReturn(droneDto);

        var result = droneService.getAvailableForLoadingDrones();

        assertEquals(Collections.singletonList(droneDto), result);
    }

    @Test
    void checkLoadingDroneTest() {
        var droneSerialNumber = "22";

        var drone = mock(Drone.class);
        when(droneRepository.findById(droneSerialNumber)).thenReturn(Optional.of(drone));

        var loadMedicationDto = mock(LoadMedicationDto.class);
        var listLoadMedicationDto = Collections.singletonList(loadMedicationDto);
        when(medicationService.findAllLoadedMedicationsForDrone(drone)).thenReturn(listLoadMedicationDto);

        var droneDto = mock(DroneLoadInformationDto.class);
        when(droneMapper.toDroneLoadInformation(drone, listLoadMedicationDto)).thenReturn(droneDto);

        var result = droneService.checkLoadingDrone(droneSerialNumber);

        assertEquals(droneDto, result);
    }

    @Test
    void createDroneTest() {
        var createDroneDto = mock(CreateDroneDto.class);

        var drone = mock(Drone.class);
        when(droneMapper.fromCreateDto(createDroneDto)).thenReturn(drone);

        when(droneRepository.save(drone)).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        var droneDto = mock(DroneDto.class);
        when(droneMapper.droneToDto(drone)).thenReturn(droneDto);

        var result = droneService.registerDrone(createDroneDto);

        assertEquals(droneDto, result);
    }

    @Test
    void loadDroneOk() {
        var serialNumber = "ww";
        var loadDroneDto = mock(LoadDroneDto.class);
        when(loadDroneDto.serialNumber()).thenReturn(serialNumber);

        double weightLimit = 499;
        var drone = mock(Drone.class);
        when(drone.getBatteryCapacity()).thenReturn((short) 26);
        when(drone.getState()).thenReturn(State.IDLE);
        when(drone.getWeightLimit()).thenReturn(weightLimit);
        when(droneRepository.findById(serialNumber)).thenReturn(Optional.of(drone));

        var medicationId = 1L;
        short count = 1;
        var createLoadMedicationDto = mock(CreateLoadMedicationDto.class);
        when(createLoadMedicationDto.id()).thenReturn(medicationId);
        when(createLoadMedicationDto.count()).thenReturn(count);

        when(loadDroneDto.loadedMedications()).thenReturn(Collections.singletonList(createLoadMedicationDto));

        double weight = 25;
        var medication = mock(Medication.class);
        when(medicationService.findById(medicationId)).thenReturn(medication);

        var createDroneToMedicationDto = mock(CreateDroneToMedicationDto.class);

        var medicationDto = mock(MedicationDto.class);
        when(medicationDto.id()).thenReturn(medicationId);

        when(medicationDto.weight()).thenReturn(weight);
        when(createDroneToMedicationDto.medication()).thenReturn(medicationDto);
        when(createDroneToMedicationDto.count()).thenReturn(count);
        when(medicationService.toCreateDroneToMedication(eq(medication), any())).thenReturn(createDroneToMedicationDto);

        var droneDto = mock(DroneDto.class);
        when(droneMapper.droneToDto(drone)).thenReturn(droneDto);

        when(medicationService.findById(medicationId)).thenReturn(medication);

        var droneToMedication = mock(DroneToMedication.class);
        when(droneToMedicationMapper.generateDroneToMedication(drone, medication, count))
                .thenReturn(droneToMedication);

        when(droneRepository.save(drone)).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        var result = droneService.loadDrone(loadDroneDto);

        assertEquals(droneDto, result);

        verify(droneToMedicationRepository, times(1)).save(droneToMedication);
        verify(droneRepository, times(1)).save(drone);
    }

    @Test
    void loadDroneDischarged() {
        var serialNumber = "ww";
        var loadDroneDto = mock(LoadDroneDto.class);
        when(loadDroneDto.serialNumber()).thenReturn(serialNumber);

        var drone = mock(Drone.class);
        when(drone.getBatteryCapacity()).thenReturn((short) 24);
        when(droneRepository.findById(serialNumber)).thenReturn(Optional.of(drone));

        assertThrows(DroneDischargedException.class, () -> droneService.loadDrone(loadDroneDto));

        verify(droneToMedicationRepository, times(0)).save(any());
        verify(droneRepository, times(0)).save(drone);
    }

    @Test
    void loadDroneIsBusy() {
        var serialNumber = "ww";
        var loadDroneDto = mock(LoadDroneDto.class);
        when(loadDroneDto.serialNumber()).thenReturn(serialNumber);

        var drone = mock(Drone.class);
        when(drone.getBatteryCapacity()).thenReturn((short) 26);
        when(drone.getState()).thenReturn(State.LOADING);
        when(droneRepository.findById(serialNumber)).thenReturn(Optional.of(drone));

        assertThrows(DroneBusyException.class, () -> droneService.loadDrone(loadDroneDto));

        verify(droneToMedicationRepository, times(0)).save(any());
        verify(droneRepository, times(0)).save(drone);
    }

    @Test
    void loadDroneOverweight() {
        var serialNumber = "ww";
        var loadDroneDto = mock(LoadDroneDto.class);
        when(loadDroneDto.serialNumber()).thenReturn(serialNumber);

        double weightLimit = 20;
        var drone = mock(Drone.class);
        when(drone.getBatteryCapacity()).thenReturn((short) 26);
        when(drone.getState()).thenReturn(State.IDLE);
        when(drone.getWeightLimit()).thenReturn(weightLimit);
        when(droneRepository.findById(serialNumber)).thenReturn(Optional.of(drone));

        var medicationId = 1L;
        short count = 1;
        var createLoadMedicationDto = mock(CreateLoadMedicationDto.class);
        when(createLoadMedicationDto.id()).thenReturn(medicationId);
        when(createLoadMedicationDto.count()).thenReturn(count);

        when(loadDroneDto.loadedMedications()).thenReturn(Collections.singletonList(createLoadMedicationDto));

        double weight = 25;
        var medication = mock(Medication.class);
        when(medicationService.findById(medicationId)).thenReturn(medication);

        var createDroneToMedicationDto = mock(CreateDroneToMedicationDto.class);

        var medicationDto = mock(MedicationDto.class);
        when(medicationDto.weight()).thenReturn(weight);
        when(createDroneToMedicationDto.medication()).thenReturn(medicationDto);
        when(createDroneToMedicationDto.count()).thenReturn(count);
        when(medicationService.toCreateDroneToMedication(eq(medication), any())).thenReturn(createDroneToMedicationDto);

        assertThrows(DroneOverweightException.class, () -> droneService.loadDrone(loadDroneDto));

        verify(droneToMedicationRepository, times(0)).save(any());
        verify(droneRepository, times(0)).save(drone);
    }
}
