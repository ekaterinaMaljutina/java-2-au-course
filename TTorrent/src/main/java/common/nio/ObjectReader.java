package common.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ObjectReader {
    public static Object readFileToObject(String path)
            throws IOException, ClassNotFoundException {
        try (FileInputStream fileInputStream = new FileInputStream(path);
             ObjectInputStream inputStream =
                     new ObjectInputStream(fileInputStream)) {
            return inputStream.readObject();
        }
    }
}
