package info.gregbiegel.model;

import java.time.LocalTime;

/**
 * The ScheduledBreak abstract base class represents a break event that has been 
 * scheduled. A scheduled break event has been assigned a start time.
 *
 */
public abstract class ScheduledBreak extends ScheduledEvent {

    public ScheduledBreak(final int id, String title, int durationInMinutes, final LocalTime startTime) {
        super(id, title, durationInMinutes, startTime);
    }

    /**
     * Gets the earliest time at which the break may commence
     * 
     * @return the earliest time at which the break may commence.
     */
    public abstract LocalTime getEarliestStartTime();

    /**
     * Gets the latest time at which the break may commence
     * 
     * @return the latest time at which the break may commence
     */
    public abstract LocalTime getLatestStartTime();

    @Override
    public String toString() {
        return timeFormatter.format(startTime) + " " + getTitle();
    }

}
