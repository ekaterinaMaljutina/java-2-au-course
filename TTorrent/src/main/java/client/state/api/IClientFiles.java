package client.state.api;

import java.util.Set;

public interface IClientFiles {
    int getId();

    long getSize();

    boolean isDownloaded();

    Set<Integer> getParts();
}
