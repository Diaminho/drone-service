package test.drone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Not found drone exception
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MedicationNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Medication with id '%d' is not found";

    public MedicationNotFoundException(Long id) {
        super(String.format(DEFAULT_MESSAGE, id));
    }
}
