package common.commands.response;

import java.io.Serializable;

public class UpdateResponse implements Serializable {
    private final boolean status;

    public UpdateResponse(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "UpdateResponse{" +
                "status=" + status +
                '}';
    }
}
