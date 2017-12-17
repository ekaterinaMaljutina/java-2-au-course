package server.api;

import common.files.IFileInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Map;

public interface ISharedFiles extends Serializable {

    Map<Integer, IFileInfo> getFiles();

    boolean checkExistFile(int idFile);

    @Nullable
    IFileInfo getFileInfo(int idFile);

    int newFile(@NotNull IFileInfo fileInfo);

    void addListenerChangeFiles(@NotNull ChangeFileList changeFileList);

    void initFileListener();
}
