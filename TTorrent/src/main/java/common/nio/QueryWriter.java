package common.nio;

import common.files.FilesCommon;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class QueryWriter {

    public static <T> void writeMessage(@NotNull Socket socket, T obj)
            throws IOException {
        try (ObjectOutputStream outputStream
                     = new ObjectOutputStream(socket.getOutputStream())) {
            outputStream.writeObject(obj);
            outputStream.flush();
        }
    }

    public static void writeContext(@NotNull Socket socket, Path pathToFile, int
            idPart)
            throws IOException {
        try (final InputStream inputStreamFile = Files.newInputStream(pathToFile);
             OutputStream outputStream = socket.getOutputStream()) {
            IOUtils.copyLarge(inputStreamFile, outputStream,
                    idPart * FilesCommon.PATH_OF_FILE_SIZE,
                    FilesCommon.PATH_OF_FILE_SIZE);
        }
    }
}
