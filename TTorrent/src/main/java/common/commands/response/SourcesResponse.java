package common.commands.response;

import client.api.ClientInfo;

import java.io.Serializable;
import java.util.List;

public class SourcesResponse implements Serializable {
    private final List<ClientInfo> clientSourceList;

    public SourcesResponse(List<ClientInfo> clientSourceList) {
        this.clientSourceList = clientSourceList;
    }

    public List<ClientInfo> getClientSourceList() {
        return clientSourceList;
    }

    @Override
    public String toString() {
        return "SourcesResponse{" +
                "clientSourceList=" + clientSourceList +
                '}';
    }
}
