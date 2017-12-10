package client.state.api;

import java.util.Set;

public interface IClientFile {
    int getId();

    long getSize();

    boolean isDownloaded();

    Set<Integer> getParts();
}
