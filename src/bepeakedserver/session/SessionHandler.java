package bepeakedserver.session;

import java.util.Date;

/**
 * SessionHandler
 * @author Lasse
 * @version 14-01-2017
 */
public class SessionHandler implements Runnable
{
    private final int timeout;
    private final long sessionTimeout;
    private boolean running;

    public SessionHandler(int timeout, long sessionTimeout) {
        this.timeout = timeout;
        this.sessionTimeout = sessionTimeout;
        running = true;
    }
    
    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        System.out.println("Session clean-up thread started");
        Date date;
        long t;
        
        while(running) {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            date = new Date();
            t = date.getTime() - sessionTimeout;
            System.out.print("\n[" + date + "] CLEANING UP SESSIONS: limit=" + t);
            Sessions.removeSessions(t);
            System.out.println(", " + Sessions.sessionsToString() + " COMPLETE!");
        }
    }
}