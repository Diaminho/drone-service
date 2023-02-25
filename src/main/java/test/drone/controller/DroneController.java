package test.drone.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import test.drone.dto.*;
import test.drone.service.DroneService;

import java.util.List;

@RestController
@RequestMapping("drones")
@Slf4j
@RequiredArgsConstructor
@Validated
public class DroneController {
    private final DroneService droneService;

    @PostMapping
    public DroneDto registerDrone(@Valid CreateDroneDto information) {
        return droneService.registerDrone(information);
    }

    @GetMapping("available")
    public List<DroneDto> getAvailableDrones() {
        return droneService.getAvailableDrones();
    }

    @GetMapping("{serialNumber}/battery")
    public BatteryLevelDto getBatteryLevel(@PathVariable @Size(min = 1, max = 100) String serialNumber) {
        return droneService.getBatteryLevel(serialNumber);
    }

    @PostMapping("load")
    public DroneDto loadDrone(@RequestBody @Valid LoadDroneDto loadDroneDto) {
        return droneService.loadDrone(loadDroneDto);
    }

    @GetMapping("{serialNumber}/load")
    public List<LoadedMedicationDto> checkLoadingDrone(@PathVariable @Size(min = 1, max = 100) String serialNumber) {
        return droneService.checkLoadingDrone(serialNumber);
    }
}
