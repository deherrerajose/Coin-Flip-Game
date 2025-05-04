package Client.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientNetwork
{
/*
        This class is used to communicate with the server via socket. This will be used in each controller
        and will handle receiving and sending messages to the server.
*/

    private static String host = "127.0.0.1";
    private static int port = 5000;

    private static Socket socket;
    private static BufferedReader serverInput;
    private static PrintWriter serverOutput;

    public static void startConnection() throws IOException
    {
        if (socket == null || socket.isClosed())
        {
            socket = new Socket(host, port);
            serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            serverOutput = new PrintWriter(socket.getOutputStream(), true);
        }
    }

    public static void closeConnection() throws IOException
    {
        serverInput.close();
        serverOutput.close();
        socket.close();
    }


    public static String authenticateUser(String username, String password, int mode)
    {
        serverOutput.println(String.format("%s:%s:%d", username, password, mode));

        try
        {
            return recieveString();
        }
        catch (IOException e)
        {
            return "Issue with server connection.";
        }
    }

    public static void sendString(String str)
    {
        serverOutput.println(str);
    }

    public static String recieveString() throws IOException
    {
        String output = serverInput.readLine();
        System.out.println(String.format("Received Packet [ %s ]", output));
        return output;
    }
}
