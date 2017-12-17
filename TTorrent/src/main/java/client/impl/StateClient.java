package client.impl;

import client.api.ClientListener;
import client.api.IClientFile;
import client.api.IState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StateClient implements IState, Serializable {

    private static final Logger LOGGER = LogManager.getLogger(StateClient.class);

    private final Map<Integer, String> idxFileToPath;
    private final Map<String, IClientFile> pathToFileInfo;
    private List<ClientListener> clientListeners = new LinkedList<>();


    public StateClient(Map<String, IClientFile> pathNameToFileInfo) {
        idxFileToPath = new ConcurrentHashMap<>();
        pathToFileInfo = new ConcurrentHashMap<>();

        pathNameToFileInfo.forEach((pathToFile, fileInfo) -> {
//            Path path = Paths.get(pathToFile);
            pathToFileInfo.put(pathToFile, fileInfo);
            idxFileToPath.put(fileInfo.getId(), pathToFile);
        });

    }

    @Override
    public void partOfFile(@NotNull String file, int numberOfPart) {
        if (pathToFileInfo.get(file).addPartOfFile(numberOfPart)) {
            update();
        }
    }

    @Override
    public boolean newFile(@NotNull String file, IClientFile fileInfo) {
        if (pathToFileInfo.containsKey(file) ||
                idxFileToPath.containsKey(fileInfo.getId())) {
            LOGGER.debug(" contains file " + file);
            return false;
        }

        pathToFileInfo.put(file, fileInfo);
        idxFileToPath.put(fileInfo.getId(), file);
        update();
        return true;
    }

    @NotNull
    @Override
    public Set<String> getAllFiles() {
        return Collections.unmodifiableSet(pathToFileInfo.keySet());
    }

    @NotNull
    @Override
    public Map<String, IClientFile> getAllFilesWithInfo() {
        return Collections.unmodifiableMap(pathToFileInfo);
    }

    @NotNull
    @Override
    public Set<Integer> getListIdFiles() {
        return Collections.unmodifiableSet(idxFileToPath.keySet());
    }

    @Nullable
    @Override
    public String getPathByFileId(int idFile) {
        return idxFileToPath.get(idFile);
    }

    @Nullable
    @Override
    public IClientFile getFileInfoById(int idFile) {
        if (idxFileToPath.containsKey(idFile)) {
            return pathToFileInfo.get(idxFileToPath.get(idFile));
        }
        return null;
    }

    @Override
    public void addClientListener(@NotNull ClientListener clientListener) {
        clientListeners.add(clientListener);
    }

    private void update() {
        clientListeners.forEach(clientListener -> {
            try {
                clientListener.changeState(this);
            } catch (IOException e) {
                LOGGER.error(" update state listener ERROR:" + e);
                e.printStackTrace();
            }
        });
    }
}
