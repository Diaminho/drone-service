package test.drone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for Drone that can't be loaded
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DroneBusyException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Drone is busy right now";

    public DroneBusyException() {
        super(DEFAULT_MESSAGE);
    }
}
