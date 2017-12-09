package client;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface IClient {
    List<Integer> stat(int id) throws IOException;

    boolean get(int id, int partNumber, @NotNull Path out) throws IOException;


}
