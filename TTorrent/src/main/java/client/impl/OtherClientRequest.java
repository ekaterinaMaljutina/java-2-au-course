package client.impl;

import client.api.IClientRequest;
import common.commands.request.GetRequest;
import common.commands.request.StatRequest;
import common.commands.response.StatResponse;
import common.nio.QueryReader;
import common.nio.QueryWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Set;

public class OtherClientRequest implements IClientRequest {

    private final InetAddress address;
    private final int port;

    public OtherClientRequest(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public Set<Integer> stat(int id) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(address, port)) {
            StatRequest request = new StatRequest(id);
            QueryWriter.writeMessage(socket, request);
            StatResponse response = QueryReader.readQuery(socket);
            return response.getIdFiles();
        }
    }

    @Override
    public boolean get(int id, int partNumber, @NotNull Path out) throws IOException {
        try (Socket socket = new Socket(address, port)) {
            GetRequest request = new GetRequest(id, partNumber);
            QueryWriter.writeMessage(socket, request);
            QueryReader.readContext(socket, out, partNumber);
            return true;
        }
    }
}
