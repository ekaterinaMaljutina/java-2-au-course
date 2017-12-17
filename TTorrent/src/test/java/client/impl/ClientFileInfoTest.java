package client.impl;

import common.Common;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

public class ClientFileInfoTest {
    public static ClientFileInfo create(int id, long size, Integer... value) {
        return new ClientFileInfo(id, Common.PATH_OF_FILE_SIZE * size,
                new HashSet<>(Arrays.asList(value)));
    }

    @Test
    public void addDuplicatedParts() {
        final ClientFileInfo info = create(1, 11, 1, 2, 3, 4);

        assertEquals(4, info.getParts().size());
        info.addPartOfFile(10);
        assertEquals(5, info.getParts().size());
        info.addPartOfFile(10);
        assertEquals(5, info.getParts().size());
    }

    @Test
    public void testAddOutOfRangePartNumber() {
        final ClientFileInfo info = create(1, 4, 1, 2, 3, 4);

        assertFalse(info.addPartOfFile(2));
        assertFalse(info.addPartOfFile(10));
    }

    @Test
    public void loadedEq() {
        final ClientFileInfo info = create(1, 4, 0, 1, 2, 3);
        assertTrue(info.isDownloaded());
    }

    @Test
    public void loadedGt() {
        final ClientFileInfo info = create(1, 4 + 1, 0, 1, 2);
        assertFalse(info.isDownloaded());
        info.addPartOfFile(3);
        assertFalse(info.isDownloaded());
        info.addPartOfFile(4);
        assertTrue(info.isDownloaded());
    }

}

