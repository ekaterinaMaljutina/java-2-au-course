package server.api;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public interface ChangeFileList extends Serializable {
    void changeStateSharedFiles(@NotNull ISharedFiles sharedFiles);
}
