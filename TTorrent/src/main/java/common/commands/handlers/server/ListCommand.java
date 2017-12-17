package common.commands.handlers.server;

import common.commands.request.Request;
import common.commands.response.ListResponse;
import common.nio.QueryWriter;
import org.jetbrains.annotations.NotNull;
import server.api.IStateServer;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ListCommand implements IServerCommand {

    @Override
    public void runCommand(@NotNull Socket socket,
                           @NotNull IStateServer stateServer,
                           @NotNull Request request) throws IOException {
        List<ListResponse.ItemFile> fileList = new LinkedList<>();
        LOGGER.info("get command list ");
        stateServer.getAllFiles().forEach(
                (id, fileInfo) -> {
                    fileList
                            .add(new ListResponse.ItemFile(id,
                                    fileInfo.getName(), fileInfo.getSize()));
                });
        ListResponse response = new ListResponse(fileList);
        LOGGER.info("create response " + response);
        QueryWriter.writeMessage(socket, response);
    }
}
