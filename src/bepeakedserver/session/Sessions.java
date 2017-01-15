package bepeakedserver.session;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Sessions
 * @author Lasse
 * @version 14-01-2017
 */
public class Sessions 
{
    private static final HashMap<String, Long> SESSIONS = new HashMap<>();
    
    public static void addSession(String id, long timestamp) {
        if(!SESSIONS.containsKey(id))
            SESSIONS.put(id, timestamp);
        else
            throw new IndexOutOfBoundsException("Session already exsist");
    }
    
    public static void updateSessionTimestamp(String id, long timestamp) {
        if(SESSIONS.containsKey(id))
            SESSIONS.replace(id, timestamp);
        else
            throw new NullPointerException("Session does not exsist");
    }
    
    public static boolean containsSession(String id) {
        return SESSIONS.containsKey(id);
    }
    
    public static void removeSessions(long timestampLimit) {
        List sessions = new ArrayList();
        Map.Entry<String, Long> session;
        sessions.addAll(SESSIONS.entrySet());
        
        for (Object obj : sessions) {
            session = (Map.Entry<String, Long>) obj;
            if(session.getValue() < timestampLimit)
                SESSIONS.remove(session.getKey());
        }
    }

    public static String sessionsToString() {
        return "Sessions{" + SESSIONS.keySet() + '}';
    }
}