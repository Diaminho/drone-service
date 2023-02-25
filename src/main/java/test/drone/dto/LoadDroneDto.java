package test.drone.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

public record LoadDroneDto(
        @Size(min = 1, max = 100) String serialNumber,
        List<CreateLoadMedicationDto> loadedMedications
) {
}
