package info.gregbiegel.model;

import java.time.LocalTime;

/**
 * TheAfternoonSession class is an extension of the Session base class and
 * represents a session starting at 1pm and lasting for a maximum of 240
 * minutes.
 *
 */
public class AfternoonSession extends Session {

    private static final LocalTime START_TIME = LocalTime.of(13, 0);
    private static final int MAXIMUM_DURATION_IN_MINUTES = 240;
    private static final int MINIMUM_DURATION_IN_MINUTES = 180;

    public AfternoonSession() {
        super(START_TIME);
    }

    /*
     * @see info.gregbiegel.model.Session#getMinimumDurationInMinutes()
     */
    @Override
    public int getMinimumDurationInMinutes() {
        return MINIMUM_DURATION_IN_MINUTES;
    }

    /*
     * @see info.gregbiegel.model.Session#getMaximumDurationInMinutes()
     */
    @Override
    public int getMaximumDurationInMinutes() {
        return MAXIMUM_DURATION_IN_MINUTES;
    }
}
