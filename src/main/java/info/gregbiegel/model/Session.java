package info.gregbiegel.model;

import java.time.LocalTime;
import java.util.*;

import info.gregbiegel.service.exception.ScheduleException;

/**
 * The Session class is the abstract base class for different
 * types of session within a track. Currently implemented sessions
 * include a morning session, an afternoon session, a lunch session
 * and a networking session.
 *
 */
public abstract class Session {

    /**
     * A set of events that have been scheduled within the session
     */
    private final SortedSet<ScheduledEvent> scheduledEvents = new TreeSet<ScheduledEvent>();
    /**
     * The time at which the session is scheduled to start
     */
    private final LocalTime startTime;

    public Session(final LocalTime startTime) {
        this.startTime = startTime;
    }

    public Iterator<ScheduledEvent> getScheduledEvents() {
        return scheduledEvents.iterator();
    }

    public int getNoOfScheduledEvents() {
        return scheduledEvents.size();
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Schedules an event within a session by setting the events start time
     * to be the time of the last scheduled event within the session, plus the 
     * duration of the last scheduled event within the session. If the event to be 
     * scheduled is the last event of the day, additional events for lunch and
     * networking are added to the session.
     * 
     * @param event the event to be scheduled within the session
     * @param isFinalEvent flag indicating whether this is the last event of the day
     */
    public void scheduleEventInSession(final Event event, final boolean isFinalEvent) throws ScheduleException {
        LocalTime eventStartTime;
        if (scheduledEvents.size() > 0) {
            ScheduledEvent lastEvent = scheduledEvents.last();
            eventStartTime = lastEvent.getStartTime().plusMinutes(lastEvent.getDurationInMinutes());
        } else {
            eventStartTime = getStartTime();
        }

        if (isEnoughTimeForEvent(eventStartTime, event.getDurationInMinutes())) {
            scheduledEvents.add(new ScheduledTalk(event, eventStartTime));
        } else {
            throw new ScheduleException("Insufficient remaining time in session to add event");
        }

        //Add the break events only once all talks have been scheduled 
        //since the start time of the break is dependent on the track schedule
        if (isFinalEvent) {
            LocalTime lastEventEndTime = scheduledEvents.last().getStartTime()
                    .plusMinutes(scheduledEvents.last().getDurationInMinutes());
            if (lastEventEndTime.isBefore(NetworkingBreak.EARLIEST_START_TIME)) {
                scheduledEvents.add(new NetworkingBreak(NetworkingBreak.EARLIEST_START_TIME));
            } else {
                scheduledEvents.add(new NetworkingBreak(NetworkingBreak.LATEST_START_TIME));
            }
            scheduledEvents.add(new LunchBreak());
        }
    }

    /**
     * Returns true if there is enough time remaining in the session for the event.
     * 
     * @param eventStartTime the time the event is scheduled to start
     * @param eventDuration the duration of the event
     * @return true if there is enough time remaining for the event, otherwise false
     */
    protected boolean isEnoughTimeForEvent(final LocalTime eventStartTime, final int eventDuration) {
        return (eventStartTime.plusMinutes(eventDuration).compareTo(
                startTime.plusMinutes(getMaximumDurationInMinutes())) <= 0);
    }

    /**
     * Gets the maximum duration of the session in minutes 
     * 
     * @return the maximum time the session can run in minutes
     */
    public abstract int getMaximumDurationInMinutes();

    /**
     * Gets the minimum duration of the session in minutes 
     * 
     * @return the minimum time the session can run in minutes
     */
    public abstract int getMinimumDurationInMinutes();

    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();
        for (Iterator<ScheduledEvent> eventIter = getScheduledEvents(); eventIter.hasNext(); ) {
            Event event = eventIter.next();
            output.append(event + "\n");
        }
        return output.toString();
    }
}
