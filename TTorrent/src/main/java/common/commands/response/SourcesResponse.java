package common.commands.response;

import java.io.Serializable;
import java.util.List;

public class SourcesResponse implements Serializable {
    private final List<ClientSource> clientSourceList;

    public SourcesResponse(List<ClientSource> clientSourceList) {
        this.clientSourceList = clientSourceList;
    }

    public List<ClientSource> getClientSourceList() {
        return clientSourceList;
    }

    public static class ClientSource {
        private final String api;
        private final int port;

        public ClientSource(String api, int port) {
            this.api = api;
            this.port = port;

        }

        public String getApi() {
            return api;
        }

        public int getPort() {
            return port;
        }

        @Override
        public String toString() {
            return "{" +
                    "api='" + api + '\'' +
                    ", port=" + port +
                    "};";
        }
    }

    @Override
    public String toString() {
        return "SourcesResponse{" +
                "clientSourceList=" + clientSourceList +
                '}';
    }
}
