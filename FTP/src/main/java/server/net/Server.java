package server.net;

import common.exception.AlreadyConnectionException;
import common.exception.NotConnectionException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface Server {


    void start(int port, @NotNull String address)
            throws IOException, AlreadyConnectionException;

    void stop() throws NotConnectionException;

    boolean isRunning();
}
