package info.gregbiegel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import info.gregbiegel.model.Event;
import info.gregbiegel.service.binpack.*;
import info.gregbiegel.service.binpack.exception.InvalidBinPatternException;

public class FirstFitBinPackerTest {

    private IBinPackerFactory binPackerFactory = new FirstFitBinPackerFactory();
    private IBinPacker binPacker;

    @BeforeEach
    void init() {
        binPacker = binPackerFactory.createBinPacker();
    }

    @Test
    public void pack_validInput_success() throws Exception {
        //[2,5,4,7,1,3,8] with a bin size of 10
        //Will be packed into the following bins
        //[2,5,1], [4,3], [7], [8]
        Event event1 = new Event(1, "Event1", 2);
        Event event2 = new Event(2, "Event2", 5);
        Event event3 = new Event(3, "Event3", 4);
        Event event4 = new Event(4, "Event4", 7);
        Event event5 = new Event(5, "Event5", 1);
        Event event6 = new Event(6, "Event6", 3); 
        Event event7 = new Event(7, "Event7", 8);
        Event[] items = new Event[] {event1, event2, event3, event4, event5, event6, event7};
        int[] binPattern = new int[]{10};
        List<List<Event>> packedEvents = binPacker.pack(items, binPattern);
        assertEquals(4, packedEvents.size());
        assertEquals(packedEvents.get(0), Arrays.asList(event1, event2, event5));
        assertEquals(packedEvents.get(1), Arrays.asList(event3, event6));
        assertEquals(packedEvents.get(2), Arrays.asList(event4));
        assertEquals(packedEvents.get(3), Arrays.asList(event7));
    }

    @Test
    public void pack_emptyInput_success() throws Exception {
        Event[] items = new Event[0];
        int[] binPattern = new int[]{100,50};
        List<List<Event>> packedEvents = binPacker.pack(items, binPattern);
        assertEquals(0, packedEvents.size());
    }

    @Test
    public void pack_noBinPattern_exceptionThrown() {
        Event[] items = new Event[1];
        items[0] = new Event(1, "Event1", 2);
        int[] binPattern = new int[0];

        InvalidBinPatternException exception = assertThrows(InvalidBinPatternException.class, () -> {
            binPacker.pack(items, binPattern);
        });
        assertEquals("Invalid bin pattern provided", exception.getMessage());
    }
}
