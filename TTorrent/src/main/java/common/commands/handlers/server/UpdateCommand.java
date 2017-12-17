package common.commands.handlers.server;

import client.api.ClientInfo;
import client.impl.SimpleClientInfo;
import common.commands.request.Request;
import common.commands.request.UpdateRequest;
import common.commands.response.UpdateResponse;
import common.nio.QueryWriter;
import org.jetbrains.annotations.NotNull;
import server.api.IStateServer;

import java.io.IOException;
import java.net.Socket;
import java.sql.Time;

public class UpdateCommand implements IServerCommand {

    @Override
    public void runCommand(@NotNull Socket socket,
                           @NotNull IStateServer stateServer,
                           @NotNull Request request) throws IOException, ClassNotFoundException {
        UpdateRequest updateRequest = (UpdateRequest) request;
        LOGGER.info(String.format("get command %s ", updateRequest));
        byte[] ip = socket.getInetAddress().getAddress();
        ClientInfo clientInfo = new SimpleClientInfo(ip[0], ip[1], ip[2], ip[3],
                updateRequest.getPort());
        stateServer.updateSharedFiles(clientInfo, updateRequest.getIdFiles());
        stateServer.changedLastUpdateFile(clientInfo, new Time(System.currentTimeMillis()));
        UpdateResponse response = new UpdateResponse(true);
        LOGGER.debug(" create response " + response);
        QueryWriter.writeMessage(socket, response);
    }
}
