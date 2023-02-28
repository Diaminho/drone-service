package test.drone.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

/**
 * Information about loaded medication
 * @param id od medication
 * @param count of medication
 * @param name of medication
 * @param weight of medication
 * @param code of medication
 * @param image of medication
 */
@Schema(description = "Information about loaded medication")
public record LoadMedicationDto(
        @Schema(description = "Medication id", example = "1") Long id,
        @Schema(description = "Medication count", example = "2") Short count,
        @Schema(description = "Medication name", example = "testName") @Pattern(regexp = "^[\\w-]*$") String name,
        @Schema(description = "Medication's weight in grams for one piece", example = "1") Double weight,
        @Schema(description = "Medication's code", example = "saflq2") @Pattern(regexp = "^[A-Z\\d_]*$") String code,
        @Schema(description = "base64 string of medication's image") String image
) {
}
