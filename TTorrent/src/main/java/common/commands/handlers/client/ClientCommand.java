package common.commands.handlers.client;

import client.api.IState;
import common.commands.request.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

public interface ClientCommand {

    Logger LOGGER = LogManager.getLogger(ClientCommand.class);

    default void run(@NotNull Socket socket,
                     @NotNull IState state,
                     @NotNull Request request) {
        try {
            runCommand(socket, state, request);
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error(" fail run command " + e);
        }
    }

    void runCommand(@NotNull Socket socket,
                    @NotNull IState stateClient,
                    @NotNull Request request)
            throws IOException, ClassNotFoundException;

    Integer getId();

}
