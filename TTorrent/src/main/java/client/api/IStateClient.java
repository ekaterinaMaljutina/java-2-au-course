package client.api;

import client.api.IClientFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public interface IStateClient extends Serializable {

    @NotNull
    Set<Path> getAllFiles();

    @NotNull
    Map<Path, IClientFile> getAllFilesWithInfo();

    @NotNull
    Set<Integer> getListIdFiles();

    @Nullable
    Path getPathByFileId(int idFile);

    @Nullable
    IClientFile getFileInfoById(int idFile);

    void addClientListener(@NotNull ClientListener clientListener);
}
