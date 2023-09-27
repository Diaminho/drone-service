package test.drone.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.drone.entity.Drone;
import test.drone.entity.DroneToMedication;
import test.drone.entity.Medication;
import test.drone.mapper.DroneToMedicationMapper;
import test.drone.mapper.MedicationMapper;
import test.drone.repository.DroneToMedicationRepository;
import test.drone.repository.MedicationRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicationServiceTest {
    @Mock
    private DroneToMedicationRepository droneToMedicationRepository;
    @Mock
    private DroneToMedicationMapper droneToMedicationMapper;
    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private MedicationMapper medicationMapper;

    @Mock
    private MinioService minioService;

    @InjectMocks
    private MedicationService medicationService;


    @Test
    void getMedicationByIdTest() {
        long id = 1L;

        var medication = mock(Medication.class);

        when(medicationRepository.findById(id)).thenReturn(Optional.of(medication));

        var found = medicationService.findById(id);
        assertEquals(medication, found);
    }

    @Test
    void findAllLoadedMedicationsForDroneEmpty() {
        var drone = mock(Drone.class);

        when(droneToMedicationRepository.findAllByDrone(drone)).thenReturn(Collections.emptyList());

        var found = medicationService.findAllLoadedMedicationsForDrone(drone);

        assertTrue(found.isEmpty());

        verify(droneToMedicationMapper, times(0)).toLoadedMedicationDto(any(), anyString());
    }

    @Test
    void findAllLoadedMedicationsForDrone() {
        var drone = mock(Drone.class);

        var medication = mock(Medication.class);
        var medicationImageUrl = "testUrl";
        when(medication.getImageUrl()).thenReturn(medicationImageUrl);

        var droneToMedication = mock(DroneToMedication.class);
        when(droneToMedication.getMedication()).thenReturn(medication);

        when(droneToMedicationRepository.findAllByDrone(drone)).thenReturn(Collections.singletonList(droneToMedication));

        when(minioService.downloadFileAsBase64(medicationImageUrl)).thenReturn("test");

        var found = medicationService.findAllLoadedMedicationsForDrone(drone);

        assertFalse(found.isEmpty());

        verify(droneToMedicationMapper, times(1)).toLoadedMedicationDto(any(), anyString());
    }

    @Test
    void toCreateDroneToMedicationTest() {
        short count = 1;
        var medication = mock(Medication.class);

        medicationService.toCreateDroneToMedication(medication, count);

        verify(medicationMapper, times(1)).toCreateDroneToMedication(any(), any());
    }
}
