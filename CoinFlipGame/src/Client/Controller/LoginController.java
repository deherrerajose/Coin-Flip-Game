package Client.Controller;

import Client.ClientApplication;
import Client.Network.ClientNetwork;
import Client.View.LoginView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController
{
    /*
            This controller will handle the users `LoginView` adn will send and receive information
            using the `ClientNetwork`.
     */
    private class LoginAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String output = ClientNetwork.authenticateUser(view.getUsername(), view.getPassword(), 0);

            System.out.println("Received: " + output);

            if (output.equals("0"))
            {
                ClientApplication.startGame();
                return;
            }

            view.setMessage(output);
        }
    }
    private class SignUpAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String output = ClientNetwork.authenticateUser(view.getUsername(), view.getPassword(), 1);

            System.out.println("Received: " + output);

            if (output.equals("0"))
            {
                ClientApplication.startGame();
                return;
            }

            view.setMessage(output);
        }
    }


    private LoginView view;

    public LoginController(LoginView view)
    {
        this.view = view;

        this.view.setLoginListener(new LoginAction());
        this.view.setSignUpListener(new SignUpAction());
    }
}
