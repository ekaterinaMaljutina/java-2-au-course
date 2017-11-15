package common.query;

import java.io.Serializable;

public abstract class Query implements Serializable {

    public abstract int getQueryType();
}
