package client;

import client.commands.CommandSingFactory;
import client.commands.ICommand;
import client.commands.Request;
import client.state.api.IState;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.util.Map;
import java.util.Scanner;

public class MainLoopClient implements Runnable {

    private final IState stateClient;
    private final Map<Integer, ICommand> idCommandToImplCommand;

    private final ICommand defaultCommand;
    private boolean isExit = false;

    public MainLoopClient(@NotNull IState stateClient,
                          @NotNull InetAddress addressServer, int portServer) {
        this.stateClient = stateClient;
        idCommandToImplCommand =
                CommandSingFactory.getInstanceCommand(addressServer, portServer);
        defaultCommand = idCommandToImplCommand.get(Request.UNKNOWN_REQUEST);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (!isExit &&
                scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("next line " + line);
            String[] argsCommand = line.split("\\s+");

            ICommand currentCommand = idCommandToImplCommand.getOrDefault(Integer.parseInt(argsCommand[0]), defaultCommand);
            isExit = currentCommand.runCommand(stateClient, argsCommand);
        }
    }
}
