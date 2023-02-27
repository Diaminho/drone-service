package test.drone.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.drone.entity.Drone;
import test.drone.repository.DroneRepository;
import test.drone.validation.UniqueDroneSerialNumber;
import test.drone.validation.UniqueDroneSerialNumberValidator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UniqueDroneSerialNumberValidatorTest {
  @Mock
  private UniqueDroneSerialNumber uniqueDroneSerialNumber;

  @Mock
  private DroneRepository droneRepository;

  @Mock
  private ConstraintValidatorContext constraintValidatorContext;

  @InjectMocks
  private UniqueDroneSerialNumberValidator uniqueDroneSerialNumberValidator;

  @Test
  public void serialNumberExists() {
    var droneSerialNumber = "1L";

    var drone = mock(Drone.class);

    when(droneRepository.findById(eq(droneSerialNumber))).thenReturn(Optional.of(drone));

    boolean result = uniqueDroneSerialNumberValidator.isValid(droneSerialNumber, constraintValidatorContext);
    assertFalse(result);
  }

  @Test
  public void serialNumberValid() {
    var droneSerialNumber = "1L";

    when(droneRepository.findById(eq(droneSerialNumber))).thenReturn(Optional.empty());

    boolean result = uniqueDroneSerialNumberValidator.isValid(droneSerialNumber, constraintValidatorContext);
    assertTrue(result);
  }

}