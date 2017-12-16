package common.commands.request;

import common.commands.handlers.client.IdRequestToClient;

import java.io.Serializable;

public class StatRequest implements Request,Serializable {
    private final int idFile;

    public StatRequest(int idFile) {
        this.idFile = idFile;
    }

    public int getIdFile() {
        return idFile;
    }

    @Override
    public String toString() {
        return "StatRequest{" +
                "idFile=" + idFile +
                '}';
    }

    @Override
    public Integer getId() {
        return IdRequestToClient.REQUEST_STAT;
    }
}
