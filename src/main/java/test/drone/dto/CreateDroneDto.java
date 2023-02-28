package test.drone.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import test.drone.entity.Model;
import test.drone.validation.UniqueDroneSerialNumber;

/**
 * Information for drone's creation
 * @param serialNumber of new
 * @param model of drone
 * @param weightLimit of drone
 * @param batteryCapacity of drone
 */
@Schema(description = "Information for Drone's registration")
public record CreateDroneDto(
        @Schema(description = "Drone's serial number", example = "1233A")
        @UniqueDroneSerialNumber @Size(min = 1, max = 100) String serialNumber,
        @Schema(description = "Drone's model", example = "Cruiserweight")
        @NotNull Model model,
        @Schema(description = "Drone's maximum carriable weight in grams", example = "250.5")
        @Min(0) @Max(500) Double weightLimit,
        @Schema(description = "Drone's current battery level in percents", example = "99")
        @Min(0) @Max(100) Short batteryCapacity
) {
}
