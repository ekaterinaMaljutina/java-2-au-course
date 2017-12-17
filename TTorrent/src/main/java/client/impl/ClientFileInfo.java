package client.impl;

import client.api.IClientFile;
import common.Common;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ClientFileInfo implements IClientFile, Serializable {

    private static final Logger LOGGER =
            LogManager.getLogger(ClientFileInfo.class);

    private final int idFile;
    private final long sizeFile;
    private final Set<Integer> partsInClient = new HashSet<>();

    public ClientFileInfo(int idFile, long sizeFile, Set<Integer> parts) {
        this.idFile = idFile;
        this.sizeFile = sizeFile;
        parts.forEach(this::addPartOfFile);
    }

    @Override
    public int getId() {
        return idFile;
    }

    @Override
    public long getSize() {
        return sizeFile;
    }

    @Override
    public boolean isDownloaded() {
        return partsInClient.size() * Common.PATH_OF_FILE_SIZE >= sizeFile;
    }

    @Override
    public boolean addPartOfFile(int idPart) {
        if (idPart * Common.PATH_OF_FILE_SIZE < sizeFile) {
//            LOGGER.debug("add part of file idPart" + idPart);
            return partsInClient.add(idPart);
        }
        return false;
    }

    @Override
    public Set<Integer> getParts() {
        return partsInClient;
    }
}
