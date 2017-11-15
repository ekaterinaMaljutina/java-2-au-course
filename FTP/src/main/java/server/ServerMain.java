package server;

import common.exception.AlreadyConnectionException;
import common.exception.NotConnectionException;
import server.net.FTPServer;
import server.net.Server;

import java.io.IOException;
import java.util.Scanner;

public class ServerMain {

    private static final int port = 20006;
    private static final String address = "localhost";


    public static void main(String[] args) {
        int usePort = port;
        String host = address;

        if (args.length < 2) {
            System.out.println(String.format("use default address: %s %d ",
                    host, usePort));
        } else {
            host = args[0];
            usePort = Integer.parseInt(args[1]);
            System.out.println(String.format("use  address: %s %d ", host,
                    usePort));
        }
        Server server = new FTPServer();
        try {
            server.start(usePort, host);
            System.out.println("server start");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("stop")) {
                    try {
                        System.out.print(" try stop server ....");
                        server.stop();
                        System.out.println("  OK");
                        return;
                    } catch (NotConnectionException e) {
                        System.out.println(" get ERROR " + e.getMessage());
                    }
                }
            }
        } catch (AlreadyConnectionException | IOException e) {
            System.out.println("get ERROR in start " + e.getMessage());
        }
    }
}
