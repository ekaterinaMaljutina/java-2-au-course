package common.commands.request;

import java.io.Serializable;
import java.util.List;

public class UpdateRequest implements Serializable {
    private final int port;
    private final List<Integer> idFiles;

    public UpdateRequest(int port, List<Integer> idFiles) {
        this.port = port;
        this.idFiles = idFiles;
    }

    public int getPort() {
        return port;
    }

    public List<Integer> getIdFiles() {
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
