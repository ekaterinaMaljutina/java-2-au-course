package server;

import common.Common;
import server.api.IStateServer;
import server.impl.StateServer;

import java.sql.Time;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UpdateClient implements Runnable {

    private final ScheduledExecutorService executorService;
    private final IStateServer stateServer;

    public UpdateClient(ScheduledExecutorService executorService, IStateServer stateServer) {
        this.executorService = executorService;
        this.stateServer = stateServer;
    }

    @Override
    public void run() {
        final long currentTime = System.currentTimeMillis();
        stateServer.getClients().forEach(clientInfo -> {
            final Time lastUpdateClientTime = stateServer
                    .lastConnectionToClient(clientInfo);
            if (lastUpdateClientTime != null &&
                    currentTime - lastUpdateClientTime.getTime() >=
                            Common.UPDATE_CLIENT_FILES) {
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
