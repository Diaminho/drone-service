package test.drone.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Drone's battery current level
 * @param currentLevel current level
 */
@Schema(description = "Drone's batter level information")
public record BatteryLevelDto(@Schema(description = "Drone's current battery level in percents", example = "99") Short currentLevel) {
}
