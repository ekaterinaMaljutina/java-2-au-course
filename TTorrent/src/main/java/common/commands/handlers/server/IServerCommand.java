package common.commands.handlers.server;


import common.commands.request.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import server.api.IStateServer;

import java.io.IOException;
import java.net.Socket;

public interface IServerCommand {
    Logger LOGGER = LogManager.getLogger(IServerCommand.class);

    default void run(@NotNull Socket socket,
                     @NotNull IStateServer state,
                     @NotNull Request request) {
        try {
            runCommand(socket, state, request);
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error(" fail run command " + e);
        }
    }

    void runCommand(@NotNull Socket socket,
                    @NotNull IStateServer stateServer,
                    @NotNull Request request) throws IOException, ClassNotFoundException;
}
