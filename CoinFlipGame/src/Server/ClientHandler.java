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
                            continue;
                        }

                        String hashed_pass = logPlayer.getHash();
//                        System.out.println(hashed_pass);
                        if (!hashed_pass.equals(password)) {
                            sendToPlayer(bufferedWriter, "Password is incorrect.");
                            continue;
                        }

                        sendToPlayer(bufferedWriter, "0");

                        authenticated = true;
                        this.player = logPlayer;

                        break;
                    // Create New
                    case 1:
                        PlayerModel newPlayer = new PlayerModel(username, password);
                        System.out.println(newPlayer);
                        if (
                                newPlayer.getUsername()   == null ||
                                newPlayer.getUsername().isEmpty() ||
                                newPlayer.getHash() == null ||
                                newPlayer.getHash().isEmpty()
                        ) {
                            sendToPlayer(bufferedWriter, "Please provide both username and password.");
                            continue;
                        }

                        if (DAO.getPlayer(username) != null) {
                            sendToPlayer(bufferedWriter, "Username taken.");
                            continue;
                        }

                        DAO.createNewPlayer(newPlayer);
                        sendToPlayer(bufferedWriter, "0");

                        authenticated = true;
                        this.player = newPlayer;

                        break;
                    default:
                        // Send packet to user (weird login, shouldn't hit this)
                        sendToPlayer(bufferedWriter, "You shouldn't be here... Try again...");
                        break;
                }
            }

            //      Need to send leaderboard results and balance apon start up, just sending dummy results to reuse function
            sendToPlayer(bufferedWriter, getString(-1));


            String gamePacket;
            while ((gamePacket = bufferedReader.readLine()) != null) {
                // Package: "<mode:0/1> <<option:0/5> <betAmount:float>"
                System.out.println("Received Packet [ " + gamePacket + " ] by player: [ " + clientSocket.getRemoteSocketAddress() + " ]");

                String[] game = gamePacket.split(":");
                int mode = Integer.parseInt(game[0]);
                int option = Integer.parseInt(game[1]);
                float betAmount = Float.parseFloat(game[2]);

                //  Dont let players bet money they dont have
                if (betAmount > player.getBalance())
                {
                    sendToPlayer(bufferedWriter, getError("You dont have enough for that bet."));
                    continue;
                }

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

        // Reordered packet format to fix issue when there is less than 3 users.
        //      Removed colons between username and respective balance since there always displayed together anyway
        //      A result of -1 now indicates an error message, soit needs to go first to detect that.


        // Packet: "<result:int>:<updatedBalance:float>
        //          <1stUsername:String> - <1stBalance:float>:
        //          <2ndUsername:String> - <2ndBalance:float>:
        //          <3rdUsername:String> - <3rdBalance:float>"

        String message = String.format("%d:%.2f", result, player.getBalance());

        ArrayList<PlayerModel> leaderboard = DAO.getTopThree();
        for (PlayerModel player : leaderboard)
        {
            message += String.format(
                    ":%s - %.2f",
                    player.getUsername(),
                    player.getBalance()
            );
        }

        return message;
    }

    private String getError(String message)
    {

        // Reordered packet format to fix issue when there is less than 3 users.
        //      Removed colons between username and respective balance since there always displayed together anyway
        //      A result of -1 now indicates an error message, soit needs to go first to detect that.


        // Packet: "<result:int>:<message:String>"

        return message = String.format("-1:%s", message);
    }


    private void sendToPlayer(BufferedWriter bufferedWriter, String msg) throws IOException
    {
        System.out.println(String.format("Sent Packet [ %s ]", msg));
        bufferedWriter.write(msg);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }
}
