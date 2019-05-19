package info.gregbiegel.model;

import java.time.LocalTime;

/**
 * The LunchBreak class class represents a scheduled lunch break. The event
 * is defined as starting at 12pm and lasting for 60 minutes
 *
 */
public class LunchBreak extends ScheduledBreak {

    private static final int LUNCH_BREAK_ID = 0;
    private static final String LUNCH_BREAK_TITLE = "Lunch";
    private static final int LUNCH_DURATION_MINUTES = 60;
    private static final LocalTime LUNCH_START_TIME = LocalTime.of(12, 0);

    public LunchBreak() {
        super(LUNCH_BREAK_ID, LUNCH_BREAK_TITLE, LUNCH_DURATION_MINUTES, LUNCH_START_TIME);
    }

    @Override
    public LocalTime getEarliestStartTime() {
        return getStartTime();
    }

    @Override
    public LocalTime getLatestStartTime() {
        return getStartTime();
    }
}
