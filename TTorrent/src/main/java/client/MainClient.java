package client;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainClient {

    private static final Logger LOGGER = LogManager.getLogger(MainClient.class);
    private static final String DEFAULT_ADDRESS = "localhost";
    private static final Integer DEFAULT_SERVER_PORT = 20006;
    private static final Integer DEFAULT_CLIENT_PORT = 20007;


    public static void main(String[] args) {
        ArgumentParser parser = initArgumentParser();

        try {
            InetAddress address = InetAddress.getByName(DEFAULT_ADDRESS);
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
