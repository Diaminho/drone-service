package test.drone.dto;

/**
 * Medication information to load drone
 * @param id of medication to load
 * @param count of medication to load
 */
public record CreateLoadMedicationDto(Long id, Short count) {
}
