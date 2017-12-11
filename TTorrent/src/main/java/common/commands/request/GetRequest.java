package common.commands.request;

import java.io.Serializable;

public class GetRequest implements Serializable {
    private final int idFile;
    private final int idPartOfFile;

    public GetRequest(int idFile, int idPartOfFile) {
        this.idFile = idFile;
        this.idPartOfFile = idPartOfFile;
    }

    public int getIdFile() {
        return idFile;
    }

    public int getIdPartOfFile() {
        return idPartOfFile;
    }

    @Override
    public String toString() {
        return "GetRequest{" +
                "idFile=" + idFile +
                ", idPartOfFile=" + idPartOfFile +
                '}';
    }
}
