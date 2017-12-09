package client.api;

import java.io.IOException;

public interface IClientLogic {
    void  shutdown() throws IOException;
    void start();
}
