package server;

import common.Common;
import common.nio.ObjectReader;
import common.nio.ObjectWriter;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import server.api.ISharedFiles;
import server.api.IStateServer;
import server.impl.SharedFiles;
import server.impl.StateServer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class MainServer {

    private static final Logger LOGGER = LogManager.getLogger(MainServer.class);

    public static void main(String[] args) {

        final ArgumentParser parser = createParser();
        final Namespace parsingResult;
        try {
            parsingResult = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            return;
        }

        final Integer port = parsingResult.getInt("port");
        final String config = parsingResult.getString("config");
        final Path path = Paths.get(config);

        ISharedFiles sharedFiles;
        if (path.toFile().exists()) {
            try {
                LOGGER.info(" load saved state files");
                sharedFiles = (SharedFiles) ObjectReader.readFileToObject(config);
                sharedFiles.initFileListener();
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.info(" load ERROR " + e);
                parser.printHelp();
                return;
            }
        } else {
            LOGGER.info("start with empty state ");
            sharedFiles = new SharedFiles(Collections.emptyMap());
        }

        sharedFiles.addListenerChangeFiles(state -> {
            try {
                LOGGER.debug(" change files ");
                ObjectWriter.writeObjectToFile(config, state);
            } catch (IOException e) {
                LOGGER.error(" change ERROR not save:" + e);
            }
        });

        IStateServer stateServer = new StateServer(sharedFiles);

        MainLoopServer mainLoopServer = new MainLoopServer(port, stateServer);
        try {
            mainLoopServer.start();
        } catch (IOException e) {
            LOGGER.error("error : " + e);
        }


    }

    private static @NotNull
    ArgumentParser createParser() {
        final ArgumentParser parser = ArgumentParsers.newArgumentParser("server")
                .description("Torrent tracker server")
                .defaultHelp(true);

        parser.addArgument("-p", "--port")
                .type(Integer.class)
                .choices(Arguments.range(0, (1 << 16) - 1))
                .setDefault(Common.DEFAULT_SERVER_PORT)
                .help("port for listening");

        parser.addArgument("-c", "--config")
                .type(String.class)
                .setDefault(Common.PATH_TO_SERVER_CONFIG)
                .help("path to configuration file");

        return parser;
    }
}
