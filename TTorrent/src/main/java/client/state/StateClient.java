package client.state;

import client.api.IClientFile;
import client.state.api.IState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StateClient implements IState, Serializable {

    private static final Logger LOGGER = LogManager.getLogger(StateClient.class);

    private final Map<Integer, Path> idxFileToPath;
    private final Map<Path, IClientFile> pathToFileInfo;


    public StateClient(Map<String, IClientFile> pathNameToFileInfo) {
        idxFileToPath = new ConcurrentHashMap<>();
        pathToFileInfo = new ConcurrentHashMap<>();

        pathNameToFileInfo.forEach((pathToFile, fileInfo) -> {
            Path path = Paths.get(pathToFile);
            pathToFileInfo.put(path, fileInfo);
            idxFileToPath.put(fileInfo.getId(), path);
        });

    }

    @Override
    public boolean partOfFile(@NotNull Path file, int numberOfPart) {
        return false;
    }

    @Override
    public boolean newFile(@NotNull Path file, IClientFile fileInfo) {
        return false;
    }

    @NotNull
    @Override
    public Set<Path> getAllFiles() {
        return Collections.unmodifiableSet(pathToFileInfo.keySet());
    }

    @NotNull
    @Override
    public Map<Path, IClientFile> getAllFilesWithInfo() {
        return Collections.unmodifiableMap(pathToFileInfo);
    }

    @NotNull
    @Override
    public Set<Integer> getListIdFiles() {
        return Collections.unmodifiableSet(idxFileToPath.keySet());
    }

    @Nullable
    @Override
    public Path getPathByFileId(int idFile) {
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
}
