package common.commands.handlers.server;

import common.commands.request.Request;
import common.commands.request.UploadRequest;
import common.commands.response.UploadResponse;
import common.files.FileInfoImpl;
import common.nio.QueryWriter;
import org.jetbrains.annotations.NotNull;
import server.api.IStateServer;

import java.io.IOException;
import java.net.Socket;

public class UploadCommand implements IServerCommand {

    @Override
    public void runCommand(@NotNull Socket socket,
                           @NotNull IStateServer stateClient,
                           @NotNull Request req) throws IOException, ClassNotFoundException {
        UploadRequest request = (UploadRequest) req;
        LOGGER.info(String.format("get command %s ", request));
        int idFile = stateClient.newFile(new FileInfoImpl(request.getNameFile(), request.getSize()));
        QueryWriter.writeMessage(socket, new UploadResponse(idFile));
    }


}
