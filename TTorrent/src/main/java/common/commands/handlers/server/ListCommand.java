package common.commands.handlers.server;

import common.commands.response.ListResponse;
import common.nio.WriteObject;
import org.jetbrains.annotations.NotNull;
import server.state.IStateServer;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ListCommand implements IServerCommand {

    @Override
    public void runCommand(@NotNull Socket socket,
                           @NotNull IStateServer stateServer) throws IOException {
        List<ListResponse.ItemFile> fileList = new LinkedList<>();
        LOGGER.info("get command list ");
        stateServer.getAllFiles().forEach(
                (id, fileInfo) -> {
                    fileList
                            .add(new ListResponse.ItemFile(id,
                                    fileInfo.getName(), fileInfo.getSize()));
                });
        ListResponse response = new ListResponse(fileList);
        WriteObject.writeMessage(socket, response);
    }
}
