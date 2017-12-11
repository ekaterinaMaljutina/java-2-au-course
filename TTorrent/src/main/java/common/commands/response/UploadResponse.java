package common.commands.response;

import java.io.Serializable;

public class UploadResponse implements Serializable{
    private final int idFile;

    public UploadResponse(int idFile) {
        this.idFile = idFile;
    }

    public int getIdFile() {
        return idFile;
    }

    @Override
    public String toString() {
        return "UploadResponse{" +
                "idFile=" + idFile +
                '}';
    }
}
