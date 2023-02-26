package test.drone.dto;

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
public record LoadMedicationDto(
        Long id,
        Short count,
        @Pattern(regexp = "^[\\w-]*$") String name,
        Double weight,
        @Pattern(regexp = "^[A-Z\\d_]*$") String code,
        String image
) {
}
