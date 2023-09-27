package test.drone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for MinIO interaction
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomMinioException extends RuntimeException {

    public CustomMinioException(String message) {
        super(message);
    }
}
