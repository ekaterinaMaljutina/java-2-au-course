package common.commands.handlers.client;

import client.api.IClientFile;
import client.api.IState;
import common.commands.request.GetRequest;
import common.commands.request.Request;
import common.nio.QueryWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

public class GetCommand implements ClientCommand {
    @Override
    public void runCommand(@NotNull Socket socket,
                           @NotNull IState stateClient,
                           @NotNull Request req)
            throws IOException, ClassNotFoundException {
        GetRequest request = (GetRequest) req;
        LOGGER.info(String.format("get command %s ", request));

        String pathToFile = stateClient.getPathByFileId(request.getIdFile());
        IClientFile fileInfo = stateClient.getFileInfoById(request.getIdFile());
        if (pathToFile == null || fileInfo == null) {
            LOGGER.warn(String.format(" file by id=%d not found", request.getIdFile()));
            return;
        }

        if (!fileInfo.getParts().contains(request.getIdPartOfFile())) {
            LOGGER.warn(String.format("part file %d with idPart=%d not contains on client", request.getIdFile(), request.getIdPartOfFile()));
            return;
        }

        QueryWriter.writeContext(socket, pathToFile, request.getIdPartOfFile());

    }

    @Override
    public Integer getId() {
        return IdRequestToClient.REQUEST_GET;
    }
}
