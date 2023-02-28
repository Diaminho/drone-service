package test.drone.entity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Drone's state enum
 */
@Schema(description = "Drone's available states")
public enum State {
    IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING
}
