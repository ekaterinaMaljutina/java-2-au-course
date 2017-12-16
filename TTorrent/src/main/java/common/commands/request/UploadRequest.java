package common.commands.request;

import common.commands.io.IdRequestToServer;

import java.io.Serializable;

public class UploadRequest implements Request, Serializable {

    private final String nameFile;
    private final long size;

    public UploadRequest(String nameFile, long size) {
        this.nameFile = nameFile;
        this.size = size;
    }

    public String getNameFile() {
        return nameFile;
    }

    public long getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "UploadRequest{" +
                "nameFile='" + nameFile + '\'' +
                ", size=" + size +
                '}';
    }

    @Override
    public Integer getId() {
        return IdRequestToServer.UPLOAD_REQUEST;
    }
}
