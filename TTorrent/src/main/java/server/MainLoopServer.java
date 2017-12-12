package server;

import client.MainLoopClient;
import common.commands.handlers.server.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import server.api.IServerLogic;
import server.api.IStateServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainLoopServer implements IServerLogic {

    private static final Logger LOGGER = LogManager.getLogger(MainLoopClient.class);
    private static final int countThread = 4;
    private static final Map<Integer, IServerCommand> idToCommand = new HashMap<>();


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


    @Nullable
    private IServerCommand findCommand(int id) {
        if (idToCommand.containsKey(id)) {
            return idToCommand.get(id);
        }
        return null;
    }

    private void intiMapCommand() {
        idToCommand.put(IdRequestToServer.REQUEST_LIST, new ListCommand());
        idToCommand.put(IdRequestToServer.REQUEST_SOURCES, new SourcesCommand());
        idToCommand.put(IdRequestToServer.REQUEST_UPDATE, new UpdateCommand());
        idToCommand.put(IdRequestToServer.REQUEST_UPLOAD, new UploadCommand());
    }

    @Override
    public void shutdown() throws IOException {

    }
}
