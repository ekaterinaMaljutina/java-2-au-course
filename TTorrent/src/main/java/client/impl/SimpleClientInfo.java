package client.impl;

import client.api.ClientInfo;

import java.io.Serializable;
import java.util.Arrays;

public class SimpleClientInfo implements ClientInfo, Serializable {
    private final byte[] ipAddress;
    private final int port;

    public SimpleClientInfo(byte b1, byte b2, byte b3, byte b4, int port) {
        ipAddress = new byte[]{b1, b2, b3, b4};
        this.port = port;
    }

    public SimpleClientInfo(byte[] ip, int port) {
        assert ip.length == 4; // ipv4
        ipAddress = ip;
        this.port = port;
    }

    @Override
    public byte[] getIpAddress() {
        return ipAddress;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientInfo)) return false;

        ClientInfo that = (ClientInfo) o;

        return getPort() == that.getPort() && Arrays.equals(getIpAddress(), that.getIpAddress());
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(getIpAddress());
        result = 31 * result + getPort();
        return result;
    }

    @Override
    public String toString() {
        return "SimpleClientInfo{" +
                "ipAddress=" + Arrays.toString(ipAddress) +
                ", port=" + port +
                '}';
    }
}
