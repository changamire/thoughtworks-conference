package info.gregbiegel.model;

import java.time.LocalTime;

/**
 * The MorningSession class is an extension of the Session base class and
 * represents a session starting at 9am and lasting for a maximum of 180
 * minutes.
 *
 */
public class MorningSession extends Session {

    private static final LocalTime START_TIME = LocalTime.of(9, 0);
    private final static int DURATION_IN_MINUTES = 180;

    public MorningSession() {
        super(START_TIME);
    }

    /*
     * @see info.gregbiegel.model.Session#getMinimumDurationInMinutes()
     */
    @Override
    public int getMinimumDurationInMinutes() {
        return DURATION_IN_MINUTES;
    }

    /*
     * @see info.gregbiegel.model.Session#getMaximumDurationInMinutes()
     */
    @Override
    public int getMaximumDurationInMinutes() {
        return DURATION_IN_MINUTES;
    }
}
