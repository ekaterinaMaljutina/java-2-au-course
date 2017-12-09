package client.commands;

import client.state.api.IState;
import org.jetbrains.annotations.NotNull;

public class GetListCommand implements ICommand {

    private IServer server;



    @Override
    public void runCommand(@NotNull IState state, String[] args) {

    }

    @Override
    public String commandName() {
        return null;
    }
}
