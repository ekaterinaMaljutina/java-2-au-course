package common.query;

public class GetRequest extends Query {

    private final String path;

    public GetRequest(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int getQueryType() {
        return TypeQuery.GET_QUERY;
    }
}
