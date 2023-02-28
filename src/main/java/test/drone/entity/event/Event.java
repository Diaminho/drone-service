package test.drone.entity.event;

import java.util.Date;
import java.util.UUID;

/**
 * Base Event information
 */
public abstract class Event {
    public final UUID id = UUID.randomUUID();
    public final Date created = new Date();
}