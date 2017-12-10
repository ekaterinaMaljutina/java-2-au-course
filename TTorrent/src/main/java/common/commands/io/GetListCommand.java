package common.commands.io;

import client.state.api.IState;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;

public class GetListCommand implements ICommand {

    private IRequestToServer server;


    public GetListCommand(@NotNull InetAddress addressServer, int portServer) {
        server = new RequestToServer(addressServer, portServer);
    }

    @Override
    public boolean runCommand(@NotNull IState state, String[] args) {
        try {
            server.getListFiles()
                    .forEach((id, fileInfo) ->
                            System.out.println(String.format(
                                    "id = %d; name = %s; size = %d",
                                    id, fileInfo.getName(), fileInfo.getSize())));

        } catch (IOException e) {
            LOGGER.error("not load list from server");
            LOGGER.error(e);
        }
        return false;
    }

    @Override
    public String commandName() {
        return "List IdRequestToServer To Server";
    }

    @NotNull
    @Override
    public Integer getId() {
        return IdRequestToServer.LIST_REQUEST;
    }
}
