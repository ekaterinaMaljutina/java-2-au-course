package common.nio;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class QueryReader {

    @SuppressWarnings("unchecked")
    public static <T> T readQuery(@NotNull Socket socket)
            throws IOException, ClassNotFoundException {
        try (
                ObjectInputStream inputStream = new ObjectInputStream(socket
                        .getInputStream())) {
            return (T) inputStream.readObject();
        }
    }

}
