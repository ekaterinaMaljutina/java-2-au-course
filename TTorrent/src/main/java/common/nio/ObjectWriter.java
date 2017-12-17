package common.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ObjectWriter {

    public static void writeObjectToFile(String path, Object object)
            throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(path);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(object);
        }
    }
}
