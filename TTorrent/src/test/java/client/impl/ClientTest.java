package client.impl;

import client.Downloader;
import client.api.IClientFile;
import client.api.IState;
import common.Common;
import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class ClientTest {
    private static final int PORT = 20004;
    private File myFile1;

    @SuppressWarnings("FieldCanBeLocal")
    private File myFile2;

    private ClientLogicImpl myServer;
    private Thread myServerThread;

    @Rule
    public final TemporaryFolder folderRule = new TemporaryFolder() {
        @Override
        protected void before() throws Throwable {
            super.before();
            myFile1 = newFile("file1");
            myFile2 = newFile("file2");

            final String longString = CharBuffer.allocate(Common.PATH_OF_FILE_SIZE)
                    .toString().replace('\0', 'a') +
                    CharBuffer.allocate(Common.PATH_OF_FILE_SIZE)
                            .toString().replace('\0', 'b') +
                    CharBuffer.allocate(Common.PATH_OF_FILE_SIZE)
                            .toString().replace('\0', 'c');

            FileUtils.write(myFile1, "HelloWorld", "UTF-8");
            FileUtils.write(myFile2, longString, "UTF-8");
            final Set<Integer> parts1 = Stream.iterate(0, i -> i + 1)
                    .limit(Downloader.partCount(myFile1.length()))
                    .collect(Collectors.toSet());
            final Set<Integer> parts2 = Stream.iterate(0, i -> i + 1)
                    .limit(Downloader.partCount(myFile2.length()))
                    .collect(Collectors.toSet());

            final Map<String, IClientFile> files = new HashMap<>();
            files.put(myFile1.toPath().toAbsolutePath().toString(),
                    new ClientFileInfo(1, myFile1.length(), parts1));
            files.put(myFile2.toPath().toAbsolutePath().toString(),
                    new ClientFileInfo(2, myFile2.length(), parts2));
            final IState state = new StateClient(files);
            myServer = new ClientLogicImpl(PORT, state);
            myServerThread = new Thread(() -> myServer.start());
            myServerThread.start();
        }

        @Override
        protected void after() {
            super.after();
            try {
                myServer.shutdown();
                myServerThread.join();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Test
    public void simpleStatRequest() throws IOException, ClassNotFoundException {
        final OtherClientRequest client =
                new OtherClientRequest(InetAddress.getLoopbackAddress(), PORT);
        final Set<Integer> stat = client.stat(1);
        assertEquals(Downloader.partCount(myFile1.length()), stat.size());
    }

    @Test
    public void simpleGetRequest() throws IOException {
        final OtherClientRequest client =
                new OtherClientRequest(InetAddress.getLoopbackAddress(), PORT);
        final File out = folderRule.newFile("out");

        client.get(1, 0, out.toPath());

        final String content = FileUtils.readFileToString(out, "UTF-8");
        assertEquals("HelloWorld", content);
    }

    @Test
    public void getWithOffsetGetRequest() throws IOException {
        final OtherClientRequest client =
                new OtherClientRequest(InetAddress.getLoopbackAddress(), PORT);
        final File out = folderRule.newFile("out");

        client.get(2, 2, out.toPath());

        final String content = FileUtils.readFileToString(out, "UTF-8");
        assertEquals(3 * Common.PATH_OF_FILE_SIZE, content.length());
        assertEquals('c', content.charAt(content.length() - 1));
    }
}
