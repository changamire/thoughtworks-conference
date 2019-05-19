package info.gregbiegel.model;

import java.time.LocalTime;

/**
 * The NetworkingBreak class represents a scheduled networking event. The event
 * is defined as not being able to start before 4pm, and not being able to start 
 * after 5pm, and is defined as being 60 minutes long.
 *
 */
public class NetworkingBreak extends ScheduledBreak {

    private static final String NETWORKING_EVENT_TITLE = "Networking Event";
    private static final int NETWORKING_DURATION_MINUTES = 60;
    public static final LocalTime EARLIEST_START_TIME = LocalTime.of(16, 0);
    public static final LocalTime LATEST_START_TIME = LocalTime.of(17, 0);

    public NetworkingBreak(final LocalTime startTime) {
        super(1, NETWORKING_EVENT_TITLE, NETWORKING_DURATION_MINUTES, startTime);
    }

    /*
     * @see info.gregbiegel.model.ScheduledBreak#getEarliestStartTime()
     */
    @Override
    public LocalTime getEarliestStartTime() {
        return EARLIEST_START_TIME;
    }

    /*
     * @see info.gregbiegel.model.ScheduledBreak#getLatestStartTime()
     */
    @Override
    public LocalTime getLatestStartTime() {
        return LATEST_START_TIME;
    }

}
