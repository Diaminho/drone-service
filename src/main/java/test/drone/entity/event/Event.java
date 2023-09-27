package test.drone.entity.event;

import java.util.Date;

/**
 * Base Event information
 */
public abstract class Event {
    private final Date created = new Date();

    public Date getCreated() {
        return created;
    }
}