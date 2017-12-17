package common.commands.io;

import client.api.IState;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class GetLocalFilesCommand implements ICommand {
    private static final String title = "%5s|%25s|%10s|%5s|%s";

    @Override
    public boolean runCommand(@NotNull IState state, String[] args) {
        LOGGER.info(String.format(title, "id", "path", "size", "isLoaded",
                "parts"));
        state.getAllFilesWithInfo().forEach((path, clientFile) -> {
            System.out.println(String.format(title, clientFile.getId(), path,
                    clientFile.getSize(), clientFile.isDownloaded(),
                    Arrays.toString(clientFile.getParts().toArray())));
        });

        return false;
    }

    @Override
    public String commandName() {
        return " show local downloaded files";
    }

    @NotNull
    @Override
    public Integer getId() {
        return IdRequestToServer.SHOW_LOCAL_FILES_REQUEST;
    }
}
