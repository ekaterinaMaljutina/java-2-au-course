package common.nio;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WriteObject {

    public static  <T> void writeMessage(@NotNull Socket socket, T obj)
            throws IOException {
        try (ObjectOutputStream outputStream
                     = new ObjectOutputStream(socket.getOutputStream())) {
            outputStream.writeObject(obj);
            outputStream.flush();
        }
    }

}
