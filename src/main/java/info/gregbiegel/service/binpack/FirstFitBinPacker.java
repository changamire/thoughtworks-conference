package info.gregbiegel.service.binpack;

import java.util.*;
import java.util.logging.Logger;

import info.gregbiegel.model.Event;
import info.gregbiegel.service.binpack.exception.InvalidBinPatternException;

/**
 * An implementation of the First Fit bin packing algorithm, using different bin
 * sizes (to represent different session lengths at a conference). 
 *
 */
public class FirstFitBinPacker implements IBinPacker {

    private static final Logger LOG = Logger.getLogger(FirstFitBinPacker.class.getName());

    /*
     * @see info.gregbiegel.service.binpack.IBinPacker#pack(info.gregbiegel.model.Event[], int[])
     */
    @Override
    public List<List<Event>> pack(Event[] items, int[] binPattern) throws InvalidBinPatternException {
        List<List<Event>> result = new ArrayList<List<Event>>();
        if (binPattern.length == 0)
            throw new InvalidBinPatternException();
        if (items.length == 0)
            return result;
        List<Bin> bins = new ArrayList<>();
        int binPatternSize = binPattern.length;
        bins.add(new Bin(binPattern[0]));
        int currentBin = 1;
        for (int i = 0; i < items.length; i++) {
            LOG.fine("Packing event of size " + items[i].getDurationInMinutes());
            boolean putItem = false;
            int binNo = 0;
            for (Bin bin : bins) {
                if (bin.doesEventFit(items[i])) {
                    bin.addEvent(items[i]);
                    LOG.fine("Item " + i + " is in bin " + binNo);
                    putItem = true;
                    break;
                }
                binNo++;
            }
            if (!putItem) {
                Bin newBin = new Bin(binPattern[currentBin % binPatternSize]);
                LOG.fine("Adding a bin of size " + binPattern[currentBin % binPatternSize]);
                newBin.addEvent(items[i]);
                bins.add(newBin);
                LOG.fine("Item " + i + " is in bin " + (bins.size()-1));
                currentBin++;
            }
        }
        for (Bin bin : bins)
            result.add(bin.getEvents());

        return result;
    }

    /**
     * The Bin class represents a bin which knows both its total size and its current
     * used size, as well as the collection of events currently in the bin.
     *
     */
    class Bin {
        private int binSize;
        private int usedSize = 0;

        private List<Event> events = new ArrayList<>();

        public Bin(int binSize) {
            super();
            this.binSize = binSize;
        }

        public void addEvent(final Event event) {
            events.add(event);
            usedSize += event.getDurationInMinutes();
        }

        public boolean doesEventFit(final Event e) {
            return (binSize - usedSize) >= e.getDurationInMinutes();
        }

        public List<Event> getEvents() {
            return events;
        }
    }
}
