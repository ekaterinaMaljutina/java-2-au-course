package client.net;

import common.exception.AlreadyConnectionException;
import common.exception.NotConnectionException;
import common.nio.ReaderObject;
import common.nio.WriterObject;
import common.query.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

public class FTPClient implements Client {

    private final WriterObject writer = new WriterObject();
    private final ReaderObject reader = new ReaderObject();

    private Socket socket;

    @Override
    public void connect(int port, @NotNull String address)
            throws IOException, AlreadyConnectionException {
        if (socket != null) {
            throw new AlreadyConnectionException();
        }
        socket = new Socket(address, port);
    }

    @Override
    public void disconnect() throws IOException, NotConnectionException {
        if (socket == null) {
            throw new NotConnectionException();
        }
        send(new DisconnectRequest());
        socket.close();
    }

    @Override
    public ListResponse executeList(@NotNull String path) throws NotConnectionException {
        if (socket == null) {
            throw new NotConnectionException();
        }

        send(new ListRequest(path));
        Query query = receive();

        if (query == null || query.getQueryType() != TypeQuery.LIST_QUERY) {
            return null;
        }
        return (ListResponse) query;
    }

    @Override
    public GetResponse executeGet(@NotNull String path) throws
            NotConnectionException {

        if (socket == null) {
            throw new NotConnectionException();
        }

        send(new GetRequest(path));
        Query query = receive();

        if (query == null || query.getQueryType() != TypeQuery.GET_QUERY) {
            return null;
        }
        return (GetResponse) query;

    }

    @Override
    public boolean isRunning() {
        return socket != null;
    }

    private void send(Query query) {
        assert socket != null;
        try {
            writer.writeMessage(socket, query);
        } catch (NotConnectionException ex) {
            System.out.println(" Not send message ");
        }
    }

    private Query receive() {
        assert socket != null;
        try {
            return reader.readQuery(socket);
        } catch (NotConnectionException ex) {
            System.out.println(" Not receive message ");
        }
        return null;
    }
}
