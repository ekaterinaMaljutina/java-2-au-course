package server.impl;


import common.files.FileInfoImpl;
import common.files.IFileInfo;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SharedFilesTest {

    @Test
    public void exists() {
        Map<Integer, IFileInfo> map = new HashMap<>();
        map.put(1, new FileInfoImpl("name1", 10));
        SharedFiles serverState = new SharedFiles(map);

        assertTrue(serverState.checkExistFile(1));
        assertFalse(serverState.checkExistFile(2));
    }

    @Test
    public void getInfo() {
        Map<Integer, IFileInfo> map = new HashMap<>();
        map.put(1, new FileInfoImpl("name1", 10));
        SharedFiles serverState = new SharedFiles(map);

        assertEquals(new FileInfoImpl("name1", 10), serverState.getFileInfo(1));
    }

    @Test
    public void putNewFile() {
        Map<Integer, IFileInfo> map = new HashMap<>();
        map.put(1, new FileInfoImpl("name1", 10));
        SharedFiles serverState = new SharedFiles(map);

        assertNotEquals(1, serverState.newFile(new FileInfoImpl("name2", 100)));
        assertEquals(2, serverState.getFiles().size());
    }
}
