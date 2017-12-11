package common.commands.handlers.server;

import common.commands.request.SourcesRequest;
import common.commands.response.SourcesResponse;
import common.nio.ReadObject;
import common.nio.WriteObject;
import org.jetbrains.annotations.NotNull;
import server.state.IStateServer;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class SourcesCommand implements IServerCommand {
    @Override
    public void runCommand(@NotNull Socket socket,
                           @NotNull IStateServer stateClient) throws IOException, ClassNotFoundException {
        SourcesRequest request = ReadObject.readQuery(socket);
        LOGGER.info(String.format("get command %s ", request));
        List<SourcesResponse.ClientSource> clientInfoList = stateClient.getClients().stream()
                .filter(clientInfo ->
                        stateClient.getIdFilesOnClients(clientInfo)
                                .contains(request.getIdFile()))
                .map(clientInfo ->
                        new SourcesResponse.ClientSource(
                                clientInfo.getIpAddress(), clientInfo.getPort()))
                .collect(Collectors.toList());
        SourcesResponse response = new SourcesResponse(clientInfoList);
        WriteObject.writeMessage(socket, response);
    }
}
