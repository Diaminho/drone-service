package test.drone.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import test.drone.dto.*;
import test.drone.service.DroneService;

import java.util.List;

/**
 * Controller for interaction with drones
 */
@RestController
@RequestMapping(value = "drones", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Tag(name = "Drones Controller", description = "Drone's API")
public class DroneController {
    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @Operation(description = "Register a new drone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created drone information",
                    content = @Content(schema = @Schema(implementation = DroneDto.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public DroneDto registerDrone(
            @Parameter(description = "Drone's information to register", required = true,
                    schema = @Schema(implementation = CreateDroneDto.class))
            @RequestBody @Valid CreateDroneDto information) {
        return droneService.registerDrone(information);
    }

    @Operation(description = "Receive a list of available to load drones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of available Drones",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DroneDto.class))))
    })
    @GetMapping("available")
    public List<DroneDto> getAvailableDrones() {
        return droneService.getAvailableForLoadingDrones();
    }

    @Operation(description = "Get drone's current battery level")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Drone's battery level information",
                    content = @Content(schema = @Schema(implementation = BatteryLevelDto.class)))
    })
    @GetMapping("{serialNumber}/battery")
    public BatteryLevelDto getBatteryLevel(@Parameter(description = "Drone's serial number") @PathVariable @Size(min = 1, max = 100) String serialNumber) {
        return droneService.getBatteryLevel(serialNumber);
    }

    @Operation(description = "Start loading drone with medication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Drone's information with changed state",
                    content = @Content(schema = @Schema(implementation = DroneDto.class)))
    })
    @PostMapping(value = "load", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DroneDto loadDrone(
            @Parameter(description = "Drone's load information", required = true,
                    schema = @Schema(implementation = LoadDroneDto.class))
            @RequestBody @Valid LoadDroneDto loadDroneDto) {
        return droneService.loadDrone(loadDroneDto);
    }

    @Operation(description = "Request an loading drone's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Drone's loading information",
                    content = @Content(schema = @Schema(implementation = DroneLoadInformationDto.class)))
    })
    @GetMapping("{serialNumber}/load")
    public DroneLoadInformationDto checkLoadingDrone(@Parameter(description = "Drone's serial number") @PathVariable @Size(min = 1, max = 100) String serialNumber) {
        return droneService.checkLoadingDrone(serialNumber);
    }
}
