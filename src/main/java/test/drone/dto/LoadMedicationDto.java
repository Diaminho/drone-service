package test.drone.dto;

import jakarta.validation.constraints.Pattern;

public record LoadMedicationDto(
        Long id,
        Short count,
        @Pattern(regexp = "^[\\w-]*$") String name,
        Double weight,
        @Pattern(regexp = "^[A-Z\\d_]*$") String code,
        String image
) {
}
