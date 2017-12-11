package common.commands.response;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class SourcesResponse implements Serializable {
    private final List<ClientSource> clientSourceList;

    public SourcesResponse(List<ClientSource> clientSourceList) {
        this.clientSourceList = clientSourceList;
    }

    public List<ClientSource> getClientSourceList() {
        return clientSourceList;
    }

    @Override
    public String toString() {
        return "SourcesResponse{" +
                "clientSourceList=" + clientSourceList +
                '}';
    }

    public static class ClientSource {
        private final byte[] api;
        private final int port;

        public ClientSource(byte[] api, int port) {
            this.api = api;
            this.port = port;

        }

        public byte[] getApi() {
            return api;
        }

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
