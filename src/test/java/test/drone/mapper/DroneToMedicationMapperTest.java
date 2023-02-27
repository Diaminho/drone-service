package test.drone.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import test.drone.dto.CreateDroneToMedicationDto;
import test.drone.dto.LoadMedicationDto;
import test.drone.entity.Drone;
import test.drone.entity.DroneMedicationKey;
import test.drone.entity.DroneToMedication;
import test.drone.entity.Medication;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DroneToMedicationMapperTest {
    @InjectMocks
    private DroneToMedicationMapper droneToMedicationMapper = Mappers.getMapper(DroneToMedicationMapper.class);

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

        var entity = new DroneToMedication();
        entity.setMedication(medication);
        entity.setDrone(drone);
        entity.setCount((short) 1);

        var id = new DroneMedicationKey();
        id.setMedicationId(medication.getId());
        id.setDroneSerialNumber(drone.getSerialNumber());

        entity.setId(id);

        var expected = new LoadMedicationDto(medication.getId(), entity.getCount(), medication.getName(), medication.getWeight(), medication.getCode(), medication.getImage());

        var result = droneToMedicationMapper.toLoadedMedicationDto(entity);
        assertEquals(expected, result);
    }
}
