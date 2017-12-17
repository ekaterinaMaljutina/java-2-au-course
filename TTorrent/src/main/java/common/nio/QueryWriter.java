package common.nio;

import common.Common;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class QueryWriter {

    public static <T> void writeMessage(@NotNull Socket socket, T obj)
            throws IOException {
        ObjectOutputStream outputStream
                = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(obj);
        outputStream.flush();
    }

    public static void writeContext(@NotNull Socket socket,
                                    @NotNull String pathToFile, int idPart)
            throws IOException {
        try (final InputStream inputStreamFile = Files.newInputStream
                (Paths.get(pathToFile));
             OutputStream outputStream = socket.getOutputStream()) {
            IOUtils.copyLarge(inputStreamFile, outputStream,
                    idPart * Common.PATH_OF_FILE_SIZE,
                    Common.PATH_OF_FILE_SIZE);
        }
    }
}
