package info.gregbiegel.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * The ScheduledEvent abstract base class represents any scheduled event within a 
 * session. A scheduled event is an event that has been assigned a start time.
 *
 */
public abstract class ScheduledEvent extends Event implements Comparable<ScheduledEvent> {

    /**
     * Formatter used to format time when printing it to a string
     */
    protected final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mma");

    /**
     * The time this event is scheduled to start
     */
    protected final LocalTime startTime;

    public ScheduledEvent(final int id, String title, int durationInMinutes, final LocalTime startTime) {
        super(id, title, durationInMinutes);
        this.startTime = startTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    @Override
    public int compareTo(ScheduledEvent other) {
        return getStartTime().compareTo(other.getStartTime());
    }

    @Override
    public String toString() {
        return timeFormatter.format(startTime) + " " + getTitle() + " " + getDurationInMinutes() + "m";
    }

}