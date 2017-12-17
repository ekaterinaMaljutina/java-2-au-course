package client.impl;

import client.api.ClientListener;
import client.api.IStateClient;
import common.nio.ObjectWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;

public class SaveStateClientListener implements ClientListener, Serializable {
    private final String file;

    public SaveStateClientListener(String file) {
        this.file = file;
    }

    @Override
    public void changeState(@NotNull IStateClient stateClient) throws IOException {
        ObjectWriter.writeObjectToFile(file, stateClient);
    }
}
