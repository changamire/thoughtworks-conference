package info.gregbiegel.service.binpack;

import java.util.List;

import info.gregbiegel.model.Event;
import info.gregbiegel.service.binpack.exception.InvalidBinPatternException;

/**
 * Interface providing a single method that packs events into bins (representing
 * sessions within a conference track) of varying sizes.
 *
 */
public interface IBinPacker {

    /**
     * Packs the provided events into as few bins as possible. Events are packed
     * based on their duration in minutes, and bins are sized by their duration in minutes.
     * Bins are allocated based on the provided bin pattern which will be repeated
     * as new bins are created. For example, a bin pattern of [180,240] that results in
     * the events being packed into 5 bins, would result in the following five bins
     * being created
     * <ol>
     *  <li>Bin 1 - size 180</li>
     *  <li>Bin 2 - size 240</li>
     *  <li>Bin 3 - size 180</li>
     *  <li>Bin 4 - size 240</li>
     *  <li>Bin 5 - size 180</li>
     * </ol>
     * 
     * @param items an array of event objects to be packed into bins
     * @param binPattern the pattern of bin sizes to use
     * @return a list of bins packed with event objects
     * @throws InvalidBinPatternException if an invalid bin pattern is provided
     */
    public List<List<Event>> pack(Event[] items, int[] binPattern) throws InvalidBinPatternException;
}
