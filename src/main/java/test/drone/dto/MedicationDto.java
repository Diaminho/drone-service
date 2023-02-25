package test.drone.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MedicationDto(
        @NotNull Long id,
        @Pattern(regexp = "^[\\w-]*$") String name,
        @Min(1) Double weight,
        @Pattern(regexp = "^[A-Z\\d_]*$") String code,
        String image
) {
}
