package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApplication {
/*
        This will be the entry point for the server-side. It will accept client connections and use
        the `ClientHandler` to manage each connection (player).
 */
    public static void main(String[] args) throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server Established. Waiting for players to connect using port [" + serverSocket.getLocalPort() + "]");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Player Connected: " + clientSocket.getRemoteSocketAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
