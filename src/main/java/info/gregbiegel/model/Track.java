package info.gregbiegel.model;

import java.util.Iterator;
import java.util.List;

/**
 * The Track class represents an individual track within a conference 
 *
 */
public class Track {

    /**
     * A list of sessions within the track
     */
    private List<Session> sessions;

    public Track(final List<Session> sessions) {
        this.sessions = sessions;
    }

    public Session getSession(int index) {
        return sessions.get(index);
    }

    public Iterator<Session> getSessions() {
        return sessions.iterator();
    }

    public void addSession(final Session session) {
        sessions.add(session);
    }

    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();
        for (Iterator<Session> sessionIter = getSessions(); sessionIter.hasNext(); ) {
            output.append(sessionIter.next().toString());
        }
        return output.toString();
    }

}
