package common.nio;

import common.exception.NotConnectionException;
import common.query.Query;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ReaderObject {

    public Query readQuery(@NotNull Socket socket) throws
            NotConnectionException {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket
                    .getInputStream());
            return (Query) inputStream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            throw new NotConnectionException(ex);
        }
    }
}

