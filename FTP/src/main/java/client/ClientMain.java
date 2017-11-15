package client;

import client.net.Client;
import client.net.FTPClient;
import common.exception.AlreadyConnectionException;
import common.exception.NotConnectionException;
import common.query.Query;
import common.query.TypeQuery;

import java.io.IOException;
import java.util.Scanner;

public class ClientMain {

    private static final int port = 20006;
    private static final String address = "localhost";

    public static void main(String[] args) {

        int usePort = port;
        String host = address;

        if (args.length < 2) {
            System.out.println("use default address");
        } else {
            host = args[1];
            usePort = Integer.getInteger(args[2]);
        }
        System.out.println(String.format("connect to adress: (%s, %d) ",
                host, usePort));

        Client client = new FTPClient();

        try {
            client.connect(port, address);
        } catch (IOException | AlreadyConnectionException ex) {
            System.out.println("Error : " + ex.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("command : ");
        while (scanner.hasNextLine()) {
            String currentCommand = scanner.nextLine();
            currentCommand = currentCommand.replaceAll("\\s+", " ")
                    .replaceFirst("^\\s+", "");
            String[] argCommand = currentCommand.split("\\s+");

            if (argCommand.length == 1 && (argCommand[0].contains("stop")
                    || Integer.parseInt(argCommand[0]) == TypeQuery.DISCONNECT_QUERY)) {
                try {
                    client.disconnect();
                    return;
                } catch (IOException | NotConnectionException ex) {
                    System.out.println(" disconnect fail " + ex.getMessage());
                }
            }

            if (argCommand.length < 2) {
                System.out.println(" get query : id path");
                continue;
            }

            try {
                Query query = null;
                switch (Integer.parseInt(argCommand[0])) {
                    case TypeQuery.LIST_QUERY:
                        query = client.executeList(argCommand[1]);
                        break;
                    case TypeQuery.GET_QUERY:
                        query = client.executeGet(argCommand[1]);
                        break;
                    default:
                        System.out.println("unsupported operation");
                        System.out.println(" get id query : -1 - disconnect \n " +
                                "1 - list query \n" +
                                "2 - get query ");
                }
                if (query == null) {
                    System.out.println(" socket close ");
                } else {
                    System.out.println(query);
                }

            } catch (NotConnectionException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }
}
