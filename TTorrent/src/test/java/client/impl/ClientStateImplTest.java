package client.impl;

import client.api.IState;
import common.Common;
import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClientStateImplTest {

    @Test
    public void addSameFileTest() {
        final IState instance = new StateClient(Collections.emptyMap());
        assertTrue(instance.newFile("File",
                new ClientFileInfo(1, 200, Collections.emptySet())));
        assertFalse(instance.newFile("File",
                new ClientFileInfo(1, 200, Collections.emptySet())));
        assertFalse(instance.newFile("file2",
                new ClientFileInfo(1, 2000, Collections.emptySet())));
    }

    @Test
    public void listenerTest() {
        final IState instance = new StateClient(Collections.emptyMap());

        final AtomicBoolean called = new AtomicBoolean(false);
        instance.addClientListener(state -> called.set(true));

        instance.getListIdFiles();
        instance.getFileInfoById(0);
        instance.getFileInfoById(0);
        instance.getAllFilesWithInfo();
        instance.getAllFiles();
        assertFalse(called.get());

        final String path = "file";
        instance.newFile(path, new ClientFileInfo(1, 100 * Common.PATH_OF_FILE_SIZE, Collections.emptySet()));
        assertTrue(called.get());
        called.set(false);

        instance.partOfFile(path, 0);
        assertTrue(called.get());

        called.set(false);
        instance.partOfFile(path, 0);
        assertFalse(called.get());
    }
}
