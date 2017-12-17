package client.api;

import org.jetbrains.annotations.NotNull;

public interface IState extends IStateClient {
    void partOfFile(@NotNull String file, int numberOfPart);

    boolean newFile(@NotNull String file, IClientFile fileInfo);
}
