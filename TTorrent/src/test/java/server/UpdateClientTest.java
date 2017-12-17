package server;

import client.api.ClientInfo;
import client.impl.SimpleClientInfo;
import common.Common;
import common.files.FileInfoImpl;
import common.files.IFileInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import server.impl.SharedFiles;
import server.impl.StateServer;

import java.sql.Time;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;


public class UpdateClientTest {
    private StateServer state;

    @Before
    public void before() {
        Map<Integer, IFileInfo> map = new HashMap<>();
        map.put(1, new FileInfoImpl("first", 10));
        map.put(2, new FileInfoImpl("second", 20));
        map.put(3, new FileInfoImpl("third", 30));
        map.put(4, new FileInfoImpl("four", 40));
        state = new StateServer(new SharedFiles(map));
    }

    @Test
    public void setNextExecution() {
        ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);

        UpdateClient refresher = new UpdateClient(executorService, state);
        refresher.run();

        verify(executorService).schedule(eq(refresher), anyLong(), any());
    }

    @Test
    public void correctReleaseClient() {
        ClientInfo client1 = new SimpleClientInfo((byte) 1, (byte) 1, (byte) 1, (byte) 1, 1000);
        ClientInfo client2 = new SimpleClientInfo((byte) 1, (byte) 1, (byte) 1, (byte) 2, 1000);
        state.updateSharedFiles(client1, new HashSet<>(Arrays.asList(1, 2)));
        state.updateSharedFiles(client2, new HashSet<>(Arrays.asList(1, 4)));
        state.changedLastUpdateFile(client1,
                new Time(System.currentTimeMillis() -
                        Common.UPDATE_CLIENT_FILES / 2));
        state.changedLastUpdateFile(client2,
                new Time(System.currentTimeMillis() -
                        TimeUnit.DAYS.toMillis(1)));

        ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);

        new UpdateClient(executorService, state).run();

        Assert.assertEquals(1, state.getClients().size());
        Assert.assertEquals(client1, state.getClients().iterator().next());
    }

    @Test
    public void setCorrectDelay() {
        ClientInfo client1 = new SimpleClientInfo((byte) 1, (byte) 1, (byte) 1, (byte) 1, 1000);
        state.updateSharedFiles(client1, new HashSet<>(Arrays.asList(1, 2)));
        state.changedLastUpdateFile(client1,
                new Time(System.currentTimeMillis() -
                        Common.UPDATE_CLIENT_FILES / 2));
        ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);

        final UpdateClient refresher = new UpdateClient(executorService, state);
        refresher.run();

        verify(executorService).schedule(eq(refresher),
                longThat(delay -> delay < Common.UPDATE_CLIENT_FILES), any());
    }
}
