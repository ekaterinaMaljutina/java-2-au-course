package common.commands.request;

import java.io.Serializable;

public class StatRequest implements Serializable {
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
}
