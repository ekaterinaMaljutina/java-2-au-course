package common.query;

public class DisconnectRequest extends Query {

    @Override
    public int getQueryType() {
        return TypeQuery.DISCONNECT_QUERY;
    }
}
