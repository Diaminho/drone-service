package test.drone.dto;

import test.drone.entity.Medication;

/**
 * Dto for Create link between Drone and Loaded Medication
 * @param medication Medication information
 * @param count Medication count
 */
public record CreateDroneToMedicationDto(Medication medication, Short count) {
}
