package server;

import common.nio.ObjectWrite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.api.ISharedFiles;
import server.api.IStateServer;
import server.impl.SharedFiles;
import server.impl.StateServer;

import java.io.IOException;
import java.util.Collections;

public class MainServer {

    private static final Logger LOGGER = LogManager.getLogger(MainServer.class);
    private static final int DEFAULT_PORT = 8081;
    private static final String PATH_TO_CONFIG = "./src/main/resources/server.cfg";

    public static void main(String[] args) {
        // update connection with client ????
        ISharedFiles sharedFiles = new SharedFiles(Collections.emptyMap());

        sharedFiles.addListenerChangeFiles(state -> {
            try {
                LOGGER.debug(" change files ");
                ObjectWrite.writeObjectToFile(PATH_TO_CONFIG, state);
            } catch (IOException e) {
                LOGGER.error(" change ERROR not save:" + e);
            }
        });

        IStateServer stateServer = new StateServer(sharedFiles);

        MainLoopServer mainLoopServer = new MainLoopServer(DEFAULT_PORT, stateServer);
        try {
            mainLoopServer.start();
        } catch (IOException e) {
            LOGGER.error("error : " + e);
        }


    }
}
