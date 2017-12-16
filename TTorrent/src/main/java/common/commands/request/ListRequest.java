package common.commands.request;

import common.commands.io.IdRequestToServer;

import java.io.Serializable;

public class ListRequest implements Request, Serializable {

    @Override
    public Integer getId() {
        return IdRequestToServer.LIST_REQUEST;
    }
}
