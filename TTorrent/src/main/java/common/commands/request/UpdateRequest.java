package common.commands.request;

import common.commands.io.IdRequestToServer;

import java.io.Serializable;
import java.util.Set;

public class UpdateRequest implements Request,Serializable {
    private final int port;
    private final Set<Integer> idFiles;

    public UpdateRequest(int port, Set<Integer> idFiles) {
        this.port = port;
        this.idFiles = idFiles;
    }

    public int getPort() {
        return port;
    }

    public Set<Integer> getIdFiles() {
        return idFiles;
    }

    @Override
    public String toString() {
        return "UpdateRequest{" +
                "port=" + port +
                ", idFiles=" + idFiles +
                '}';
    }

    @Override
    public Integer getId() {
        return IdRequestToServer.UPDATE_REQUEST;
    }
}
