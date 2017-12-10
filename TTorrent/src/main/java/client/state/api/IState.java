package client.state.api;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public interface IState extends IStateClient {
    boolean partOfFile(@NotNull Path file, int numberOfPart);
    boolean newFile(@NotNull Path file, IClientFile fileInfo);
}
