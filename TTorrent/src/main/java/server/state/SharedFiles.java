package server.state;

import common.files.IFileInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SharedFiles implements ISharedFiles, Serializable {

    private final Map<Integer, IFileInfo> idToFileInfo;

    public SharedFiles(Map<Integer, IFileInfo> idToFileInfo) {
        this.idToFileInfo = new ConcurrentHashMap<>(idToFileInfo);
    }

    @Override
    public Map<Integer, IFileInfo> getFiles() {
        return idToFileInfo;
    }

    @Override
    public boolean checkExistFile(int idFile) {
        return idToFileInfo.containsKey(idFile);
    }

    @Nullable
    @Override
    public IFileInfo getFileInfo(int idFile) {
        return idToFileInfo.get(idFile);
    }

    @Override
    public int newFile(@NotNull IFileInfo fileInfo) {
        int newId = getNewId();
        idToFileInfo.put(newId, fileInfo);
        return newId;
    }

    private int getNewId() {
        return idToFileInfo.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
    }
}
