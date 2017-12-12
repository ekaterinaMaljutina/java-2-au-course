package server.state;

import client.api.ClientInfo;
import common.files.IFileInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Time;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IStateServer {

    int newFile(@NotNull IFileInfo fileInfo);

    void changedLastUpdateFile(@NotNull ClientInfo clientInfo, @NotNull Time time);

    @Nullable
    Time lastConnectionToClient(@NotNull ClientInfo clientInfo);

    void setFileOnClient(@NotNull ClientInfo client, Set<Integer> idFiles);

    void removeClient(@NotNull ClientInfo clientInfo);

    Map<Integer, IFileInfo> getAllFiles();

    Set<Integer> getIdFilesOnClients(@NotNull ClientInfo clientInfo);

    Set<ClientInfo> getClients();

    void updateSharedFiles(ClientInfo clientInfo, Set<Integer> idFiles);
}
