package common.commands.io;

import client.Downloader;
import client.api.IState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DownloadFileCommand implements ICommand {

    private final Downloader downloader;

    public DownloadFileCommand(Downloader downloader) {
        this.downloader = downloader;
    }

    @Override
    public boolean runCommand(@NotNull IState state, String[] args) {
        LOGGER.warn("run download");
        if (args.length < 2) {
            LOGGER.warn(" args is empty");
            return false;
        }

        List<Integer> idFiles = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            int id = Integer.parseInt(args[i]);
            if (state.getFileInfoById(id) != null) {
                LOGGER.info(" file with id " + id + " downloaded");
            }
            idFiles.add(id);
        }
        idFiles.forEach(downloader::startDownload);
        return false;
    }

    @Override
    public String commandName() {
        return "download file";
    }

    @NotNull
    @Override
    public Integer getId() {
        return IdRequestToServer.SOURCES_REQUEST;
    }
}
