package client.commands;


import client.state.api.IState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public interface ICommand {

    Logger LOGGER = LogManager.getLogger(ICommand.class);

    void runCommand(@NotNull IState state, String[] args);

    String commandName();
}
