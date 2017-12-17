package common;

import java.util.concurrent.TimeUnit;

public class Common {

    public static final Integer PATH_OF_FILE_SIZE = 10 * 1024 * 1024; // 10 M

    public static final long UPDATE_CLIENT_FILES =
            TimeUnit.MINUTES.toMillis(TimeUnit.MINUTES.toMinutes(5));


    public static final String DEFAULT_ADDRESS = "localhost";

    public static final Integer DEFAULT_SERVER_PORT = 8081;

    public static final Integer DEFAULT_CLIENT_PORT = 8082;

    public static final String PATH_TO_RESOURCES = "./src/main/resources/";

    public static final String PATH_TO_CLIENT_CONFIG =
            PATH_TO_RESOURCES + "client.cfg";

    public static final String PATH_TO_SERVER_CONFIG =
            PATH_TO_RESOURCES + "server.cfg";

    private Common() {
    }
}
