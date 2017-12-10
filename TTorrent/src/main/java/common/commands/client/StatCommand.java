package common.commands.client;

import client.api.IClientFile;
import client.state.api.IState;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Set;

public class StatCommand implements ICommand {

    @Override
    public void runCommand(@NotNull Socket socket,
                           @NotNull IState stateClient) throws IOException {
        // ??? response serialization ????

        try (DataInputStream inputStream =
                     new DataInputStream(socket.getInputStream())) {
            int idFile = inputStream.readInt();

            LOGGER.info("get command STAT id=" + idFile);
            IClientFile fileInfo = stateClient.getFileInfoById(idFile);
            if (fileInfo == null) {
                LOGGER.warn(String.format("file by id %d not found", idFile));
                return;
            }
            try (DataOutputStream out =
                         new DataOutputStream(socket.getOutputStream())) {
                final Set<Integer> parts = fileInfo.getParts();
                out.writeInt(parts.size());
                for (Integer part : parts) {
                    out.writeInt(part);
                }
            }
        }

    }
}
