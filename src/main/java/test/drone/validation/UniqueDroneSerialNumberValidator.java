package test.drone.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import test.drone.repository.DroneRepository;

/**
 * Validator to check that droneSerialNumber is not stored
 */
@Component
public class UniqueDroneSerialNumberValidator implements ConstraintValidator<UniqueDroneSerialNumber, String> {
    private final DroneRepository droneRepository;

    public UniqueDroneSerialNumberValidator(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !droneRepository.existsById(value);
    }
}
