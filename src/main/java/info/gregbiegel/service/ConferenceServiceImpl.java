package info.gregbiegel.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Stream;

import info.gregbiegel.model.*;
import info.gregbiegel.service.binpack.*;
import info.gregbiegel.service.exception.*;

public class ConferenceServiceImpl implements IConferenceService {

    private static final Logger LOG = Logger.getLogger(ConferenceServiceImpl.class.getName());
    private static final String LIGHTNING_TALK_KEY = "lightning";
    private static final String TIME_SUFFIX = "min";
    private static final int LIGHTNING_TALK_TIME = 5;

    /**
     * A reference to a specific implementation of the bin packer factory
     * interface
     */
    private final IBinPackerFactory binPackerFactory;

    /**
     * A reference to a specific implementation of the bin packer interface
     */
    private final IBinPacker binPacker;

    /**
     * Instantiates a new object, using the default bin packing algorithm (next
     * fit)
     */
    public ConferenceServiceImpl() {
        binPackerFactory = new NextFitBinPackerFactory();
        binPacker = binPackerFactory.createBinPacker();
    }

    /**
     * Instantiates a new object using the selected bin packing algorithm
     */
    public ConferenceServiceImpl(final BinPackType binPackType) {
        LOG.fine("Instantiated with bin pack algorithm " + binPackType);
        switch (binPackType) {
        case NEXT_FIT:
            binPackerFactory = new NextFitBinPackerFactory();
            break;
        case FIRST_FIT:
            binPackerFactory = new FirstFitBinPackerFactory();
            break;
        default:
            binPackerFactory = new NextFitBinPackerFactory();
        }
        binPacker = binPackerFactory.createBinPacker();
    }

    /*
     * @see
     * info.gregbiegel.service.IConferenceService#readTalkDataFromFile(java.lang
     * .String)
     */
    @Override
    public Set<Event> readTalkDataFromFile(final String fileName) throws FileParserException {
        LOG.fine("Reading input file " + fileName);
        Set<Event> result = new HashSet<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(new Consumer<String>() {
                int id = 2; // Id 0 and 1 are reserved for break events
                public void accept(String line) {
                    LOG.fine("Read line " + line);
                    if (!isStringBlankOrNull(line))
                        result.add(parseTalkString(id++, line));
                }
            });
        } catch (IOException | EventParserException e) {
            throw new FileParserException("Error reading event data from file", e);
        }
        return result;
    }

    /*
     * @see
     * info.gregbiegel.service.IConferenceService#scheduleConference(java.util.
     * Set, java.util.List)
     */
    @Override
    public Conference scheduleConference(final Set<Event> events, final List<Session> sessions)
            throws ConferenceSchedulerException {
        if (events == null || events.size() == 0 || sessions == null || sessions.size() == 0)
            throw new ConferenceSchedulerException("No events or no sessions provided to scheduler");
        if (!areEventsSchedulable(events, sessions)) 
            throw new ConferenceSchedulerException("There is an event that is longer than the available sessions");

        Conference conference = new Conference();
        int[] sessionPattern = new int[sessions.size()];
        int sessionPatternIndex = 0;

        for (Iterator<Session> sessionIter = sessions.iterator(); sessionIter.hasNext();)
            sessionPattern[sessionPatternIndex++] = sessionIter.next().getMaximumDurationInMinutes();

        Event[] eventArray = events.toArray(new Event[events.size()]);
        List<List<Event>> bins = binPacker.pack(eventArray, sessionPattern);
        int sessionNo = 0;
        Track track = new Track(Arrays.asList(new MorningSession(), new AfternoonSession()));
        LOG.fine("Added new track to the conference " + track.toString());
        conference.addTrack(track);
        for (List<Event> bin : bins) {
            if (sessionNo == sessions.size()) { // Start a new track
                sessionNo = 0;
                track = new Track(Arrays.asList(new MorningSession(), new AfternoonSession()));
                conference.addTrack(track);
                LOG.fine("Added new track to the conference " + track.toString());
            }
            for (int currentEventIdx = 0; currentEventIdx < bin.size(); currentEventIdx++) {
                try {
                    track.getSession(sessionNo).scheduleEventInSession(bin.get(currentEventIdx),
                            isFinalTrackEvent(sessionNo, sessionPattern.length, bin.size(), currentEventIdx));
                } catch (ScheduleException e) {
                    throw new ConferenceSchedulerException(e);
                }
            }
            sessionNo++;
        }
        return conference;
    }

    /**
     * Parses a line from the input file, creating an event object representing
     * the event
     * 
     * @param id
     *            the id to be assigned to the created event object
     * @param talkString
     *            the input string to parse
     * @return an object representing an event that can be scheduled
     * @throws EventParserException
     *             if there is a problem parsing the input string
     */
    protected Event parseTalkString(final int id, final String talkString) throws EventParserException {
        LOG.fine("Parsing " + talkString);
        if (talkString.trim().length() < 4) {
            throw new EventParserException("Event description could not be parsed");
        }
        String title;
        int time;
        if (talkString.toLowerCase().endsWith(LIGHTNING_TALK_KEY)) {
            time = LIGHTNING_TALK_TIME;
            title = talkString.substring(0, talkString.toLowerCase().indexOf(LIGHTNING_TALK_KEY));
        } else {
            int i;
            for (i = 0; i < talkString.length(); i++)
                if (talkString.charAt(i) >= '0' && talkString.charAt(i) <= '9')
                    break;
            //Assume that there is 1 space between the talk title and the time string
            title = talkString.substring(0, i-1);
            try {
                //Assume the time of the event is always followed by the string 'min'
                time = Integer.parseInt(talkString.substring(i, talkString.length() - TIME_SUFFIX.length()));
            } catch (StringIndexOutOfBoundsException | NumberFormatException nfe) {
                LOG.severe("Error parsing length of event");
                throw new EventParserException("Could not parse length of event", nfe);
            }
        }
        return new Event(id, title, time);
    }

    /**
     * Determines if the event is the last of the track. The event is defined as
     * the final in the track if it is the last event in the last session of the
     * track.
     * 
     * @param sessionNo
     *            the session number within the track
     * @param noOfSessionsPerTrack
     *            the number of sessions per track
     * @param noOfEventsInSession
     *            number of events scheduled in the session
     * @param currentEventIdx
     *            the index of the current event within the session
     * @return true if index of the current event represents the final event of
     *         the track
     */
    protected boolean isFinalTrackEvent(final int sessionNo, final int noOfSessionsPerTrack,
            final int noOfEventsInSession, final int currentEventIdx) {
        return ((sessionNo == noOfSessionsPerTrack - 1) && currentEventIdx == (noOfEventsInSession - 1));
    }

    /**
     * Returns true if the input string is either null or a blank string.
     * 
     * @param input the input string
     * @return true if the input string is null or blank
     */
    protected boolean isStringBlankOrNull(final String input) {
        return input == null || input.trim().length() == 0;
    }
    
    /**
     * Returns true if there is no one event that is longer than the available session times, otherwise returns false
     * 
     * @param events the set of events to check
     * @param sessions the available sessions
     * @return true if no one event is longer than the available session times
     */
    protected boolean areEventsSchedulable(final Set<Event> events, final List<Session> sessions) {
        for (Event e : events) {
            boolean foundLargeEnoughSession = false;
            for (Session s : sessions) {
                if (s.getMaximumDurationInMinutes() >= e.getDurationInMinutes()) {
                    foundLargeEnoughSession = true;
                }
            }
            if (!foundLargeEnoughSession)
                return false;
        }
        return true;
    }
}
