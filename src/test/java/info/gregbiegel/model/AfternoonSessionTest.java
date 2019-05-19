package info.gregbiegel.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import info.gregbiegel.service.exception.ScheduleException;

public class AfternoonSessionTest {

    private AfternoonSession afternoonSession;

    @BeforeEach
    void init() {
        afternoonSession = new AfternoonSession();
    }

    @Test
    void scheduleEventInSession_validInput_success() throws Exception {
        Event event1 = new Event(2, "Event1", 60);
        afternoonSession.scheduleEventInSession(event1, false);
        assertEquals(1, afternoonSession.getNoOfScheduledEvents());
    }

    @Test
    void scheduleEventInSession_validInput_throwsException() {
        Event event1 = new Event(2, "Event1", afternoonSession.getMaximumDurationInMinutes() + 1);
        ScheduleException exception = assertThrows(ScheduleException.class, () -> {
            afternoonSession.scheduleEventInSession(event1, false);
        });
        assertEquals("Insufficient remaining time in session to add event", exception.getMessage());
    }

    @Test 
    void isEnoughTimeForEvent_validInput_returnTrue() {
        LocalTime eventStart = LocalTime.of(15, 0);
        boolean result = afternoonSession.isEnoughTimeForEvent(eventStart, 120);
        assertTrue(result);
    }

    @Test 
    void isEnoughTimeForEvent_validInput_returnFalse() {
        LocalTime eventStart = LocalTime.of(15, 30);
        boolean result = afternoonSession.isEnoughTimeForEvent(eventStart, 91);
        assertFalse(result);
    }
}
