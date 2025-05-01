package Server;

import Server.Model.PlayerModel;
import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
/*
        This will communicate with each client/player. It will use Authentication for logging in,
        GameManager for handling game logic (coin flip), and the leaderboard rankings.
 */
    private final Socket clientSocket;
    private boolean authenticated = false;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            while(!authenticated) {
                // Login/Create
                String credentialsPacket;
                while ((credentialsPacket = bufferedReader.readLine()) == null);

                System.out.println("Received Packet [ " + credentialsPacket + " ] by player: [ " + clientSocket.getRemoteSocketAddress() + " ]");

                // Aggregate the information from the incoming packet (String separated by colons)
                String username, password;
                int authType;
                String[] creds = credentialsPacket.split(":");
                username = creds[0];

                // Hash the password here
                password = creds[1];
                authType = Integer.parseInt(creds[2]);

                switch (authType) {
                    // Logging In
                    case 0:
                        PlayerModel player = DAO.getPlayer(username);

                        if (player == null) {
                            bufferedWriter.write("Username doesn't exist.");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();

                            continue;
                        }


                        if (player.getHash().equals(password))
                        {
                            bufferedWriter.write("Password is incorrect.");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();

                            continue;
                        }

                        bufferedWriter.write("0");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();

                        authenticated = true;
                    case 1:
                        PlayerModel newPlayer = new PlayerModel(username, password);

                        if (newPlayer.getUsername() == null || newPlayer.getHash() == null) {
                            bufferedWriter.write("Please provide both username and password.");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();

                            continue;
                        }

                        if (DAO.getPlayer(username) != null)
                        {
                            bufferedWriter.write("Username taken.");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();

                            continue;
                        }

                        bufferedWriter.write("0");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();

                        authenticated = true;
                    default:
                        // Send packet to user (weird login, shouldn't hit this)
                        bufferedWriter.write("You shouldn't be here... Try again...");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                }
            }

            String gamePacket;
            while ((gamePacket = bufferedReader.readLine()) != null) {
                System.out.println("Received Packet [ " + gamePacket + " ] by player: [ " + clientSocket.getRemoteSocketAddress() + " ]");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
