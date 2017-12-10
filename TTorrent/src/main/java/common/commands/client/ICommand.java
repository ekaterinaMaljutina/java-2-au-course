package common.commands.client;

import client.state.api.IState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

public interface ICommand {

    Logger LOGGER = LogManager.getLogger(ICommand.class);

    void runCommand(@NotNull Socket socket, @NotNull IState stateClient) throws IOException;
}
