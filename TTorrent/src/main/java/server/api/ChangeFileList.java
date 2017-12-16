package server.api;

import org.jetbrains.annotations.NotNull;

public interface ChangeFileList {
    void changeStateSharedFiles(@NotNull ISharedFiles sharedFiles);
}
