package server.state;

import client.api.IClientInfo;
import common.files.IFileInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Time;
import java.util.Map;
import java.util.Set;

public interface IStateServer {

    int newFile(@NotNull IFileInfo fileInfo);

    void changedLastUpdateFile(@NotNull IClientInfo clientInfo, @NotNull Time time);

    @Nullable
    Time lastConnectionToClient(@NotNull IClientInfo clientInfo);

    void setFileOnClient(@NotNull IClientInfo client, Set<Integer> idFiles);

    void removeClient(@NotNull IClientInfo clientInfo);

    Map<Integer, IFileInfo> getAllFiles();

    Set<Integer> getIdFilesOnClients(@NotNull IClientInfo clientInfo);

    Set<IClientInfo> getClients();
}
