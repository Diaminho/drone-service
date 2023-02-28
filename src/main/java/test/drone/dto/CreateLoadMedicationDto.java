package test.drone.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

/**
 * Medication information to load drone
 * @param id of medication to load
 * @param count of medication to load
 */
@Schema(description = "Medication information to load a drone")
public record CreateLoadMedicationDto(
        @Schema(description = "Medication id", example = "1") @Min(1) Long id,
        @Schema(description = "Medication count to load", example = "1") @Min(1) Short count) {
}
