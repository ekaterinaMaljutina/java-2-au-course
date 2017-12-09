package client.commands;

import client.state.api.IState;
import common.files.FileInfoImpl;
import common.files.IFileInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadCommand implements ICommand {

    private IRequestToServer server;

    public UploadCommand(@NotNull InetAddress addressServer, int port) {
        server = new RequestToServer(addressServer, port);
    }

    @Override
    public void runCommand(@NotNull IState state, String[] args) {
        // args : id_command file_name_for_download

        if (args.length < 2) {
            LOGGER.error(" upload without args : <2> file_name");
            return;
        }

        IFileInfo fileInfo = getFileInfo(Paths.get(args[1]), state);
        if (fileInfo == null) {
            return;
        }

        try {
            int idxFile = server.upload(fileInfo);
            LOGGER.debug(String.format(
                    " upload file : %s -> id_file : %d",
                    fileInfo.getName(), idxFile));

            // state add new file !!!!!

        } catch (IOException e) {
            LOGGER.error(" not upload file " + args[1]);
            LOGGER.error(e);
        }

    }

    @Override
    public String commandName() {
        return "Upload Request To Server";
    }


    private static @Nullable
    IFileInfo getFileInfo(@NotNull Path pathToFile, @NotNull IState currentState) {
        if (currentState.getAllFilesWithInfo().containsKey(pathToFile) || !pathToFile.toFile().exists()) {
            LOGGER.error(String.format("file_name = %s : file already exist or file not found", pathToFile.toString()));
            return null;
        }

        return new FileInfoImpl(pathToFile.toFile().getName(), pathToFile.toFile().length());
    }
}
