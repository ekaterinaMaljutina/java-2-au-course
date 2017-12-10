package client.commands;

import client.state.api.IState;
import org.jetbrains.annotations.NotNull;

public class ExitCommand implements ICommand {
    @Override
    public boolean runCommand(@NotNull IState state, String[] args) {
        return true;
    }

    @Override
    public String commandName() {
        return "Exit Command";
    }

    @NotNull
    @Override
    public Integer getId() {
        return Request.EXIT_REQUEST;
    }
}
