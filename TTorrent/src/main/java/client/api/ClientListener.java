package client.api;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;

public interface ClientListener extends Serializable {
    void changeState(@NotNull IStateClient stateClient) throws IOException;
}
