package test.drone.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Drone's load information to add
 * @param serialNumber of drone
 * @param loadedMedications list of medication to load the drone
 */
public record LoadDroneDto(
        @Size(min = 1, max = 100) String serialNumber,
        List<CreateLoadMedicationDto> loadedMedications
) {
}
