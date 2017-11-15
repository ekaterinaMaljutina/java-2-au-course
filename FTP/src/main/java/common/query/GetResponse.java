package common.query;

import java.util.Arrays;

public class GetResponse extends Query {
    private final byte[] bytes;

    public GetResponse(byte[] bytes) {
        this.bytes = bytes;
    }

    public int getSize() {
        return bytes.length;
    }

    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public int getQueryType() {
        return TypeQuery.GET_QUERY;
    }

    @Override
    public String toString() {
        return "size:  " + bytes.length +
                (bytes.length > 0 ? " content " + Arrays.toString(bytes) : "");
    }
}
