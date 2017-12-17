package client.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface IStateClient extends Serializable {

    @NotNull
    Set<String> getAllFiles();

    @NotNull
    Map<String, IClientFile> getAllFilesWithInfo();

    @NotNull
    Set<Integer> getListIdFiles();

    @Nullable
    String getPathByFileId(int idFile);

    @Nullable
    IClientFile getFileInfoById(int idFile);

    void addClientListener(@NotNull ClientListener clientListener);
}
