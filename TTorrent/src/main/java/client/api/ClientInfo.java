package client.api;

import java.io.Serializable;

public interface ClientInfo extends Serializable {

    byte[] getIpAddress();

    int getPort();
}
