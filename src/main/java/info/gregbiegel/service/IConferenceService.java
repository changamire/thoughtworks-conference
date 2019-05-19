package info.gregbiegel.service;

import java.util.List;
import java.util.Set;

import info.gregbiegel.model.*;
import info.gregbiegel.service.exception.ConferenceSchedulerException;
import info.gregbiegel.service.exception.FileParserException;

/**
 * The IConferenceService interface exposes a service interface that provides operations
 * to parse an input file representing conference talks, and schedules those talks based
 * on defined conference sessions.
 *
 */
public interface IConferenceService {

    /**
     * Reads a file containing records that represent talks at a conference. The input data
     * must follow the format [Talk title][Duration] where [Duration] is either:
     * <ol>
     *  <li>The string <b>lightning</b> in which case the talk lasts 5 minutes</li>
     *  <li>The length of the talk in minutes, followed by the string min</li>
     * </ol>
     * 
     * If there is a single line in the input file that cannot be parsed, parsing and processing
     * of the whole input file is abandoned so that an incomplete conference is not scheduled.
     * 
     * @param fileName the name of the file to read from the filesystem
     * @return a set of event objects that represent the records in the input file
     * @throws FileParserException if there is a problem reading the input file
     */
    public Set<Event> readTalkDataFromFile(final String fileName) throws FileParserException;

    /**
     * Schedules a set of events at a conference into a group of sessions across one or more tracks.
     * Scheduling is performed using the bin packing algorithm defined at instantiation of an implementation
     * of this interface.
     * 
     * @param events the set of events to be scheduled
     * @param sessions a list of objects that represent the available 
     *  types of session. e.g {MorningSession, AfternoonSession}
     * @return an object representing a conference with all the events scheduled into tracks and sessions
     * @throws ConferenceSchedulerException if there is a problem scheduling the conference
     */
    public Conference scheduleConference(final Set<Event> events, 
            final List<Session> sessions) throws ConferenceSchedulerException;
}
