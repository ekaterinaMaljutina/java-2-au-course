package common.commands.request;

import common.commands.io.IdRequestToServer;

import java.io.Serializable;

public class SourcesRequest implements Request, Serializable {
    private final int idFile;

    public SourcesRequest(int idFile) {
        this.idFile = idFile;
    }

    public int getIdFile() {
        return idFile;
    }

    @Override
    public String toString() {
        return "SourcesRequest{" +
                "idFile=" + idFile +
                '}';
    }

    @Override
    public Integer getId() {
        return IdRequestToServer.SOURCES_REQUEST;
    }
}
