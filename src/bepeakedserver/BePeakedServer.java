package bepeakedserver;

import SocketServer.ClientServiceThreadJSON;
import SocketServer.Debug;
import SocketServer.SocketServer;
import bepeakedserver.backend.BackendData;
import bepeakedserver.backend.ServerMultiplexer;
import bepeakedserver.session.SessionHandler;

/**
 * BePeakedServer
 * @author Lasse
 * @version 11-01-2017
 */
public class BePeakedServer 
{
    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer(BackendData.PORT, (Class) ClientServiceThreadJSON.class, (Class) ServerMultiplexer.class, Debug.DEBUG);
        Thread server = new Thread(socketServer);
        server.start();
        
        SessionHandler sessionHandler = new SessionHandler(60000, 900000);
        Thread sessionThread = new Thread(sessionHandler);
        sessionThread.setDaemon(true);
        sessionThread.start();
    }
}