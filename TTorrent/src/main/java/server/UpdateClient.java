package server;

import common.Common;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.api.IStateServer;

import java.sql.Time;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UpdateClient implements Runnable {

    private static final Logger LOGGER =
            LogManager.getLogger(UpdateClient.class);

    private final ScheduledExecutorService executorService;
    private final IStateServer stateServer;

    public UpdateClient(ScheduledExecutorService executorService,
                        IStateServer stateServer) {
        this.executorService = executorService;
        this.stateServer = stateServer;
    }

    @Override
    public void run() {
        final long currentTime = System.currentTimeMillis();
        stateServer.getClients().forEach(clientInfo -> {
            final Time lastUpdateClientTime =
                    stateServer.lastConnectionToClient(clientInfo);
            if (lastUpdateClientTime != null &&
                    currentTime - lastUpdateClientTime.getTime() >=
                            Common.UPDATE_CLIENT_FILES) {
                LOGGER.debug(" remove client ip = " +
                        Arrays.toString(clientInfo.getIpAddress()) +
                        " port = " + clientInfo.getPort());
                stateServer.removeClient(clientInfo);
            }
        });

        final long nextUpdate = stateServer.getClients().stream()
                .map(stateServer::lastConnectionToClient)
                .filter(Objects::nonNull).max(Time::compareTo)
                .map(time -> -(currentTime - time.getTime()) + Common.UPDATE_CLIENT_FILES)
                .orElse(Common.UPDATE_CLIENT_FILES);
        executorService.schedule(this, nextUpdate, TimeUnit.MICROSECONDS);

    }
}
