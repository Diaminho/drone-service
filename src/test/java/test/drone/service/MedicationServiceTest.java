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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicationServiceTest {
    @Mock
    private DroneToMedicationRepository droneToMedicationRepository;
    @Mock
    private DroneToMedicationMapper droneToMedicationMapper;
    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private MedicationMapper medicationMapper;

    @InjectMocks
    private MedicationService medicationService;


    @Test
    public void getMedicationByIdTest() {
        long id = 1L;

        var medication = mock(Medication.class);

        when(medicationRepository.findById(eq(id))).thenReturn(Optional.of(medication));

        var found = medicationService.findById(id);
        assertEquals(medication, found);
    }

    @Test
    public void findAllLoadedMedicationsForDroneEmpty() {
        var drone = mock(Drone.class);

        when(droneToMedicationRepository.findAllByDrone(eq(drone))).thenReturn(Collections.emptyList());

        var found = medicationService.findAllLoadedMedicationsForDrone(drone);

        assertTrue(found.isEmpty());

        verify(droneToMedicationMapper, times(0)).toLoadedMedicationDto(any());
    }

    @Test
    public void findAllLoadedMedicationsForDrone() {
        var drone = mock(Drone.class);

        var droneToMedication = mock(DroneToMedication.class);

        when(droneToMedicationRepository.findAllByDrone(eq(drone))).thenReturn(Collections.singletonList(droneToMedication));

        var found = medicationService.findAllLoadedMedicationsForDrone(drone);

        assertFalse(found.isEmpty());

        verify(droneToMedicationMapper, times(1)).toLoadedMedicationDto(any());
    }

    @Test
    public void toCreateDroneToMedicationTest() {
        short count = 1;
        var medication = mock(Medication.class);

        medicationService.toCreateDroneToMedication(medication, count);

        verify(medicationMapper, times(1)).toCreateDroneToMedication(any(), any());
    }
}
