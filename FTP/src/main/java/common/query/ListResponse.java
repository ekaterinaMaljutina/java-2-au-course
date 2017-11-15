package common.query;

import java.util.LinkedList;
import java.util.List;

public class ListResponse extends Query {

    private final List<ItemList> pathList = new LinkedList<>();

    public List<ItemList> getPathList() {
        return pathList;
    }

    @Override
    public int getQueryType() {
        return TypeQuery.LIST_QUERY;
    }

    @Override
    public String toString() {
        return " size : " + pathList.size() +
                (pathList.size() > 0 ? pathList : "");
    }
}
