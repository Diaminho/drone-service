package test.drone.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import test.drone.dto.*;
import test.drone.entity.Drone;
import test.drone.entity.Model;
import test.drone.entity.State;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DroneMapperTest {
    @InjectMocks
    private DroneMapper droneMapper = Mappers.getMapper(DroneMapper.class);

    @Test
    public void droneToDtoTest() {
        var drone = new Drone();

        var dto = new DroneDto(drone.getSerialNumber(), drone.getModel(), drone.getWeightLimit(), drone.getBatteryCapacity(), drone.getState());

        var result = droneMapper.droneToDto(drone);
        assertEquals(dto, result);
    }

    @Test
    public void droneToBatteryLevelDtoTest() {
        var drone = new Drone();
        drone.setBatteryCapacity((short) 99);

        var dto = new BatteryLevelDto(drone.getBatteryCapacity());

        var result = droneMapper.toBatteryLevelDto(drone);
        assertEquals(dto, result);
    }

    @Test
    public void fromCreateDtoTest() {
        var createDto = new CreateDroneDto("11W", Model.Cruiserweight, 44D, (short) 99);

        var drone = new Drone();
        drone.setState(State.IDLE);
        drone.setModel(createDto.model());
        drone.setBatteryCapacity(createDto.batteryCapacity());
        drone.setSerialNumber(createDto.serialNumber());
        drone.setWeightLimit(createDto.weightLimit());

        var result = droneMapper.fromCreateDto(createDto);
        assertEquals(drone, result);
    }

    @Test
    public void toDroneLoadInformationTest() {
        var drone = new Drone();
        drone.setState(State.IDLE);
        drone.setModel(Model.Cruiserweight);
        drone.setBatteryCapacity((short) 99);
        drone.setSerialNumber("11W");
        drone.setWeightLimit(44D);

        var loadMedicationDto = new LoadMedicationDto(1L, (short) 1, "testMed", 50.5D, "code", "-");
        var loadMedicationList = Collections.singletonList(loadMedicationDto);

        var droneLoadInformationDto = new DroneLoadInformationDto(loadMedicationList, 50.5D, drone.getWeightLimit());

        var result = droneMapper.toDroneLoadInformation(drone, loadMedicationList);
        assertEquals(droneLoadInformationDto, result);
    }
}
