package test.drone.service.kafka;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import test.drone.entity.Drone;
import test.drone.entity.event.BatteryCheckLevelEvent;
import test.drone.repository.DroneRepository;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckBatteryTaskTest {
    @Mock
    private KafkaTemplate<String, BatteryCheckLevelEvent> kafkaTemplate;

    @Mock
    private DroneRepository droneRepository;

    @InjectMocks
    private CheckBatteryTask checkBatteryTask;

    @Test
    void sendEventTest() {
        var drone = new Drone();

        when(droneRepository.findAll()).thenReturn(Collections.singletonList(drone));

        assertDoesNotThrow(checkBatteryTask::checkBatteryStatusForDrone);
        verify(kafkaTemplate, times(1)).send(nullable(String.class), any(BatteryCheckLevelEvent.class));
    }

    @Test
    void sendEventWithDisabledKafkaTest() {
        var checkBatteryTask = new CheckBatteryTask(droneRepository, null);

        var drone = new Drone();

        when(droneRepository.findAll()).thenReturn(Collections.singletonList(drone));

        assertDoesNotThrow(checkBatteryTask::checkBatteryStatusForDrone);
        verify(kafkaTemplate, never()).send(anyString(), any(BatteryCheckLevelEvent.class));
    }
}
