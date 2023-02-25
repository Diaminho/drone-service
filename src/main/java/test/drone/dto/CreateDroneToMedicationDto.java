package test.drone.dto;

import test.drone.entity.Medication;

public record CreateDroneToMedicationDto(Medication medication, Short count) {
}
