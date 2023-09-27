package test.drone.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import test.drone.dto.LoadMedicationDto;
import test.drone.entity.Drone;
import test.drone.entity.DroneMedicationKey;
import test.drone.entity.DroneToMedication;
import test.drone.entity.Medication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DroneToMedicationMapperTest {

    @InjectMocks
    private DroneToMedicationMapper droneToMedicationMapper;

    @Test
    void entityToDtoTest() {
        var drone = new Drone();
        drone.setSerialNumber("1L");
        drone.setWeightLimit(400D);

        var medication = new Medication();
        medication.setId(2L);
        medication.setWeight(100D);
        medication.setCode("code");
        var imageUrl = "imageUrl";
        medication.setImageUrl(imageUrl);

        var entity = new DroneToMedication();
        entity.setMedication(medication);
        entity.setDrone(drone);
        entity.setCount((short) 1);

        var id = new DroneMedicationKey();
        id.setMedicationId(medication.getId());
        id.setDroneSerialNumber(drone.getSerialNumber());

        entity.setId(id);

        var base64Image = "base64";
        var expected = new LoadMedicationDto(medication.getId(), entity.getCount(), medication.getName(), medication.getWeight(), medication.getCode(), base64Image);

        var result = droneToMedicationMapper.toLoadedMedicationDto(entity, base64Image);
        assertEquals(expected, result);
    }

    @Test
    void generateDroneToMedicationTest() {
        var droneId = "1A";
        var drone = mock(Drone.class);
        when(drone.getSerialNumber()).thenReturn(droneId);

        long medicationId = 1;
        var medication = mock(Medication.class);
        when(medication.getId()).thenReturn(medicationId);

        short count = 1;

        var expected = new DroneToMedication();
        expected.setDrone(drone);
        expected.setMedication(medication);
        expected.setCount(count);

        var id = new DroneMedicationKey();
        id.setMedicationId(medicationId);
        id.setDroneSerialNumber(droneId);

        expected.setId(id);

        var actual = droneToMedicationMapper.generateDroneToMedication(drone, medication, count);
        assertEquals(expected, actual);
    }
}
