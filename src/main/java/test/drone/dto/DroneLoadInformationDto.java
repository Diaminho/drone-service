package test.drone.dto;

import java.util.List;

/**
 * Information about drone's medication loading
 * @param medications List of loaded medications
 * @param currentWeight current medications weight
 * @param maxWeight max drone capacity weight
 */
public record DroneLoadInformationDto(List<LoadMedicationDto> medications, Double currentWeight, Double maxWeight) {
}
