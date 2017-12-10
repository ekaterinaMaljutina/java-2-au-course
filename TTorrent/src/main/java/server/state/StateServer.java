package server.state;

import client.api.IClientInfo;
import common.files.IFileInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Time;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StateServer implements IStateServer {


    private final Map<IClientInfo, Set<Integer>> clientToListFiles = new ConcurrentHashMap<>();

    private final Map<IClientInfo, Time> lastUpdateClient = new ConcurrentHashMap<>();

    @Override
    public int newFile(@NotNull IFileInfo fileInfo) {
        return 0;
    }

    @Override
    public void setFileOnClient(@NotNull IClientInfo client, Set<Integer> idFiles) {
        clientToListFiles.put(client, idFiles);
    }

    @Override
    @Nullable
    public Time lastConnectionToClient(@NotNull IClientInfo clientInfo) {
        return lastUpdateClient.getOrDefault(clientInfo, null);
    }

    @Override
    public void changedLastUpdateFile(@NotNull IClientInfo clientInfo, @NotNull Time time) {
        lastUpdateClient.put(clientInfo, time);
    }

    @Override
    public Map<Integer, IFileInfo> getAllFiles() {
        return null;
    }

    @Override
    public void removeClient(@NotNull IClientInfo clientInfo) {
        lastUpdateClient.remove(clientInfo);
        clientToListFiles.remove(clientInfo);
    }

    @Override
    public Set<Integer> getIdFilesOnClients(@NotNull IClientInfo clientInfo) {
        return clientToListFiles.get(clientInfo);
    }

    @Override
    public Set<IClientInfo> getClients() {
        return clientToListFiles.keySet(); // unmodif ???
    }
}
