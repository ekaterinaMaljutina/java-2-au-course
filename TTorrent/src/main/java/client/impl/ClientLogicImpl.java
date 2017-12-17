package client.impl;

import client.api.IClientLogic;
import client.api.IState;
import common.commands.handlers.client.ClientCommand;
import common.commands.handlers.client.GetCommand;
import common.commands.handlers.client.IdRequestToClient;
import common.commands.handlers.client.StatCommand;
import common.commands.request.Request;
import common.nio.QueryReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientLogicImpl implements IClientLogic {

    private static final Logger LOGGER = LogManager.getLogger(ClientLogicImpl.class);

    private static final Map<Integer, ClientCommand> idToRequest;
    private static final int COUNT_WORKERS = 4;

    static {
        idToRequest = new ConcurrentHashMap<>();
        idToRequest.put(IdRequestToClient.REQUEST_STAT, new StatCommand());
        idToRequest.put(IdRequestToClient.REQUEST_GET, new GetCommand());
    }

    private final int portClient;
    private final IState stateClient;
    private final ExecutorService executorService =
            Executors.newScheduledThreadPool(COUNT_WORKERS);
    private ServerSocket serverSocket;

    public ClientLogicImpl(int portClient, IState stateClient) {
        this.portClient = portClient;
        this.stateClient = stateClient;
    }

    @Override
    public void shutdown() throws IOException {
        serverSocket.close();
    }

    @Override
    public void start() {
        try (ServerSocket serverSocketCurrent = new ServerSocket(portClient)) {
            serverSocket = serverSocketCurrent;
            while (!serverSocketCurrent.isClosed()) {
                Socket socket = serverSocketCurrent.accept();
                execute(socket);
            }
        } catch (IOException e) {
            if (serverSocket == null || !serverSocket.isClosed()) {
                LOGGER.fatal("get er.", e);
            }
        }
    }

    private void execute(@NotNull Socket socket) {
        executorService.execute(() -> {
            try {
                Request request = QueryReader.readQuery(socket);
                if (!idToRequest.containsKey(request.getId())) {
                    LOGGER.warn("Unknown request with id" + request.getId());
                } else {
                    LOGGER.info("Run command with id " + request.getId());
                    idToRequest.get(request.getId()).run(socket, stateClient);
                    LOGGER.info("Done command with id = " + request.getId());
                }
            } catch (IOException | ClassNotFoundException e) {
                if (!serverSocket.isClosed()) {
                    LOGGER.error("get error on command ", e);
                }
            }
        });
    }
}
