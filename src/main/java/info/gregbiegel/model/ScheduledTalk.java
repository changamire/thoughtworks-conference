package info.gregbiegel.model;

import java.time.LocalTime;

/**
 * The ScheduledTalk class represents a presentation at the conference
 * that has been assigned a start time.
 *
 */
public class ScheduledTalk extends ScheduledEvent {

    public ScheduledTalk(int id, String title, int durationInMinutes, final LocalTime startTime) {
        super(id, title, durationInMinutes, startTime);
    }

    public ScheduledTalk(final Event event, final LocalTime startTime) {
        super(event.getId(), event.getTitle(), event.getDurationInMinutes(), startTime);
    }

}
