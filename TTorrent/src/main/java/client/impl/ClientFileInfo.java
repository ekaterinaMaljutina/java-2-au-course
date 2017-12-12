package client.impl;

import client.api.IClientFile;
import common.files.FilesCommon;

import java.io.Serializable;
import java.util.Set;

public class ClientFileInfo implements IClientFile, Serializable {

    private final int idFile;
    private final long sizeFile;
    private final Set<Integer> partsInClient;

    public ClientFileInfo(int idFile, long sizeFile, Set<Integer> parts) {
        this.idFile = idFile;
        this.sizeFile = sizeFile;
        partsInClient = parts;
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
        return partsInClient.size() * FilesCommon.PATH_OF_FILE_SIZE >= sizeFile;
    }

    @Override
    public boolean addPartOfFile(int idPart) {
        return !(partsInClient.contains(idPart)
                || idPart * FilesCommon.PATH_OF_FILE_SIZE >= sizeFile)
                && partsInClient.add(idPart);
    }

    @Override
    public Set<Integer> getParts() {
        return partsInClient;
    }
}
