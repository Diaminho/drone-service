package test.drone.dto;

import jakarta.validation.constraints.Pattern;

public record LoadedMedicationDto(
        Long id,
        Short count,
        @Pattern(regexp = "^[\\w-]*$") String name,
        Long weight,
        @Pattern(regexp = "^[A-Z\\d_]*$") String code,
        String image
) {
}
