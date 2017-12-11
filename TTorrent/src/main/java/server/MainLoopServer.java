package server;

import client.MainLoopClient;
import common.commands.handlers.server.IServerCommand;
import common.commands.handlers.server.IdRequestToServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import server.api.IServerLogic;
import server.state.IStateServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainLoopServer implements IServerLogic {

    private static final Logger LOGGER = LogManager.getLogger(MainLoopClient.class);
    private static final int countThread = 4;


    private final int portServer;
    private final IStateServer stateServer;


    private ExecutorService threadPool = Executors.newFixedThreadPool(countThread);

    public MainLoopServer(int portServer, @NotNull IStateServer stateServer) {
        this.portServer = portServer;
        this.stateServer = stateServer;
    }

    @Override
    public void start() throws IOException {
        LOGGER.debug("start server with top " + portServer);
        try (ServerSocket serverSocket = new ServerSocket(portServer)) {
            while (!serverSocket.isClosed()) {
                Socket socketClient = serverSocket.accept();
                threadPool.execute(() -> receiveClient(socketClient));
            }
        }
    }


    private void receiveClient(@NotNull Socket socketClient) {
        try (Socket client = socketClient;
             InputStream is = client.getInputStream()) {
            final int commandId = is.read();
            LOGGER.info("get commnad id = " + commandId);
            IServerCommand currentCommand = findCommand(commandId);
            if (currentCommand == null) {
                return;
            }
            currentCommand.run(client, stateServer);
        } catch (IOException e) {
            LOGGER.error("error: ", e);
        }
    }


    private IServerCommand findCommand(int id) {
        switch (id) {
            case IdRequestToServer.REQUEST_LIST:
            case IdRequestToServer.REQUEST_SOURCES:
            case IdRequestToServer.REQUEST_UPDATE:
            case IdRequestToServer.REQUEST_UPLOAD:
            default:
                LOGGER.warn("unknown command id " + id);
                return null;
        }
    }

    @Override
    public void shutdown() throws IOException {

    }
}
