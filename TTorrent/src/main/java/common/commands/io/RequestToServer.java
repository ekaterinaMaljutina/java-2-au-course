package common.commands.io;

import client.api.ClientInfo;
import common.commands.request.ListRequest;
import common.commands.request.SourcesRequest;
import common.commands.request.UpdateRequest;
import common.commands.request.UploadRequest;
import common.commands.response.ListResponse;
import common.commands.response.SourcesResponse;
import common.commands.response.UpdateResponse;
import common.commands.response.UploadResponse;
import common.files.FileInfoImpl;
import common.files.IFileInfo;
import common.nio.QueryReader;
import common.nio.QueryWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public Map<Integer, IFileInfo> getListFiles() throws IOException, ClassNotFoundException {
        LOGGER.info("send to list request");
        try (Socket socket = new Socket(addressServer, portServer)) {
            ListRequest request = new ListRequest();
            QueryWriter.writeMessage(socket, request);
            ListResponse response = QueryReader.readQuery(socket);
            LOGGER.info("get response list");
            Map<Integer, IFileInfo> result = new HashMap<>();
            response.getListFiles().forEach(itemFile -> {
                result.put(itemFile.getId(),
                        new FileInfoImpl(itemFile.getName(),
                                itemFile.getSize()));
            });
            return result;
        }
    }

    @Override
    public int upload(@NotNull IFileInfo fileInfo) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(addressServer, portServer)) {
            LOGGER.info("send to upload request");
            UploadRequest request = new UploadRequest(fileInfo.getName(), fileInfo.getSize());
            QueryWriter.writeMessage(socket, request);
            LOGGER.info("get upload response");
            UploadResponse response = QueryReader.readQuery(socket);
            return response.getIdFile();
        }
    }

    @Override
    public List<ClientInfo> sources(int idFile) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(addressServer, portServer)) {
            LOGGER.info("send to sources request");
            SourcesRequest request = new SourcesRequest(idFile);
            QueryWriter.writeMessage(socket, request);
            SourcesResponse response = QueryReader.readQuery(socket);
            LOGGER.info("get sources response");
            return response.getClientSourceList();
        }
    }

    @Override
    public boolean update(int port, Set<Integer> idFiles) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(addressServer, portServer)) {
            LOGGER.info("send to update request");
            UpdateRequest request = new UpdateRequest(port, idFiles);
            QueryWriter.writeMessage(socket, request);
            UpdateResponse response = QueryReader.readQuery(socket);
            return response.isStatus();
        }
    }
}
