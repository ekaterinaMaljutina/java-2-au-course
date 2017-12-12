package client;

import client.api.ExitCommandListener;
import client.state.api.IState;
import common.commands.io.IRequestToServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UpdateInfoTaskFromServer implements Runnable, ExitCommandListener {

    private static final Logger LOGGER =
            LogManager.getLogger(UpdateInfoTaskFromServer.class);

    private static final int COUNT_THREAD = 1;

    private final ScheduledExecutorService executorService;
    private final IState stateClient;
    private final IRequestToServer server;
    private final int portClient;

    public UpdateInfoTaskFromServer(IState stateClient, IRequestToServer server, int portClient) {
        this.stateClient = stateClient;
        this.server = server;
        this.portClient = portClient;
        executorService = Executors.newScheduledThreadPool(COUNT_THREAD);
        executorService.schedule(this, 0, TimeUnit.MICROSECONDS);
    }

    @Override
    public void run() {
        try {
            if (server.update(portClient, stateClient.getListIdFiles())) {
            LOGGER.debug("update file OK");
            }

        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error("update file ERROR " + e);
        }
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
    }
}
