package client;

import client.api.ClientListener;
import client.api.IClientLogic;
import client.api.IState;
import client.impl.ClientLogicImpl;
import client.impl.SaveStateClientListener;
import client.impl.StateClient;
import client.impl.UpdateInfoTaskFromServer;
import common.Common;
import common.commands.io.RequestToServer;
import common.nio.ObjectReader;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

public class MainClient {

    private static final Logger LOGGER = LogManager.getLogger(MainClient.class);

    public static void main(String[] args) {
        LOGGER.info(Arrays.asList(args));
        ArgumentParser parser = initArgumentParser();

        try {
            Namespace parsingResult;
            try {
                parsingResult = parser.parseArgs(args);
            } catch (ArgumentParserException e) {
                parser.handleError(e);
                return;
            }
            String address = parsingResult.getString("address");
            int portServer = parsingResult.getInt("port_server");
            int portClient = parsingResult.getInt("port");
            final Path configPath = Paths.get(parsingResult.getString("config"));
            Path dirDownloader = Paths.get(parsingResult.getString("directory"));
            File workingDirectoryFile = dirDownloader.toFile();
            if (!workingDirectoryFile.exists() ||
                    !workingDirectoryFile.isDirectory()) {
                LOGGER.fatal(" directory not exist or not is directory " +
                        dirDownloader);
                parser.printHelp();
                return;
            }


            IState state;
            if (configPath.toFile().exists()) {
                LOGGER.info("load saved state");
                state = (StateClient) ObjectReader.readFileToObject(configPath.toString());
            } else {
                LOGGER.info(" state is empty loading ");
                state = new StateClient(Collections.emptyMap());
            }

            ClientListener listener = new SaveStateClientListener(configPath.toString());
            state.addClientListener(listener);
            InetAddress ip = InetAddress.getByName(address);
            UpdateInfoTaskFromServer updateInfoTaskFromServer =
                    new UpdateInfoTaskFromServer(state,
                            new RequestToServer(ip, portServer), portClient);

            Downloader downloader = new Downloader(updateInfoTaskFromServer, state,
                    new RequestToServer(ip, portServer), dirDownloader);
            state.getListIdFiles().forEach(downloader::startDownload);

            IClientLogic client = new ClientLogicImpl(portClient, state);


            MainLoopClient loopClient = new MainLoopClient(state, ip, portServer,
                    downloader);
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
            LOGGER.fatal(" unknow server address ");
            parser.printHelp();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.fatal(" get ERROR ", e);
        }

    }

    private static @NotNull
    ArgumentParser initArgumentParser() {
        final ArgumentParser parser = ArgumentParsers
                .newArgumentParser("client")
                .defaultHelp(true).description("client torrent");

        parser.addArgument("-a", "--address")
                .type(String.class)
                .setDefault(Common.DEFAULT_ADDRESS)
                .dest("server address");

        parser.addArgument("-ps", "--port_server")
                .type(Integer.class)
                .choices(Arguments.range(0, (1 << 16) - 1))
                .setDefault(Common.DEFAULT_SERVER_PORT)
                .help("server port for connection");

        parser.addArgument("-p", "--port")
                .type(Integer.class)
                .choices(Arguments.range(0, (1 << 16) - 1))
                .setDefault(Common.DEFAULT_CLIENT_PORT)
                .help("client port for connection");

        parser.addArgument("-d", "--directory")
                .type(String.class)
                .setDefault(Common.PATH_TO_RESOURCES)
                .help("path to working directory");
        parser.addArgument("-c", "--config")
                .type(String.class)
                .setDefault(Common.PATH_TO_CLIENT_CONFIG)
                .help("path to configuration file");

        return parser;
    }
}
