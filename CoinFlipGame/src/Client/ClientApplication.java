package Client;

import Client.Controller.GameController;
import Client.Controller.LoginController;
import Client.Network.ClientNetwork;
import Client.View.GameView;
import Client.View.LoginView;

import java.io.IOException;

public class ClientApplication
{
/*
        This will be the main entry point for the client/player. It will initialize the
        GUI and handle switching between the views.
*/

    private static LoginView loginView;

    public static void main(String[] arg)
    {
        loginView = new LoginView();
        LoginController loginController = new LoginController(loginView);
    }

    public static void startGame()
    {
        loginView.close();
        GameView view = new GameView();
        GameController controller = new GameController(view);
    }
}
