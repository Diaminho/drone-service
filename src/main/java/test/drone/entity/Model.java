package test.drone.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Drone's model enum
 */
@Schema(description = "Drone's model names")
public enum Model {
    @JsonProperty("Lightweight")
    LIGHTWEIGHT,
    @JsonProperty("Middleweight")
    MIDDLEWEIGHT,
    @JsonProperty("Cruiserweight")
    CRUISERWEIGHT,
    @JsonProperty("Heavyweight")
    HEAVYWEIGHT
}
