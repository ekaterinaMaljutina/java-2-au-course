package common.commands.handlers.client;

import client.api.IClientFile;
import client.api.IState;
import common.commands.request.GetRequest;
import common.nio.QueryReader;
import common.nio.QueryWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;

public class GetCommand implements ClientCommand {
    @Override
    public void runCommand(@NotNull Socket socket,
                           @NotNull IState stateClient)
            throws IOException, ClassNotFoundException {
        GetRequest request = QueryReader.readQuery(socket);
        LOGGER.info(String.format("get command %s ", request));

        Path pathToFile = stateClient.getPathByFileId(request.getIdFile());
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
}
