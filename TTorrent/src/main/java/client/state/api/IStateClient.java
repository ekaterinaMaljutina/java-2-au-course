package client.state.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public interface IStateClient {

    @NotNull
    Set<Path> getAllFiles();

    @NotNull
    Map<Path, IClientFile> getAllFilesWithInfo();

    @NotNull
    Set<Object> getListIdFiles();

    @NotNull
    Path getPathByFileId(int idFile);

    @Nullable
    IClientFile getFileInfoById(int idFile);
}
