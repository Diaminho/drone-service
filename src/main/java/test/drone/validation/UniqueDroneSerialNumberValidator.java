package test.drone.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import test.drone.repository.DroneRepository;

/**
 * Validator to check that droneSerialNumber is not stored
 */
@Component
@RequiredArgsConstructor
public class UniqueDroneSerialNumberValidator implements ConstraintValidator<UniqueDroneSerialNumber, String> {
    private final DroneRepository droneRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        var found = droneRepository.findById(value).orElse(null);
        return found == null;
    }
}
