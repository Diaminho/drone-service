package test.drone.dto;

import java.util.List;

public record DroneLoadInformation(List<LoadMedicationDto> medications, Double currentWeight, Double maxWeight) {
}
