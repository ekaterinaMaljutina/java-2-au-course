package common.commands.handlers.client;

import client.api.IClientFile;
import client.api.IState;
import common.commands.request.StatRequest;
import common.commands.response.StatResponse;
import common.nio.QueryReader;
import common.nio.QueryWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

public class StatCommand implements ClientCommand {

    @Override
    public void runCommand(@NotNull Socket socket,
                           @NotNull IState stateClient) throws IOException, ClassNotFoundException {

        StatRequest request = QueryReader.readQuery(socket);
        LOGGER.info(String.format("get command %s ", request));
        IClientFile fileInfo = stateClient.getFileInfoById(request.getIdFile());
        if (fileInfo == null) {
            LOGGER.warn(String.format("file by id %d not found", request.getIdFile()));
            return;
        }
        StatResponse response = new StatResponse(fileInfo.getParts());
        QueryWriter.writeMessage(socket, response);
    }
}
