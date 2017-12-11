package server.api;

import java.io.IOException;

public interface IServerLogic {
    void start() throws IOException;
    void shutdown() throws IOException;
}
