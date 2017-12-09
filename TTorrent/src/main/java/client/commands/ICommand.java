package client.commands;


import client.state.api.IState;
import org.jetbrains.annotations.NotNull;

public interface ICommand {

    void runCommand(@NotNull IState state, String[] args);

    String commandName();
}
