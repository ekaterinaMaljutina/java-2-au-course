package common.commands.handlers.client;

import client.state.api.IState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

public interface ClientCommand {

    Logger LOGGER = LogManager.getLogger(ClientCommand.class);

    default void run(@NotNull Socket socket,
                     @NotNull IState state) {
        try {
            runCommand(socket, state);
        } catch (IOException e) {
            LOGGER.error(" fail run command");
        }
    }

    void runCommand(@NotNull Socket socket,
                    @NotNull IState stateClient) throws IOException, ClassNotFoundException;
}
