package common.commands.io;


import client.api.ClientInfo;
import common.files.IFileInfo;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IRequestToServer {
    @NotNull
    Map<Integer, IFileInfo> getListFiles() throws IOException;

    int upload(@NotNull IFileInfo fileInfo) throws IOException;

    List<ClientInfo> sources(int idFile) throws IOException;

    boolean update(int port, int[] idFiles) throws IOException;


}
