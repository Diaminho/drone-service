package test.drone.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Medication database entity dto
 * @param id of medication
 * @param name of medication
 * @param weight of medication
 * @param code of medication
 * @param image of medication
 */
public record MedicationDto(
        @NotNull Long id,
        @Pattern(regexp = "^[\\w-]*$") String name,
        @Min(1) Double weight,
        @Pattern(regexp = "^[A-Z\\d_]*$") String code,
        String image
) {
}
