package client.impl;

import client.api.ClientListener;
import client.api.IStateClient;
import common.nio.ObjectWrite;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SaveStateClientListener implements ClientListener {
    private final String file;

    public SaveStateClientListener(String file) {
        this.file = file;
    }


    @Override
    public void changeState(@NotNull IStateClient stateClient) throws IOException {
        ObjectWrite.writeObjectToFile(file, stateClient);
    }
}
