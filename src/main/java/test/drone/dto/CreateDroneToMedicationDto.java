package test.drone.dto;

/**
 * Dto for Create link between Drone and Loaded Medication
 * @param medication Medication information
 * @param count Medication count
 */
public record CreateDroneToMedicationDto(MedicationDto medication, Short count) {
}
