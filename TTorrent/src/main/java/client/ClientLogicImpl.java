package client;

import client.api.IClientLogic;
import client.state.api.IState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;

public class ClientLogicImpl implements IClientLogic {

    private static final Logger LOGGER = LogManager.getLogger(ClientLogicImpl
            .class);

    private final int portClient;
    private final IState stateClient;
    private ServerSocket serverSocket;

    public ClientLogicImpl(int portClient, IState stateClient) {
        this.portClient = portClient;
        this.stateClient = stateClient;
    }

    @Override
    public void shutdown() throws IOException {

    }

    @Override
    public void start() {

    }
}
