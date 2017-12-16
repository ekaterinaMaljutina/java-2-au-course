package client.api;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface ClientListener {
    void changeState(@NotNull IStateClient stateClient) throws IOException;
}
