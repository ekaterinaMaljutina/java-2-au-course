package client;

import client.api.IClientLogic;
import client.state.StateClient;
import client.state.api.IState;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;

public class MainClient {

    private static final Logger LOGGER = LogManager.getLogger(MainClient.class);
    private static final String DEFAULT_ADDRESS = "localhost";
    private static final Integer DEFAULT_SERVER_PORT = 8081;
    private static final Integer DEFAULT_CLIENT_PORT = 8082;


    public static void main(String[] args) {
        ArgumentParser parser = initArgumentParser();

        try {
            InetAddress address = InetAddress.getByName(DEFAULT_ADDRESS);


            // load last client state
            // Here load old state client
            IState state = new StateClient(Collections.emptyMap());

            IClientLogic client =
                    new ClientLogicImpl(DEFAULT_CLIENT_PORT, state);

            MainLoopClient loopClient = new MainLoopClient(state,
                    address, DEFAULT_SERVER_PORT);

            loopClient.run();


        } catch (UnknownHostException e) {
            LOGGER.fatal(" unknow server address " + DEFAULT_ADDRESS);
            parser.printHelp();
            return;
        }

    }

    private static @NotNull
    ArgumentParser initArgumentParser() {
        final ArgumentParser parser = ArgumentParsers
                .newArgumentParser("client")
                .defaultHelp(true).description("client torrent");

        parser.addArgument("-a", "--address")
                .type(String.class)
                .setDefault(DEFAULT_ADDRESS)
                .dest("server address");

        return parser;
    }
}
