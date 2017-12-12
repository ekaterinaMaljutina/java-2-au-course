package common.commands.request;

import java.io.Serializable;
import java.util.Set;

public class UpdateRequest implements Serializable {
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
}
