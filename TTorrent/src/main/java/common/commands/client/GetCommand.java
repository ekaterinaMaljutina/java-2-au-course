package common.commands.client;

import client.api.IClientFile;
import client.state.api.IState;
import common.files.FilesCommon;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class GetCommand implements ICommand {
    @Override
    public void runCommand(@NotNull Socket socket,
                           @NotNull IState stateClient) throws IOException {

        try (DataInputStream inputStream =
                     new DataInputStream(socket.getInputStream())) {
            int idFile = inputStream.readInt();
            int idPart = inputStream.readInt();

            LOGGER.info(String.format("get command GET id=%d, idPart=%d", idFile, idPart));

            Path pathToFile = stateClient.getPathByFileId(idFile);
            IClientFile fileInfo = stateClient.getFileInfoById(idFile);
            if (pathToFile == null || fileInfo == null) {
                LOGGER.warn(String.format(" file by id=%d not found", idFile));
                return;
            }

            if (!fileInfo.getParts().contains(idPart)){
                LOGGER.warn(String.format("part file %d with idPart=%d not contains on client", idFile, idPart));
                return;
            }

            try (final InputStream inputStreamFile = Files.newInputStream(pathToFile);
            OutputStream outputStream = socket.getOutputStream()) {
                IOUtils.copyLarge(inputStream, outputStream,
                        idPart * FilesCommon.PATH_OF_FILE_SIZE,
                        FilesCommon.PATH_OF_FILE_SIZE);
            }
        }

    }
}
