package ru.mit.spbau;

import client.net.Client;
import client.net.FTPClient;
import common.query.GetResponse;
import common.query.ItemList;
import common.query.ListResponse;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import server.net.FTPServer;
import server.net.Server;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class ServerClientTest {

    private static final int port = 10050;
    private static final String host = "localhost";

    private static final Server server = new FTPServer();
    private static final Client client1 = new FTPClient();
    private static final Client client2 = new FTPClient();


    @BeforeClass
    public static void start() {
        try {
            server.start(port, host);
            Assert.assertTrue(server.isRunning());

            client1.connect(port, host);
            Assert.assertTrue(client1.isRunning());

            client2.connect(port, host);
            Assert.assertTrue(client1.isRunning());
        } catch (Exception ex) {
            assert false;
        }
    }


    @AfterClass
    public static void stop() {
        try {
            client2.disconnect();
            client1.disconnect();
            server.stop();
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    public void serverClientListTest() throws Exception {
        File serverDir = Paths.get(System.getProperty("user.dir"), "tmp")
                .toFile();
        if (serverDir.exists()) {
            FileUtils.deleteDirectory(serverDir);
        }
        assertTrue(serverDir.mkdir());

        File tempFile = File.createTempFile("testFile", ".txt", serverDir);
        ListResponse response = client1.executeList(serverDir.toString());
        assertFalse(response.getPathList().contains(new ItemList(serverDir
                .toString(),
                true)));
        assertTrue(response.getPathList().contains(new ItemList(tempFile.getName(),
                false)));
        assertEquals(1, response.getPathList().size());

        FileUtils.deleteDirectory(serverDir);
    }

    @Test
    public void serverClientGetTest() throws Exception {
        File serverDir = Paths.get(System.getProperty("user.dir"), "tmp")
                .toFile();
        if (serverDir.exists()) {
            FileUtils.deleteDirectory(serverDir);
        }
        assertTrue(serverDir.mkdir());

        File tempFile = File.createTempFile("testFile", ".txt", serverDir);
        PrintStream fileStream = new PrintStream(tempFile);
        fileStream.println("1 2 3 4 5 6 7 8");
        fileStream.close();

        GetResponse response = client2.executeGet(tempFile.toString());
        Assert.assertTrue(response.getSize() != 0);
        byte[] expectedByte = Files.readAllBytes(tempFile.toPath());
        Assert.assertEquals(expectedByte.length, response.getSize());
        for (int i = 0; i < expectedByte.length; i++) {
            Assert.assertEquals(expectedByte[i], response.getBytes()[i]);
        }

        FileUtils.deleteDirectory(serverDir);
    }
}


