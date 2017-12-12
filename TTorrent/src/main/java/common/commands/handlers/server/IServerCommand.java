package common.commands.handlers.server;


import common.commands.handlers.client.ClientCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import server.api.IStateServer;

import java.io.IOException;
import java.net.Socket;

public interface IServerCommand {
    Logger LOGGER = LogManager.getLogger(ClientCommand.class);

    default void run(@NotNull Socket socket,
                     @NotNull IStateServer state) {
        try {
            runCommand(socket, state);
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error(" fail run command " + e);
        }
    }

    void runCommand(@NotNull Socket socket,
                    @NotNull IStateServer stateServer) throws IOException, ClassNotFoundException;
}
