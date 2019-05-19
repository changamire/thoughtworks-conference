package info.gregbiegel;

import java.util.*;

import info.gregbiegel.model.*;
import info.gregbiegel.service.*;
import info.gregbiegel.service.binpack.BinPackType;
import info.gregbiegel.service.exception.ConferenceSchedulerException;
import info.gregbiegel.service.exception.FileParserException;

/**
 * The Main class provides an entry point to the application.
 *
 */
public class Main {

    private IConferenceService conferenceService;

    public Main() {
    }

    public static void main(String[] args) {
        Main main = new Main();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter the input filename: ");
        String filename = scanner.nextLine();
        System.out.print("Select scheduling method\n ");
        System.out.print("1. Next fit\n ");
        System.out.print("2. First fit\n ");
        System.out.print("Press <Enter> for default method ");
        String method = scanner.nextLine();
        BinPackType binPackType = BinPackType.NEXT_FIT;
        try {
            binPackType = BinPackType.getForId(Integer.parseInt(method));
        } catch (NumberFormatException nfe) {
            System.out.println("Defaulting to next fit.\n");
        }
        scanner.close();
        main.loadAndScheduleConference(filename, binPackType);
    }

    /**
     * Loads conference events from an input file and schedules them using a specific
     * implementation of the bin packing algorithm to assign talks to sessions. Once
     * scheduling is complete, the details of the conference program are output to standard out.
     * 
     * @param fileName the full path and name of the input file
     * @param binPackType the type of bin packing algorithm to use in the scheduling
     */
    public void loadAndScheduleConference(final String fileName, final BinPackType binPackType) {
        conferenceService = new ConferenceServiceImpl(binPackType);
        try {
            Conference conference = conferenceService.scheduleConference(
                    conferenceService.readTalkDataFromFile(fileName), 
                    Arrays.asList(new MorningSession(), new AfternoonSession()));
            System.out.println(conference);
        } catch (FileParserException | ConferenceSchedulerException e) {
            System.err.println("Failed to read and parse file: " + e.getMessage());
            //e.printStackTrace();
        }
    }

}
