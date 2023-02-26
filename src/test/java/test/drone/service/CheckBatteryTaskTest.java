package test.drone.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import test.drone.entity.Drone;
import test.drone.repository.DroneRepository;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CheckBatteryTaskTest {
    @Mock
    private KafkaTemplate kafkaTemplate;

    @Mock
    private DroneRepository droneRepository;

    @InjectMocks
    private CheckBatteryTask checkBatteryTask;

    @Test
    public void sendEventTest() {
        var drone = new Drone();

        when(droneRepository.findAll()).thenReturn(Collections.singletonList(drone));

        assertDoesNotThrow(() -> checkBatteryTask.checkBatteryStatusForDrone());
    }
}
