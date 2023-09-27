package test.drone.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.drone.repository.DroneRepository;
import test.drone.validation.UniqueDroneSerialNumber;
import test.drone.validation.UniqueDroneSerialNumberValidator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UniqueDroneSerialNumberValidatorTest {
  @Mock
  private UniqueDroneSerialNumber uniqueDroneSerialNumber;

  @Mock
  private DroneRepository droneRepository;

  @Mock
  private ConstraintValidatorContext constraintValidatorContext;

  @InjectMocks
  private UniqueDroneSerialNumberValidator uniqueDroneSerialNumberValidator;

  @Test
  void serialNumberExists() {
    var droneSerialNumber = "1L";

    when(droneRepository.existsById(droneSerialNumber)).thenReturn(true);

    boolean result = uniqueDroneSerialNumberValidator.isValid(droneSerialNumber, constraintValidatorContext);
    assertFalse(result);
  }

  @Test
  void serialNumberValid() {
    var droneSerialNumber = "1L";

    when(droneRepository.existsById(droneSerialNumber)).thenReturn(false);

    boolean result = uniqueDroneSerialNumberValidator.isValid(droneSerialNumber, constraintValidatorContext);
    assertTrue(result);
  }

}