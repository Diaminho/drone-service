package test.drone.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Drone's load information to add
 * @param serialNumber of drone
 * @param loadedMedications list of medication to load the drone
 */
@Schema(description = "Information about medications to be loaded in drone")
public record LoadDroneDto(
        @Schema(description = "Drone's serialNumber", example = "1232A")
        @Size(min = 1, max = 100) @Size(min = 1, max = 100) String serialNumber,
        @Schema(description = "List of medication information")
        @NotEmpty List<@Valid CreateLoadMedicationDto> loadedMedications
) {
}
