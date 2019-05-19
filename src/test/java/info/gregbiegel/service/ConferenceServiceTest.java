package info.gregbiegel.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.*;

import info.gregbiegel.model.*;
import info.gregbiegel.service.exception.*;

public class ConferenceServiceTest {

    private ConferenceServiceImpl conferenceService;

    @BeforeEach
    void init() {
        conferenceService = new ConferenceServiceImpl();
    }

    @Test
    void readTalkDataFromFile_validFile_fileLoaded() throws Exception {
        Event e1 = new Event(2, "Writing Fast Tests Against Enterprise Rails", 60);
        Event e2 = new Event(20, "User Interface CSS in Rails Apps", 30);
        Set<Event> talks = conferenceService.readTalkDataFromFile("src/test/resources/conference_data.txt");
        assertEquals(19, talks.size());
        assertTrue(talks.contains(e1));
        assertTrue(talks.contains(e2));
    }

    @Test
    void readTalkDataFromFile_invalidFile_exceptionThrown() {
        FileParserException exception = assertThrows(FileParserException.class, () -> {
            conferenceService.readTalkDataFromFile("src/test/resources/conference_data_bad.txt");
        });
        assertEquals("Error reading event data from file", exception.getMessage());
    }

    @Test
    void parseTalkString_validString_parseSuccessfully() throws Exception {
        String validTalkString = "Communicating Over Distance 60min";
        Event parsedEvent = conferenceService.parseTalkString(1, validTalkString);
        assertEquals(1, parsedEvent.getId());
        assertEquals("Communicating Over Distance", parsedEvent.getTitle());
        assertEquals(60, parsedEvent.getDurationInMinutes());
    }

    @Test
    void parseTalkString_invalidString_exceptionThrown() {
        String invalidTalkString = "foo";
        EventParserException exception = assertThrows(EventParserException.class, () -> {
            conferenceService.parseTalkString(1, invalidTalkString);
        });
        assertEquals("Event description could not be parsed", exception.getMessage());
    }

    @Test
    void scheduleConference_validInput_scheduleSuccessful() throws Exception {
        //[60,70,100,90,40,60,10]
        //Session pattern is [180,240]
        //Result is [60,70], [100,90,40], [60,10]
        Set<Event> events = new HashSet<Event>();
        Event event1 = new Event(2, "Event1", 60);
        Event event2 = new Event(3, "Event2", 70);
        Event event3 = new Event(4, "Event3", 100);
        Event event4 = new Event(5, "Event4", 90);
        Event event5 = new Event(6, "Event5", 40);
        Event event6 = new Event(7, "Event6", 60); 
        Event event7 = new Event(8, "Event7", 10);
        events.add(event1);
        events.add(event2);
        events.add(event3);
        events.add(event4);
        events.add(event5);
        events.add(event6);
        events.add(event7);
        Conference conference = conferenceService.scheduleConference(events, 
                Arrays.asList(new MorningSession(), new AfternoonSession()));
        assertEquals(2, conference.getTracks().size());
        Track track1 = conference.getTracks().get(0);
        Session track1Morning = track1.getSession(0);
        assertEquals(2, track1Morning.getNoOfScheduledEvents());
        Session track1Afternoon = track1.getSession(1);
        assertEquals(5, track1Afternoon.getNoOfScheduledEvents());//Lunch and networking events added to afternoon track
        Track track2 = conference.getTracks().get(1);
        Session track2Morning = track2.getSession(0);
        assertEquals(2, track2Morning.getNoOfScheduledEvents());
    }

    @Test
    void scheduleConference_invalidSessionPattern_exceptionThrown() throws Exception {
        //[60,70,100,90,40,60,10]
        //Session pattern is [180,240]
        //Result is [60,70], [100,90,40], [60,10]
        Set<Event> events = new HashSet<Event>();
        Event event1 = new Event(2, "Event1", 60);
        Event event2 = new Event(3, "Event2", 70);
        Event event3 = new Event(4, "Event3", 100);
        Event event4 = new Event(5, "Event4", 90);
        Event event5 = new Event(6, "Event5", 40);
        Event event6 = new Event(7, "Event6", 60); 
        Event event7 = new Event(8, "Event7", 10);
        events.add(event1);
        events.add(event2);
        events.add(event3);
        events.add(event4);
        events.add(event5);
        events.add(event6);
        events.add(event7);
        ConferenceSchedulerException exception = assertThrows(ConferenceSchedulerException.class, () -> {
            conferenceService.scheduleConference(events, Arrays.asList());
        });
        assertEquals("No events or no sessions provided to scheduler", exception.getMessage());
    }

    @Test
    void scheduleConference_noEvents_exceptionThrown() throws Exception {
        //[60,70,100,90,40,60,10]
        //Session pattern is [180,240]
        //Result is [60,70], [100,90,40], [60,10]
        Set<Event> events = new HashSet<Event>();
        ConferenceSchedulerException exception = assertThrows(ConferenceSchedulerException.class, () -> {
            conferenceService.scheduleConference(events, Arrays.asList(new MorningSession(), new AfternoonSession()));
        });
        assertEquals("No events or no sessions provided to scheduler", exception.getMessage());
    }

    @Test
    void isFinalTrackEvent_finalTrackEvent_returnTrue() {
        int sessionNo = 2;
        int noOfSessionsPerTrack = 3;
        int noOfEventsInSession = 10;
        int currentEventIndex = 9;
        boolean result = conferenceService.isFinalTrackEvent(sessionNo, 
                noOfSessionsPerTrack, noOfEventsInSession, currentEventIndex);
        assertTrue(result);
    }

    @Test
    void isFinalTrackEvent_firstTrackEvent_returnFalse() {
        int sessionNo = 2;
        int noOfSessionsPerTrack = 3;
        int noOfEventsInSession = 10;
        int currentEventIndex = 0;
        boolean result = conferenceService.isFinalTrackEvent(sessionNo, 
                noOfSessionsPerTrack, noOfEventsInSession, currentEventIndex);
        assertFalse(result);
    }

    @Test
    void isFinalTrackEvent_firstSession_returnFalse() {
        int sessionNo = 0;
        int noOfSessionsPerTrack = 3;
        int noOfEventsInSession = 10;
        int currentEventIndex = 9;
        boolean result = conferenceService.isFinalTrackEvent(sessionNo, 
                noOfSessionsPerTrack, noOfEventsInSession, currentEventIndex);
        assertFalse(result);
    }

    @Test
    void isStringBlankOrNull_nullString_returnTrue() {
        assertTrue(conferenceService.isStringBlankOrNull(null));
    }

    @Test
    void isStringBlankOrNull_blankString_returnTrue() {
        assertTrue(conferenceService.isStringBlankOrNull("   "));
    }

    @Test
    void isStringBlankOrNull_notNullString_returnFalse() {
        assertFalse(conferenceService.isStringBlankOrNull("Not null"));
    }

    @Test
    void areEventsSchedulable_allSchedulable_returnTrue() {
        Set<Event> events = new HashSet<Event>();
        Event e1 = new Event(1, "Event1", 240);
        Event e2 = new Event(1, "Event2", 185);
        Event e3 = new Event(1, "Event3", 90);
        events.add(e1);
        events.add(e2);
        events.add(e3);
        assertTrue(conferenceService.areEventsSchedulable(events, Arrays.asList(new MorningSession(), 
                new AfternoonSession())));
    }

    @Test
    void areEventsSchedulable_notSchedulable_returnFalse() {
        Set<Event> events = new HashSet<Event>();
        Event e1 = new Event(1, "Event1", 60);
        Event e2 = new Event(2, "Event2", 180);
        Event e3 = new Event(3, "Event3", 241);
        events.add(e1);
        events.add(e2);
        events.add(e3);
        assertFalse(conferenceService.areEventsSchedulable(events, Arrays.asList(new MorningSession(), 
                new AfternoonSession())));
    }
}