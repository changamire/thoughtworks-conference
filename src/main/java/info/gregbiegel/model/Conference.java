package info.gregbiegel.model;

import java.util.*;

/**
 * The Conference class represents a scheduled conference event.
 *
 */
public class Conference {

    /**
     * The list of tracks associated with the conference
     */
    private final List<Track> tracks;

    public Conference() {
        this.tracks = new ArrayList<Track>();
    }

    /**
     * Adds a track to the conference.
     * 
     * @param track reference to the track to add
     */
    public void addTrack(final Track track) {
        tracks.add(track);
    }

    /**
     * Gets a reference to the conference tracks
     * 
     * @return a reference to the list of tracks within the conference
     */
    public List<Track> getTracks() {
        return tracks;
    }

    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();
        output.append("Conference\n");
        output.append("----------\n");
        int trackNo = 1;
        for (Iterator<Track> trackIter = tracks.iterator(); trackIter.hasNext();) {
            Track track = trackIter.next();
            output.append("Track " + trackNo++ + ":\n");
            output.append(track.toString());
            output.append("\n");
        }
        return output.toString();
    }
}
