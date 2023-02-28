package test.drone.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.drone.dto.CreateDroneToMedicationDto;
import test.drone.dto.LoadMedicationDto;
import test.drone.entity.Drone;
import test.drone.entity.DroneMedicationKey;
import test.drone.entity.DroneToMedication;
import test.drone.entity.Medication;
import test.drone.service.MinioService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DroneToMedicationMapperTest {
    @Mock
    private MinioService minioService;

    @InjectMocks
    private DroneToMedicationMapper droneToMedicationMapper;

    @Test
    public void createDroneToMedicationLinkTest() {
        var drone = new Drone();
        drone.setSerialNumber("1L");
        drone.setWeightLimit(400D);

        var medication = new Medication();
        medication.setId(2L);
        medication.setWeight(100D);

        var createDroneToMedicationDto = new CreateDroneToMedicationDto(medication, (short) 1);

        var expected = new DroneToMedication();
        expected.setMedication(medication);
        expected.setDrone(drone);
        expected.setCount(createDroneToMedicationDto.count());

        var id = new DroneMedicationKey();
        id.setMedicationId(medication.getId());
        id.setDroneSerialNumber(drone.getSerialNumber());

        expected.setId(id);

        var result = droneToMedicationMapper.toDroneToMediation(drone, createDroneToMedicationDto);
        assertEquals(expected, result);
    }

    @Test
    public void entityToDtoTest() {
        var drone = new Drone();
        drone.setSerialNumber("1L");
        drone.setWeightLimit(400D);

        var medication = new Medication();
        medication.setId(2L);
        medication.setWeight(100D);
        medication.setCode("code");
        medication.setImage("image");

        var base64Image = "base64";
        when(minioService.downloadFile(eq(medication.getImage()))).thenReturn(base64Image);

        var entity = new DroneToMedication();
        entity.setMedication(medication);
        entity.setDrone(drone);
        entity.setCount((short) 1);

        var id = new DroneMedicationKey();
        id.setMedicationId(medication.getId());
        id.setDroneSerialNumber(drone.getSerialNumber());

        entity.setId(id);

        var expected = new LoadMedicationDto(medication.getId(), entity.getCount(), medication.getName(), medication.getWeight(), medication.getCode(), base64Image);

        var result = droneToMedicationMapper.toLoadedMedicationDto(entity);
        assertEquals(expected, result);
    }
}
