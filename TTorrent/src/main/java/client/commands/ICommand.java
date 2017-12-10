package client.commands;


import client.state.api.IState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public interface ICommand {

    Logger LOGGER = LogManager.getLogger(ICommand.class);

    // is last command (exit)
    boolean runCommand(@NotNull IState state, String[] args);

    String commandName();

    @NotNull
    Integer getId();
}
