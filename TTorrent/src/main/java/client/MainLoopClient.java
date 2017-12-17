package client;

import client.api.ExitCommandListener;
import client.api.IState;
import common.commands.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.util.*;

public class MainLoopClient implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(MainLoopClient.class);
    private final IState stateClient;
    private final Map<Integer, ICommand> idCommandToImplCommand;
    private final List<ExitCommandListener> runBeforeExitCommand = new LinkedList<>();

    private final ICommand defaultCommand;
    private boolean isExit = false;

    public MainLoopClient(@NotNull IState stateClient,
                          @NotNull InetAddress addressServer, int portServer,
                          @NotNull Downloader downloader) {
        this.stateClient = stateClient;
        idCommandToImplCommand = new HashMap<>();
        initMapCommand(addressServer, portServer, downloader);
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
            String[] argsCommand = line.split("\\s+");
            int idCommand;
            try {
                if (argsCommand.length == 0) {
                    LOGGER.warn("arg command is empty");
                }
                idCommand = Integer.parseInt(argsCommand[0]);
            } catch (NumberFormatException e) {
                continue;
            }
            ICommand currentCommand = idCommandToImplCommand.getOrDefault(idCommand,
                    defaultCommand);
            isExit = currentCommand.runCommand(stateClient, argsCommand);
        }
    }

    private void initMapCommand(@NotNull InetAddress address, int port,
                                @NotNull Downloader downloader) {
        idCommandToImplCommand.put(IdRequestToServer.LIST_REQUEST,
                new GetListCommand(address, port));
        idCommandToImplCommand.put(IdRequestToServer.EXIT_REQUEST,
                new MainLoopClient.ExitCommand());
        idCommandToImplCommand.put(IdRequestToServer.UPLOAD_REQUEST,
                new UploadCommand(address, port));
        idCommandToImplCommand.put(IdRequestToServer.SOURCES_REQUEST,
                new DownloadFileCommand(downloader));
        idCommandToImplCommand.put(IdRequestToServer.SHOW_LOCAL_FILES_REQUEST,
                new GetLocalFilesCommand());
        idCommandToImplCommand.put(IdRequestToServer.UNKNOWN_REQUEST,
                new UnknownCommand(idCommandToImplCommand));
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
