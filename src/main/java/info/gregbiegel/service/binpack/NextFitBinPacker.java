package info.gregbiegel.service.binpack;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import info.gregbiegel.model.Event;
import info.gregbiegel.service.binpack.exception.InvalidBinPatternException;

/**
 * An implementation of the Next Fit bin packing algorithm using different bin
 * sizes (to represent different session lengths at a conference). 
 *
 */
public class NextFitBinPacker implements IBinPacker {

    private static final Logger LOG = Logger.getLogger(NextFitBinPacker.class.getName());

    /*
     * @see
     * info.gregbiegel.service.binpack.IBinPacker#pack(info.gregbiegel.model.
     * Event[], int[])
     */
    @Override
    public List<List<Event>> pack(Event[] items, int[] binPattern) throws InvalidBinPatternException {
        List<List<Event>> result = new ArrayList<>();
        if (binPattern.length == 0)
            throw new InvalidBinPatternException();
        if (items.length == 0)
            return result;
        int binPatternSize = binPattern.length;
        List<Event> bin = new ArrayList<>();
        result.add(bin);
        int binSize = binPattern[0];
        LOG.fine("Bin size " + binSize);
        int currentBin = 1;
        for (int i = 0; i < items.length; i++) {
            LOG.fine("Packing event of size " + items[i].getDurationInMinutes());
            if (binSize - items[i].getDurationInMinutes() >= 0) {
                binSize -= items[i].getDurationInMinutes();
                bin.add(items[i]);
                LOG.fine("Item " + i + " is in bin " + currentBin);
                continue;
            } else {
                bin = new ArrayList<>();
                result.add(bin);
                binSize = binPattern[currentBin % binPatternSize];
                LOG.fine("Adding a bin of size " + binSize);
                currentBin++;
                i--;
            }
        }
        return result;
    }
}