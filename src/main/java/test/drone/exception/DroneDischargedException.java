package test.drone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DroneDischargedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Drone is discharged";

    public DroneDischargedException() {
        super(DEFAULT_MESSAGE);
    }
}
