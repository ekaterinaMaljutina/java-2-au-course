package server.state;

import client.api.ClientInfo;
import common.files.IFileInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Time;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StateServer implements IStateServer {


    private final Map<ClientInfo, Set<Integer>> clientToListFiles = new ConcurrentHashMap<>();

    private final Map<ClientInfo, Time> lastUpdateClient = new ConcurrentHashMap<>();

    @Override
    public int newFile(@NotNull IFileInfo fileInfo) {
        return 0;
    }

    @Override
    public void setFileOnClient(@NotNull ClientInfo client, Set<Integer> idFiles) {
        clientToListFiles.put(client, idFiles);
    }

    @Override
    @Nullable
    public Time lastConnectionToClient(@NotNull ClientInfo clientInfo) {
        return lastUpdateClient.getOrDefault(clientInfo, null);
    }

    @Override
    public void changedLastUpdateFile(@NotNull ClientInfo clientInfo, @NotNull Time time) {
        lastUpdateClient.put(clientInfo, time);
    }

    @Override
    public Map<Integer, IFileInfo> getAllFiles() {
        return null;
    }

    @Override
    public void removeClient(@NotNull ClientInfo clientInfo) {
        lastUpdateClient.remove(clientInfo);
        clientToListFiles.remove(clientInfo);
    }

    @Override
    public Set<Integer> getIdFilesOnClients(@NotNull ClientInfo clientInfo) {
        return clientToListFiles.get(clientInfo);
    }

    @Override
    public Set<ClientInfo> getClients() {
        return clientToListFiles.keySet(); // unmodif ???
    }

    @Override
    public void updateSharedFiles(ClientInfo clientInfo, List<Integer> idFiles) {

    }
}
