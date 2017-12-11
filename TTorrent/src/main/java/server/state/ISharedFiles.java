package server.state;

import common.files.IFileInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ISharedFiles {

    Map<Integer, IFileInfo> getFiles();

    boolean checkExistFile(int idFile);

    @Nullable
    IFileInfo getFileInfo(int idFile);

    int newFile(@NotNull IFileInfo fileInfo);
}
