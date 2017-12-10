package common.commands.server;

import client.api.IClientInfo;
import common.files.IFileInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;

public class RequestToServer implements IRequestToServer {

    private static final Logger LOGGER = LogManager.getLogger(RequestToServer.class);

    private final InetAddress addressServer;
    private final int portServer;

    public RequestToServer(@NotNull InetAddress addressServer, int portServer) {
        this.addressServer = addressServer;
        this.portServer = portServer;
    }

    @NotNull
    @Override
    public Map<Integer, IFileInfo> getListFiles() throws IOException {
        LOGGER.info("send to list request");
        return null;
    }

    @Override
    public int upload(@NotNull IFileInfo fileInfo) throws IOException {
        LOGGER.info("send to upload request");
        return 0;
    }

    @Override
    public List<IClientInfo> sources(int idFile) throws IOException {
        LOGGER.info("send to sources request");
        return null;
    }

    @Override
    public boolean update(int port, int[] idFiles) throws IOException {
        LOGGER.info("send to update request");
        return false;
    }
}
