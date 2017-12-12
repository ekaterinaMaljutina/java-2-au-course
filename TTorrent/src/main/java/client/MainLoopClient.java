package client;

import client.api.ExitCommandListener;
import client.state.api.IState;
import common.commands.io.*;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.util.*;

public class MainLoopClient implements Runnable {

    private final IState stateClient;
    private final Map<Integer, ICommand> idCommandToImplCommand;
    private final List<ExitCommandListener> runBeforeExitCommand = new LinkedList<>();

    private final ICommand defaultCommand;
    private boolean isExit = false;

    public MainLoopClient(@NotNull IState stateClient,
                          @NotNull InetAddress addressServer, int portServer) {
        this.stateClient = stateClient;
        idCommandToImplCommand = new HashMap<>();
        initMapCommand(addressServer, portServer);
        defaultCommand = idCommandToImplCommand.get(IdRequestToServer.UNKNOWN_REQUEST);
    }

    public void addExitCommand(@NotNull ExitCommandListener listener) {
        runBeforeExitCommand.add(listener);
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

    private void initMapCommand(@NotNull InetAddress address, int port) {
        idCommandToImplCommand.put(IdRequestToServer.LIST_REQUEST,
                new GetListCommand(address, port));
        idCommandToImplCommand.put(IdRequestToServer.EXIT_REQUEST,
                new MainLoopClient.ExitCommand());
        idCommandToImplCommand.put(IdRequestToServer.UNKNOWN_REQUEST,
                new UnknownCommand());
        idCommandToImplCommand.put(IdRequestToServer.UPLOAD_REQUEST,
                new UploadCommand(address, port));

    }

    public class ExitCommand implements ICommand {
        @Override
        public boolean runCommand(@NotNull IState state, String[] args) {
            runBeforeExitCommand.forEach(ExitCommandListener::shutdown);
            return true;
        }

        @Override
        public String commandName() {
            return "Exit Command";
        }

        @NotNull
        @Override
        public Integer getId() {
            return IdRequestToServer.EXIT_REQUEST;
        }
    }
}
