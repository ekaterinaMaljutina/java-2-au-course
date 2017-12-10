package client.api;

import java.util.Set;

public interface IClientFile {
    int getId();

    long getSize();

    boolean isDownloaded();

    boolean addPartOfFile(int idPart);

    Set<Integer> getParts();
}
