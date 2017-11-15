package common.nio;

import common.exception.NotConnectionException;
import common.query.Query;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class WriterObject {

    public void writeMessage(@NotNull Socket socket, Query obj) throws NotConnectionException {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(obj);
            outputStream.flush();
        } catch (IOException ex) {
            throw new NotConnectionException(ex);
        }
    }
}
