package common.commands.handlers.server;

import common.commands.request.UploadRequest;
import common.commands.response.UploadResponse;
import common.files.FileInfoImpl;
import common.nio.ReadObject;
import common.nio.WriteObject;
import org.jetbrains.annotations.NotNull;
import server.state.IStateServer;

import java.io.IOException;
import java.net.Socket;

public class UploadCommand implements IServerCommand {

    @Override
    public void runCommand(@NotNull Socket socket,
                           @NotNull IStateServer stateClient) throws IOException, ClassNotFoundException {
        UploadRequest request = ReadObject.readQuery(socket);
        int idFile = stateClient.newFile(new FileInfoImpl(request.getNameFile(), request.getSize()));
        WriteObject.writeMessage(socket, new UploadResponse(idFile));
    }


}
