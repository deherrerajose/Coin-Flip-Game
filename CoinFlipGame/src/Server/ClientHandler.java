package Server;

import Server.Model.PlayerModel;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
/*
        This will communicate with each client/player. It will use Authentication for logging in,
        GameManager for handling game logic (coin flip), and the leaderboard rankings.
 */
    private final Socket clientSocket;
    private boolean authenticated = false;
    private PlayerModel player;

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

                // Packet: "<username:String> <password:String> <authType:0/1>"
                System.out.println("Received Packet [ " + credentialsPacket + " ] by player: [ " + clientSocket.getRemoteSocketAddress() + " ]");

                // Parse the information from the incoming packet (String separated by colons)
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
                        PlayerModel logPlayer = DAO.getPlayer(username);

                        if (logPlayer == null) {
                            sendToPlayer(bufferedWriter, "Username doesn't exist.");
                            System.out.println("Sent Packet [ Username doesn't exist. ]");
                            continue;
                        }

                        String hashed_pass = logPlayer.getHash();
//                        System.out.println(hashed_pass);
                        if (!hashed_pass.equals(password)) {
                            sendToPlayer(bufferedWriter, "Password is incorrect.");
                            System.out.println("Sent Packet [ Password is incorrect. ]");
                            continue;
                        }

                        sendToPlayer(bufferedWriter, "0");
                        System.out.println("Sent Packet [ 0 ] indicating success.");

                        authenticated = true;
                        this.player = logPlayer;

                        break;
                    // Create New
                    case 1:
                        PlayerModel newPlayer = new PlayerModel(username, password);

                        if (newPlayer.getUsername() == null || newPlayer.getHash() == null) {
                            sendToPlayer(bufferedWriter, "Please provide both username and password.");
                            System.out.println("Sent Packet [ Please provide both username and password. ]");
                            continue;
                        }

                        if (DAO.getPlayer(username) != null) {
                            sendToPlayer(bufferedWriter, "Username taken.");
                            System.out.println("Sent Packet [ Username taken. ]");
                            continue;
                        }

                        DAO.createNewPlayer(newPlayer);
                        sendToPlayer(bufferedWriter, "0");
                        System.out.println("Sent Packet [ 0 ] indicating success.");

                        authenticated = true;
                        this.player = newPlayer;

                        break;
                    default:
                        // Send packet to user (weird login, shouldn't hit this)
                        sendToPlayer(bufferedWriter, "You shouldn't be here... Try again...");
                        System.out.println("Sent Packet [ You shouldn't be here... Try again... ]");
                        break;
                }
            }

            String gamePacket;
            while ((gamePacket = bufferedReader.readLine()) != null) {
                // Package: "<mode:0/1> <<option:0/5> <betAmount:float>"
                System.out.println("Received Packet [ " + gamePacket + " ] by player: [ " + clientSocket.getRemoteSocketAddress() + " ]");

                String[] game = gamePacket.split(":");
                int mode = Integer.parseInt(game[0]);
                int option = Integer.parseInt(game[1]);
                float betAmount = Float.parseFloat(game[2]);

                switch (mode) {
                    // Coinflip
                    case 0:
                        int flip = (int) Math.floor(Math.random() * 2);

                        if (flip == option && player != null) {
                            player.setBalance(player.getBalance() + betAmount);
                        } else if (flip != option && player != null) {
                            player.setBalance(player.getBalance() - betAmount);
                        }
                        DAO.updatePlayerBalance(player, player.getBalance());

                        sendToPlayer(bufferedWriter, getString(flip));

                        break;
                    // Dice
                    case 1:
                        int roll = (int) Math.floor(Math.random() * 6);

                        if (roll == option && player != null) {
                            player.setBalance(player.getBalance() + betAmount * 5);
                        } else if (roll != option && player != null) {
                            player.setBalance(player.getBalance() - betAmount);
                        }
                        DAO.updatePlayerBalance(player, player.getBalance());

                        sendToPlayer(bufferedWriter, getString(roll));

                        break;
                    default:
                        sendToPlayer(bufferedWriter, "Something went wrong, try again.");
                        break;
                }
            }

        } catch (IOException e) {
            System.out.println("Client [ " + clientSocket.getRemoteSocketAddress() + " ] disconnected.");
        }
    }

    private String getString(int result) {
        ArrayList<PlayerModel> leaderboard = DAO.getTopThree();
        // Packet: "<updatedBalance:float> <1stUsername:String> <1stBalance:float> <2ndUsername:String>
        //              <2ndBalance:float> <3rdUsername:String> <3rdBalance:float> <result>"
        return String.format("%.2f:%s:%.2f:%s:%.2f:%s:%.2f:%d", player.getBalance(), leaderboard.get(0).getUsername(),
                leaderboard.get(0).getBalance(), leaderboard.get(1).getUsername(), leaderboard.get(1).getBalance(),
                leaderboard.get(2).getUsername(), leaderboard.get(2).getBalance(), result);
    }

    private void sendToPlayer(BufferedWriter bufferedWriter, String msg) throws IOException {
        bufferedWriter.write(msg);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }
}
