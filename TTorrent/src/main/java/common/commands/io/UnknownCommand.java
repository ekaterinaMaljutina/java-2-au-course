package common.commands.io;

import client.state.api.IState;
import org.jetbrains.annotations.NotNull;

public class UnknownCommand implements ICommand {

    @Override
    public boolean runCommand(@NotNull IState state, String[] args) {
        if (args.length != 0) {
            System.out.println(" unknown command id " + args[0]);
        }
        return false;
    }

    @Override
    public String commandName() {
        return "";
    }

    @NotNull
    @Override
    public Integer getId() {
        return IdRequestToServer.UNKNOWN_REQUEST;
    }
}
