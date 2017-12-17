package server;

import common.commands.handlers.server.*;
import common.commands.request.Request;
import common.nio.QueryReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import server.api.IServerLogic;
import server.api.IStateServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class MainLoopServer implements IServerLogic {

    private static final Logger LOGGER = LogManager.getLogger(MainLoopServer.class);
    private static final int COUNT_THREAD = 4;
    private static final Map<Integer, IServerCommand> idToCommand = new HashMap<>();


    private final int portServer;
    private final IStateServer stateServer;


    private final ExecutorService threadPool = Executors.newFixedThreadPool(COUNT_THREAD);
    private final ScheduledExecutorService clientUpdateScheduler =
            new ScheduledThreadPoolExecutor(1);

    public MainLoopServer(int portServer, @NotNull IStateServer stateServer) {
        this.portServer = portServer;
        this.stateServer = stateServer;
        intiMapCommand();
    }

    @Override
    public void start() throws IOException {

        clientUpdateScheduler.execute(
                new UpdateClient(clientUpdateScheduler, stateServer));

        LOGGER.debug("start server with top " + portServer);
        try (ServerSocket serverSocket = new ServerSocket(portServer)) {
            while (!serverSocket.isClosed()) {
                Socket socketClient = serverSocket.accept();
                threadPool.execute(() -> receiveClient(socketClient));
            }
        }
    }


    private void receiveClient(@NotNull Socket socketClient) {
        try {
            final Request request = QueryReader.readQuery(socketClient);
            LOGGER.info("get commnad id = " + request.getId());
            IServerCommand currentCommand = findCommand(request.getId());
            if (currentCommand == null) {
                return;
            }
            currentCommand.run(socketClient, stateServer, request);
        } catch (IOException | ClassNotFoundException e) {
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
        idToCommand.put(IdRequestToServer.REQUEST_EXIT, new ExitCommand());
    }

    @Override
    public void shutdown() throws IOException {
        clientUpdateScheduler.shutdown();
        threadPool.shutdown();
    }

    private class ExitCommand implements IServerCommand {
        @Override
        public void runCommand(@NotNull Socket socket,
                               @NotNull IStateServer stateServer,
                               @NotNull Request request)
                throws IOException, ClassNotFoundException {
            shutdown();
        }
    }
}
