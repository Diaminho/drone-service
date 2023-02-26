package test.drone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception to indicate drone's overweight
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DroneOverweightException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Drone's weight capacity: '%f'. Provided weight: '%f'";

    public DroneOverweightException(Double capacity, Double current) {
        super(String.format(DEFAULT_MESSAGE, capacity, current));
    }
}
