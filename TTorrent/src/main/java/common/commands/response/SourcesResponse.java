package common.commands.response;

import client.api.ClientInfo;

import java.io.Serializable;
import java.util.Arrays;
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

    public static class ClientSource implements ClientInfo {
        private final byte[] api;
        private final int port;

        public ClientSource(byte[] api, int port) {
            this.api = api;
            this.port = port;

        }

        @Override
        public byte[] getIpAddress() {
            return api;
        }

        @Override
        public int getPort() {
            return port;
        }

        @Override
        public String toString() {
            return "{" +
                    "api='" + Arrays.toString(api) + '\'' +
                    ", port=" + port +
                    "};";
        }
    }
}
