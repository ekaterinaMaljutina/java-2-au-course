package client.commands;

import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class CommandSingFactory {

    private static final Map<Integer, ICommand> idxCommandToImplCommand = new HashMap<>();

    public static Map<Integer, ICommand> getInstanceCommand(@NotNull InetAddress addressServer, int portServer) {
        if (idxCommandToImplCommand.size() == 0) {
            initMapCommand(addressServer, portServer);
        }
        return idxCommandToImplCommand;
    }

    private static void initMapCommand(@NotNull InetAddress address, int port) {
        idxCommandToImplCommand.put(Request.LIST_REQUEST, new GetListCommand(address, port));
        idxCommandToImplCommand.put(Request.EXIT_REQUEST, new ExitCommand());
        idxCommandToImplCommand.put(Request.UNKNOWN_REQUEST, new UnknownCommand());
        idxCommandToImplCommand.put(Request.UPLOAD_REQUEST, new UploadCommand(address, port));
    }
}
