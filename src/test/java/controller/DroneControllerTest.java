package controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import test.drone.controller.DroneController;

import test.drone.dto.*;
import test.drone.entity.Model;
import test.drone.entity.State;
import test.drone.service.DroneService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DroneControllerTest {
    @Mock
    private DroneService droneService;

    @InjectMocks
    private DroneController droneController;

    @Test
    public void createDroneTest() {
        var request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        short battery = 95;
        var createDroneDto = new CreateDroneDto("12", Model.Cruiserweight, 55D, battery);

        var droneDto = new DroneDto(createDroneDto.serialNumber(), createDroneDto.model(), createDroneDto.weightLimit(), createDroneDto.batteryCapacity(), State.IDLE);

        when(droneService.registerDrone(eq(createDroneDto))).thenReturn(droneDto);
        var response = droneController.registerDrone(createDroneDto);

        assertEquals(droneDto, response);
    }

    @Test
    public void getAvailableDronesForLoading() {
        var request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        short battery = 95;
        var droneDto = new DroneDto("12", Model.Cruiserweight, 55D, battery, State.IDLE);

        var foundDrones = Collections.singletonList(droneDto);
        when(droneService.getAvailableForLoadingDrones()).thenReturn(foundDrones);
        var response = droneController.getAvailableDrones();

        assertEquals(foundDrones, response);
    }

    @Test
    public void getBatteryTest() {
        var request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        short battery = 95;

        var droneDto = new DroneDto("12", Model.Cruiserweight, 55D, battery, State.IDLE);

        var batteryLevelDto = new BatteryLevelDto(droneDto.batteryCapacity());
        when(droneService.getBatteryLevel(droneDto.serialNumber())).thenReturn(batteryLevelDto);
        var response = droneController.getBatteryLevel(droneDto.serialNumber());

        assertEquals(batteryLevelDto, response);
    }

    @Test
    public void checkLoadingDroneTest() {
        var request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        short battery = 95;

        var droneDto = new DroneDto("12", Model.Cruiserweight, 55D, battery, State.IDLE);

        var loadMedicationDto = new LoadMedicationDto(1L, (short) 1, "name", 221d, "code", "image");

        var droneLoadInformationDto = new DroneLoadInformationDto(Collections.singletonList(loadMedicationDto), 221d, 444d);
        when(droneService.checkLoadingDrone(droneDto.serialNumber())).thenReturn(droneLoadInformationDto);
        var response = droneController.checkLoadingDrone(droneDto.serialNumber());

        assertEquals(droneLoadInformationDto, response);
    }

    @Test
    public void loadDroneTest() {
        var request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        var createLoad = new CreateLoadMedicationDto(1L, (short) 1);
        var loadDroneDto = new LoadDroneDto("1L", Collections.singletonList(createLoad));

        var droneDto = new DroneDto("1", Model.Cruiserweight, 2d, (short) 2, State.LOADING);
        when(droneService.loadDrone(eq(loadDroneDto))).thenReturn(droneDto);
        var response = droneController.loadDrone(loadDroneDto);

        assertEquals(droneDto, response);
    }

}
