package test.drone.entity.event;

import java.time.Instant;
import java.util.UUID;

public abstract class Event {
    public final UUID id = UUID.randomUUID();
    public final Instant created = Instant.now();
}