package client.state.api;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public interface IState {
    boolean partOfFile(@NotNull Path file, int numberOfPart);
    boolean newFile(@NotNull Path file, IClientFiles fileInfo);
}
