package server.impl;

import common.files.IFileInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import server.api.ChangeFileList;
import server.api.ISharedFiles;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class SharedFiles implements ISharedFiles, Serializable {

    private final Map<Integer, IFileInfo> idToFileInfo;
    private final List<ChangeFileList> changeFileLists = new CopyOnWriteArrayList<>();

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
        changeListFiles();
        return newId;
    }

    private int getNewId() {
        return idToFileInfo.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
    }

    @Override
    public void addListenerChangeFiles(@NotNull ChangeFileList changeFileList) {
        changeFileLists.add(changeFileList);
    }

    private void changeListFiles() {
        changeFileLists.forEach(changeFileList -> changeFileList
                .changeStateSharedFiles(this));
    }
}
