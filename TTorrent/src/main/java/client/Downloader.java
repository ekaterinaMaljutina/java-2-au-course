package client;

import client.api.*;
import client.impl.ClientFileInfo;
import client.impl.OtherClientRequest;
import client.impl.UpdateInfoTaskFromServer;
import common.Common;
import common.commands.io.IRequestToServer;
import common.files.IFileInfo;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class Downloader implements ExitCommandListener {

    private static final Logger LOGGER = LogManager.getLogger(Downloader.class);

    private static final int COUNT_THREAD = 4;
    private static final long RESOLVER_FAILED_DELAY_SECONDS = 10;
    private static final int TASKS_PER_FILE_LIMIT = 5;

    private final ScheduledExecutorService executorService;
    private final UpdateInfoTaskFromServer updateInfoTaskFromServer;
    private final IState stateClient;
    private final IRequestToServer server;

    private final Path dirForDownloaderFiles;

    private final Map<Integer, BlockingQueue<Integer>> idFileToDownloadPart =
            new ConcurrentHashMap<>();

    public Downloader(UpdateInfoTaskFromServer updateInfoTaskFromServer, IState stateClient, IRequestToServer server, Path dirForDownloaderFiles) {
        this.updateInfoTaskFromServer = updateInfoTaskFromServer;
        this.stateClient = stateClient;
        this.server = server;
        this.dirForDownloaderFiles = dirForDownloaderFiles;
        this.executorService = Executors.newScheduledThreadPool(COUNT_THREAD);
    }

    public void startDownload(int idFile) {
        executorService.execute(new ResolverFile(idFile));
    }

    @Override
    public void shutdown() {
        LOGGER.debug(" shutdown Downloader ");
        executorService.shutdown();
    }


    private class ResolverFile implements Runnable {

        private final int idFile;

        public ResolverFile(int idFile) {
            this.idFile = idFile;
        }

        @Override
        public void run() {
            if (idFileToDownloadPart.containsKey(idFile)) {
                LOGGER.info("File with id " + idFile + " already downloading.");
                return;
            }

            final Path localPath = stateClient.getPathByFileId(idFile);
            final IClientFile localInfo = stateClient.getFileInfoById
                    (idFile);
            if (localPath != null && localInfo != null) {
                downloadToExistedFile(localInfo);
            } else {
                downloadNewFile();
            }
        }

        private void downloadNewFile() {
            try {
                final Map<Integer, IFileInfo> files = server.getListFiles();
                if (!files.containsKey(idFile)) {
                    LOGGER.warn("File with id = " + idFile + "not found");
                    return;
                }

                final IFileInfo info = files.get(idFile);
                final Path localPath = dirForDownloaderFiles.resolve(info.getName());
                final Path pathToSave = getFileToSave(localPath, info);
                if (pathToSave == null) {
                    LOGGER.error("Could not find the file to save file with id =" +
                            " " + idFile);
                    return;
                }

                stateClient.newFile(pathToSave, new ClientFileInfo(idFile,
                        info.getSize(), Collections.emptySet()));
                run();
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.error("Cannot load files on the server. Try again after " + RESOLVER_FAILED_DELAY_SECONDS + " seconds");
                executorService.schedule(this, RESOLVER_FAILED_DELAY_SECONDS, TimeUnit
                        .SECONDS);
            }
        }

        private void downloadToExistedFile(@NotNull IClientFile localInfo) {
            final int partCount = partCount(localInfo.getSize());
            final Set<Integer> loadedParts = localInfo.getParts();
            final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(partCount - loadedParts.size());
            for (int i = 0; i < partCount; i++) {
                if (!loadedParts.contains(i)) {
                    queue.add(i);
                }
            }

            if (queue.size() > 0) {
                idFileToDownloadPart.put(idFile, queue);
                final Runnable downloader = new DownloaderFile(idFile);
                for (int i = 0; i < TASKS_PER_FILE_LIMIT; i++) {
                    executorService.execute(downloader);
                }

            } else {
                LOGGER.info(String.format("File with id = %d already loaded", idFile));
            }
        }

        @Nullable
        private Path getFileToSave(@Nullable Path path, @NotNull IFileInfo info) {
            if (path == null) {
                LOGGER.info("No local file for file with id = " + idFile + ". Create a new one");
                path = dirForDownloaderFiles.resolve(info.getName());
            }

            if (path.toFile().exists()) {
                return canSaveToExistedFile(path, info) ? path : null;
            }

            return allocateNewFile(path, info) ? path : null;
        }

        private boolean allocateNewFile(@NotNull Path path,
                                        @NotNull IFileInfo info) {
            try (RandomAccessFile f = new RandomAccessFile(path.toAbsolutePath().toString(), "rw")) {
                f.setLength(info.getSize());
                return true;
            } catch (FileNotFoundException e) {
                LOGGER.error("Cannot create file " + path.toAbsolutePath() + " for file with id = " + idFile, e);
            } catch (IOException e) {
                LOGGER.error(String.format("Cannot resize file %s, to %d for file with id = %d", path.toAbsolutePath(),
                        info.getSize(), idFile));
            }

            return false;
        }

        private boolean canSaveToExistedFile(@NotNull Path path,
                                             @NotNull IFileInfo info) {
            if (FileUtils.sizeOf(path.toFile()) == info.getSize()) {
                LOGGER.info(String.format("Download file with id = %d to existed file %s", idFile,
                        path.toAbsolutePath()));
                return true;
            }

            LOGGER.error("Could not save file with id = " + idFile + " to " + path.toAbsolutePath() +
                    ". File has another size");

            return false;
        }

        public int partCount(long size) {
            return (int) Math.ceil((double) size / Common.PATH_OF_FILE_SIZE);
        }
    }

    private class DownloaderFile implements Runnable {
        private final int idFile;

        public DownloaderFile(int idFile) {
            this.idFile = idFile;
        }

        @Override
        public void run() {

            BlockingQueue<Integer> partsForDownload
                    = idFileToDownloadPart.getOrDefault(idFile, null);
            Integer idPartForDownload = partsForDownload == null ? null :
                    partsForDownload.poll();

            Path filePath = stateClient.getPathByFileId(idFile);

            if (idPartForDownload == null || checkExistFile(filePath)) {
                LOGGER.debug(" file id = " + idFile + " loaded or not exist");
                idFileToDownloadPart.remove(idFile);
                return;
            }

            try {
                if (!loadPartFile(idFile, idPartForDownload, filePath)) {
                    LOGGER.warn(" Not part on other client ");
                    partsForDownload.add(idPartForDownload);
                    executorService.schedule(this, 10, TimeUnit.SECONDS);
                } else {
                    executorService.schedule(this, 10, TimeUnit.MICROSECONDS);
                }

            } catch (IOException | ClassNotFoundException e) {
                LOGGER.error(String.format(" download part %d failed for " +
                        "file %d", idPartForDownload, idFile));
                partsForDownload.add(idPartForDownload);
                executorService.schedule(this, 10, TimeUnit.SECONDS);
            }
        }


        private boolean loadPartFile(@NotNull Integer idFile,
                                     @NotNull Integer idPartForDownload,
                                     @NotNull Path filePath)
                throws IOException, ClassNotFoundException {
            boolean flagSuccess = false;
            final List<ClientInfo> clientInfoList = server.sources(idFile);
            for (ClientInfo clientInfo : clientInfoList) {
                IClientRequest clientRequest =
                        new OtherClientRequest(
                                InetAddress.getByAddress(
                                        clientInfo.getIpAddress()),
                                clientInfo.getPort());
                Set<Integer> parts = clientRequest.stat(idFile);
                if (parts.contains(idPartForDownload)) {
                    if (clientRequest.get(idFile, idPartForDownload,
                            filePath)) {
                        flagSuccess = true;
                        LOGGER.debug(String.format("Load part %d for " +
                                "file %d ", idPartForDownload, idFile));

                        IClientFile file = stateClient.getFileInfoById(idFile);
                        if (file != null && file.getParts().isEmpty()) {
                            updateInfoTaskFromServer.runAsych();
                        }
                        stateClient.partOfFile(filePath, idPartForDownload);

                        IClientFile fileInfo = stateClient.getFileInfoById
                                (idFile);
                        if (fileInfo != null && fileInfo.isDownloaded()) {
                            idFileToDownloadPart.remove(idFile);
                        }
                        break;
                    }
                }
            }
            return flagSuccess;
        }

        private boolean checkExistFile(@Nullable Path path) {
            return path == null || !path.toFile().exists();
        }
    }
}
