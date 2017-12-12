package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.api.IStateServer;
import server.impl.StateServer;

import java.io.IOException;

public class MainServer {

    private static final Logger LOGGER = LogManager.getLogger(MainServer.class);
    private static final int DEFAULT_PORT = 8081;

    public static void main(String[] args) {
        // update connection with client ????
        IStateServer stateServer = new StateServer();
        MainLoopServer mainLoopServer = new MainLoopServer(DEFAULT_PORT, stateServer);
        try {
            mainLoopServer.start();
        } catch (IOException e) {
            LOGGER.error("error : " + e);
        }


    }
}
