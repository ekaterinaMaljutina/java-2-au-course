package client.net;

import common.exception.AlreadyConnectionException;
import common.exception.NotConnectionException;
import common.query.GetResponse;
import common.query.ListResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface Client {
    void connect(int port, @NotNull String address) throws
            IOException, AlreadyConnectionException;

    void disconnect() throws IOException, NotConnectionException;

    ListResponse executeList(@NotNull String path) throws NotConnectionException;

    GetResponse executeGet(@NotNull String path) throws NotConnectionException;

    boolean isRunning();
}
