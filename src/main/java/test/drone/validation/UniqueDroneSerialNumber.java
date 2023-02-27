package test.drone.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to unique droneSerialNumber in creation dto
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = UniqueDroneSerialNumberValidator.class)
public @interface UniqueDroneSerialNumber {
    String message() default "Drone with this serialNumber exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
