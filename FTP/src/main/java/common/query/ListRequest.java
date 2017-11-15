package common.query;

public class ListRequest extends Query {
    private final String path;

    public ListRequest(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int getQueryType() {
        return TypeQuery.LIST_QUERY;
    }
}
