package client.api;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public interface IClientRequest {
    Set<Integer> stat(int id) throws IOException;

    boolean get(int id, int partNumber, @NotNull Path out) throws IOException;


}
