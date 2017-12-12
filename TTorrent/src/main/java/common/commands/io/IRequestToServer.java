package common.commands.io;


import client.api.ClientInfo;
import common.files.IFileInfo;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRequestToServer {
    @NotNull
    Map<Integer, IFileInfo> getListFiles() throws IOException, ClassNotFoundException;

    int upload(@NotNull IFileInfo fileInfo) throws IOException, ClassNotFoundException;

    List<ClientInfo> sources(int idFile) throws IOException, ClassNotFoundException;

    boolean update(int port, Set<Integer> idFiles) throws IOException, ClassNotFoundException;


}
