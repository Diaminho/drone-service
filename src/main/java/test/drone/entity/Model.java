package test.drone.entity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Drone's model enum
 */
@Schema(description = "Drone's model names")
public enum Model {
    Lightweight, Middleweight, Cruiserweight, Heavyweight
}
