package common.commands.handlers.server;

import client.api.ClientInfo;
import client.impl.SimpleClientInfo;
import common.commands.request.Request;
import common.commands.request.SourcesRequest;
import common.commands.response.SourcesResponse;
import common.nio.QueryWriter;
import org.jetbrains.annotations.NotNull;
import server.api.IStateServer;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class SourcesCommand implements IServerCommand {
    @Override
    public void runCommand(@NotNull Socket socket,
                           @NotNull IStateServer stateClient,
                           @NotNull Request req) throws IOException, ClassNotFoundException {
        SourcesRequest request = (SourcesRequest) req;
        LOGGER.info(String.format("get command %s ", request));
        List<ClientInfo> clientInfoList = stateClient.getClients().stream()
                .filter(clientInfo ->
                        stateClient.getIdFilesOnClients(clientInfo)
                                .contains(request.getIdFile()))
                .map(clientInfo ->
                        new SimpleClientInfo(clientInfo.getIpAddress(), clientInfo.getPort()))
                .collect(Collectors.toList());
        SourcesResponse response = new SourcesResponse(clientInfoList);
        LOGGER.info("create response " + response);
        QueryWriter.writeMessage(socket, response);
    }
}
