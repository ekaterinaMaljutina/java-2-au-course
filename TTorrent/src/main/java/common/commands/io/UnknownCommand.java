package common.commands.io;

import client.api.IState;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class UnknownCommand implements ICommand {


    private final Map<Integer, ICommand> allComands;

    private static final String title = "%5s|%20s";

    public UnknownCommand(Map<Integer, ICommand> allComands) {
        this.allComands = allComands;
    }

    @Override
    public boolean runCommand(@NotNull IState state, String[] args) {

        if (args.length != 0) {
            System.out.println(" unknown command id " + args[0]);
        }
        System.out.println(String.format(title, "id", "comment"));
        allComands.forEach((id, command) ->
                System.out.println(String.format(title, id, command.commandName())));
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
