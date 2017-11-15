package server.net;

import common.exception.AlreadyConnectionException;
import common.exception.NotConnectionException;
import common.nio.ReaderObject;
import common.nio.WriterObject;
import common.query.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class FTPServer implements Server {

    private final List<Thread> clientThreads = new LinkedList<>();
    private final ReaderObject readerObject = new ReaderObject();
    private final WriterObject writerObject = new WriterObject();

    private ServerSocket socket;
    private Thread threadAcceptClient;

    @Override
    public void start(int port, @NotNull String address)
            throws AlreadyConnectionException, IOException {

        if (socket != null) {
            throw new AlreadyConnectionException();
        }
        SocketAddress socketAddress = new InetSocketAddress(address, port);
        socket = new ServerSocket();
        socket.bind(socketAddress);
        threadAcceptClient = new Thread(this::getClient);
        threadAcceptClient.start();
    }

    @Override
    public void stop() throws NotConnectionException {
        if (socket == null) {
            throw new NotConnectionException();
        }

        clientThreads.forEach(Thread::interrupt);
        clientThreads.clear();
        threadAcceptClient.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket = null;

    }

    @Override
    public boolean isRunning() {
        return socket != null;
    }

    private void getClient() {
        while (!Thread.interrupted()) {
            Socket socketClient;
            try {
                socketClient = socket.accept();
            } catch (IOException e) {
                break;
            }
            if (socketClient == null) {
                continue;
            }
            System.out.println(String.format("accept client: %s %d",
                    socketClient.getLocalAddress(), socketClient.getPort()));

            Thread threadClient = new Thread(() -> processClient(socketClient));
            clientThreads.add(threadClient);
            threadClient.start();
        }
    }

    private void processClient(@NotNull Socket clientSocket) {
        while (!Thread.interrupted()) {
            try {
                if (clientSocket.isClosed()) {
                    return;
                }
                Query query = receive(clientSocket);
                if (queryToProcess(query, clientSocket) == -1) {
                    tryCloseSocket(clientSocket);
                    return;
                }

            } catch (NotConnectionException ignore) {
                return;
            }
        }
    }

    private void getListResponse(@NotNull String path, @NotNull Socket
            socketClient) {
        System.out.println("get query - list - path " + path);
        Path pathToDir = Paths.get(path);
        ListResponse response = new ListResponse();
        if (Files.notExists(pathToDir) || !Files.isDirectory(pathToDir)) {
            System.out.println("\t dir don't  exists or not dir ");
        } else {
            try (Stream<Path> paths = Files.list(pathToDir)) {
                paths.forEach(path1 ->
                        response.getPathList()
                                .add(new ItemList(path1.getFileName().toString(),
                                        Files.isDirectory(path1))));
            } catch (IOException ex) {
                System.out.println(" get ERROR " + ex.getMessage());
            }
        }
        send(response, socketClient);
    }

    private void getGetResponse(@NotNull String path, @NotNull Socket
            socketClient) {
        System.out.println("get query - get - path " + path);
        GetResponse response = new GetResponse(new byte[0]);
        Path pathToFile = Paths.get(path);
        if (Files.notExists(pathToFile) && !Files.isDirectory(pathToFile)) {
            System.out.println("\t file don't  exists or is dir ");
            send(response, socketClient);
            return;
        }

        try {
            response = new GetResponse(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            System.out.println("get ERROR " + e.getMessage());
        }
        send(response, socketClient);
    }

    private void send(@NotNull Query query, @NotNull Socket socketClient) {
        try {
            writerObject.writeMessage(socketClient, query);
        } catch (NotConnectionException e) {
            System.out.println("Not send message");
        }
    }

    private @Nullable
    Query receive(@NotNull Socket clientSocket) throws
            NotConnectionException {
        try {
            return readerObject.readQuery(clientSocket);
        } catch (NotConnectionException ex) {
            tryCloseSocket(clientSocket);
        }
        return null;
    }

    private int queryToProcess(@Nullable Query query, @NotNull Socket
            socketClient) {
        if (query == null) {
            return 0;
        }

        switch (query.getQueryType()) {
            case TypeQuery.LIST_QUERY:
                getListResponse(((ListRequest) query).getPath(), socketClient);
                return 0;
            case TypeQuery.GET_QUERY:
                getGetResponse(((GetRequest) query).getPath(), socketClient);
                return 0;
            case TypeQuery.DISCONNECT_QUERY:
                return -1;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void tryCloseSocket(@NotNull Socket clientSocket)
            throws NotConnectionException {
        try {
            clientSocket.close();
            System.out.println(String.format(" close socket: %s %d ",
                    clientSocket.getInetAddress().toString(),
                    clientSocket.getPort()));
        } catch (IOException ignore) {
            System.out.println(String.format(" not close socket: %s %d ",
                    clientSocket.getInetAddress().toString(),
                    clientSocket.getPort()));
            throw new NotConnectionException();
        }
    }
}
