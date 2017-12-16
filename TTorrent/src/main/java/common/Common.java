package common;

import java.util.concurrent.TimeUnit;

public class Common {

    public static final Integer PATH_OF_FILE_SIZE = 10 * 1024 * 1024; // 10 M
    public static final long UPDATE_CLIENT_FILES =
            TimeUnit.MINUTES.toMinutes(5);

    private Common() {
    }
}
