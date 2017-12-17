package client;

import client.api.ClientListener;
import client.api.IClientLogic;
import client.api.IState;
import client.impl.ClientLogicImpl;
import client.impl.SaveStateClientListener;
import client.impl.StateClient;
import client.impl.UpdateInfoTaskFromServer;
import common.commands.io.RequestToServer;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class MainClient {

    private static final Logger LOGGER = LogManager.getLogger(MainClient.class);
    private static final String DEFAULT_ADDRESS = "localhost";
    private static final Integer DEFAULT_SERVER_PORT = 8081;
    private static final Integer DEFAULT_CLIENT_PORT = 8082;
    private static final String PATH_TO_CONFIG = "./src/main/resources/client.cfg";
    private static final Path dirDownloader = Paths.get("./src/main/resources/");


    public static void main(String[] args) {
        ArgumentParser parser = initArgumentParser();

        try {
            InetAddress address = InetAddress.getByName(DEFAULT_ADDRESS);


            // load last client state
            // Here load old state client
            IState state = new StateClient(Collections.emptyMap());
            ClientListener listener = new SaveStateClientListener(PATH_TO_CONFIG);
            state.addClientListener(listener);

            UpdateInfoTaskFromServer updateInfoTaskFromServer =
                    new UpdateInfoTaskFromServer(state,
                            new RequestToServer(address, DEFAULT_SERVER_PORT),
                            DEFAULT_CLIENT_PORT);

            Downloader downloader = new Downloader(updateInfoTaskFromServer, state, new RequestToServer(address, DEFAULT_SERVER_PORT), dirDownloader);
            state.getListIdFiles().forEach(downloader::startDownload);

            IClientLogic client =
                    new ClientLogicImpl(DEFAULT_CLIENT_PORT, state);


            MainLoopClient loopClient = new MainLoopClient(state,
                    address, DEFAULT_SERVER_PORT, downloader);
            loopClient.addExitCommand(downloader);
            loopClient.addExitCommand(updateInfoTaskFromServer);
            new Thread(loopClient).start();
            loopClient.addExitCommand(() -> {
                try {
                    client.shutdown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            client.start();


        } catch (UnknownHostException e) {
            LOGGER.fatal(" unknow server address " + DEFAULT_ADDRESS);
            parser.printHelp();
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
