package common.commands.handlers.server;

import client.SimpleClientInfo;
import client.api.ClientInfo;
import common.commands.request.UpdateRequest;
import common.commands.response.UpdateResponse;
import common.nio.ReadObject;
import common.nio.WriteObject;
import org.jetbrains.annotations.NotNull;
import server.state.IStateServer;

import java.io.IOException;
import java.net.Socket;
import java.sql.Time;

public class UpdateCommand implements IServerCommand {

    @Override
    public void runCommand(@NotNull Socket socket,
                           @NotNull IStateServer stateServer) throws IOException, ClassNotFoundException {
        UpdateRequest request = ReadObject.readQuery(socket);
        byte[] ip = socket.getInetAddress().getAddress();
        ClientInfo clientInfo = new SimpleClientInfo(ip[0], ip[1], ip[2], ip[3],
                request.getPort());
        stateServer.updateSharedFiles(clientInfo, request.getIdFiles());
        stateServer.changedLastUpdateFile(clientInfo, new Time(System.currentTimeMillis()));
        UpdateResponse response = new UpdateResponse(true);
        WriteObject.writeMessage(socket, response);
    }
}
