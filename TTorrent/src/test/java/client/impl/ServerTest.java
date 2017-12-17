package client.impl;

import common.commands.io.RequestToServer;
import common.files.FileInfoImpl;
import common.files.IFileInfo;
import org.junit.BeforeClass;
import org.junit.Test;
import server.MainLoopServer;
import server.impl.SharedFiles;
import server.impl.StateServer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ServerTest {
    private static final int PORT = 20003;
    private static final IFileInfo FILE1 = new FileInfoImpl("name", 100);
    private static final IFileInfo FILE2 = new FileInfoImpl("name2", 1000);
    private static final IFileInfo FILE3 = new FileInfoImpl("name3", 10000);
    private static final IFileInfo FILE4 = new FileInfoImpl("name", 100000);
    private static MainLoopServer myServer;

    @BeforeClass
    public static void before() throws InterruptedException, IOException {
        final Map<Integer, IFileInfo> fileMap = new HashMap<>();
        fileMap.put(1, FILE1);
        fileMap.put(2, FILE2);
        fileMap.put(3, FILE3);
        fileMap.put(4, FILE4);
        final SharedFiles files = new SharedFiles(fileMap);
        final StateServer state = new StateServer(files);
        myServer = new MainLoopServer(PORT, state);
        new Thread(() -> {
            try {
                myServer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Test
    public void listRequest() throws IOException, InterruptedException, ClassNotFoundException {
        Thread.sleep(1000);
        final RequestToServer server = new RequestToServer(InetAddress.getLoopbackAddress(), PORT);
        final Map<Integer, IFileInfo> list = server.getListFiles();

        assertTrue(list.containsKey(1));
        assertTrue(list.containsKey(2));
        assertTrue(list.containsKey(3));
        assertTrue(list.containsKey(4));

        assertTrue(list.containsValue(FILE1));
        assertTrue(list.containsValue(FILE2));
        assertTrue(list.containsValue(FILE3));
        assertTrue(list.containsValue(FILE4));
    }

    @Test
    public void uploadRequest() throws IOException, ClassNotFoundException {
        final RequestToServer server = new RequestToServer(InetAddress.getLoopbackAddress(), PORT);
        final IFileInfo file = new FileInfoImpl("myFile", 10003);
        final int id = server.upload(file);

        assertFalse(Arrays.asList(1, 2, 3, 4).contains(id));

        assertTrue(server.getListFiles().containsValue(file));
    }

    @Test
    public void updateAndSourcesRequests() throws IOException, ClassNotFoundException {
        final RequestToServer server = new RequestToServer(InetAddress.getLoopbackAddress(), PORT);

        final boolean update = server.update(30000,
                new HashSet<>(Arrays.asList(1, 2, 3)));

        assertTrue(update);
        assertTrue(server.sources(1).stream().anyMatch(clientInfo -> clientInfo.getPort() == 30000));
        assertTrue(server.sources(2).stream().anyMatch(clientInfo -> clientInfo.getPort() == 30000));
        assertTrue(server.sources(3).stream().anyMatch(clientInfo -> clientInfo.getPort() == 30000));
        assertFalse(server.sources(100).stream().anyMatch(clientInfo -> clientInfo.getPort() == 30000));
    }
}
