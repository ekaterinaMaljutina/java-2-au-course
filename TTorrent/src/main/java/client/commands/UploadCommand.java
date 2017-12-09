package client.commands;

import client.state.api.IState;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;

public class UploadCommand implements ICommand {

    private IRequestToServer server;

    public UploadCommand(@NotNull InetAddress addressServer, int port) {
        server = new RequestToServer(addressServer, port);
    }

    @Override
    public void runCommand(@NotNull IState state, String[] args) {
        
        //        server.upload();
    }

    @Override
    public String commandName() {
        return "Upload Request To Server";
    }
}
