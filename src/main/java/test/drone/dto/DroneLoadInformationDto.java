package test.drone.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Information about drone's medication loading
 * @param medications List of loaded medications
 * @param currentWeight current medications weight
 * @param maxWeight max drone capacity weight
 */
@Schema(description = "Information about drone's medication loading")
public record DroneLoadInformationDto(
        @Schema(description = "List of loaded medications") List<LoadMedicationDto> medications,
        @Schema(description = "current loaded weight in grams to drone") Double currentWeight,
        @Schema(description = "max drone's load weight in grams") Double maxWeight) {
}
