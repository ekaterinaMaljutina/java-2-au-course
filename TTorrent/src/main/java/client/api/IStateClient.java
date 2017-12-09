package client.api;

import common.files.IFileInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface IStateClient {

    @NotNull
    List<Path> getAllFiles();

    @NotNull
    Map<Path, IFileInfo> getAllFilesWithInfo();

    @NotNull
    List<Integer> getListIdFiles();

    @NotNull
    Path getPathByFileId(int idFile);

    @Nullable
    IFileInfo getFileInfoById(int idFile);
}
