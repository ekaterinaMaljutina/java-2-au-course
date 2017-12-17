package common.nio;

import common.Common;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;

public class QueryReader {

    @SuppressWarnings("unchecked")
    public static <T> T readQuery(@NotNull Socket socket)
            throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream =
                new ObjectInputStream(socket.getInputStream());
        return (T) inputStream.readObject();

    }


    public static void readContext(@NotNull Socket socket, Path pathToFile, int idPart)
            throws IOException {
        try (RandomAccessFile out = new RandomAccessFile(pathToFile.toFile(), "rw");
             InputStream inputStream = socket.getInputStream()) {
            out.seek(Common.PATH_OF_FILE_SIZE * idPart);
            IOUtils.copyLarge(inputStream, new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    throw new IOException("not implemented");
                }

                @Override
                public void write(@NotNull byte[] b, int off, int len) throws IOException {
                    out.write(b, off, len);
                }
            });
        }

    }

}
