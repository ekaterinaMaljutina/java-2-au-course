package common.commands.request;

import java.io.Serializable;

public class SourcesRequest implements Serializable {
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
}
