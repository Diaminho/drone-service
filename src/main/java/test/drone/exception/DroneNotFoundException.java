package test.drone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DroneNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Drone is with serialNumber '%s' is not found";

    public DroneNotFoundException(String serialNumber) {
        super(String.format(DEFAULT_MESSAGE, serialNumber));
    }
}
