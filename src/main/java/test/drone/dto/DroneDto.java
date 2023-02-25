package test.drone.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import test.drone.entity.Model;
import test.drone.entity.State;

import java.util.List;

public record DroneDto(
        @Size(min = 1, max = 100) String serialNumber,
        @NotNull Model model,
        @Min(0) @Max(500) Double weightLimit,
        @Min(0) @Max(100) Short batteryCapacity,
        @NotNull State state,
        List<MedicationDto> loadedMedications
) {
}
