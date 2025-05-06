package Client.Controller;

import Client.Network.ClientNetwork;
import Client.View.GameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GameController {
/*
        This will be the game and leaderboard logic. Functionality will include updating the clients
        view and receiving and sending information using `ClientNetwork`.
 */
    private static String[][] betOptions = new String[][]
        {
            {"Heads", "Tails"},
            {"1", "2", "3", "4", "5", "6"}
        };
    private int currentMode = 0;

    private GameView view;
    private ClientNetwork network;

    private class swapActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            currentMode = (++currentMode) % betOptions.length;
            view.setBetOption(betOptions[currentMode]);
        }
    }
    private class BetActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            try
            {

                float bet = Float.parseFloat(view.getBet());

                String packet = String.format(
                        "%d:%d:%f",
                        currentMode,
                        view.getBetOption(),
                        bet
                );
                ClientNetwork.sendString(packet);

                String[] output = ClientNetwork.recieveString().split(":");
                int result = Integer.parseInt(output[0]);
                if (result < 0)
                {
                    view.setResult(output[1]);
                    return;
                }

                view.setResult(betOptions[currentMode][result]);
                view.setBalance(output[1]);

                for (int i = 2; i < output.length; i++)
                    view.setLeader(i, output[i]);
            }
            catch (NumberFormatException ex)
            {
                view.setResult("Invalid input for bet. Enter a number.");
            }
            catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
        }
    }

    public GameController(GameView view)
    {
        this.view = view;

        try
        {
            String[] output = ClientNetwork.recieveString().split(":");
            view.setBalance(output[1]);
            view.setResult("Enter bet when ready");

            for (int i = 2; i < output.length; i++)
                view.setLeader(i, output[i]);
        }
        catch (IOException e)
        {
            view.setResult("Error getting data from server, please restart the program.");
            return;
        }


        this.view.setSwapListener(new swapActionListener());
        this.view.setBetOption(betOptions[currentMode]);

        this.view.setBetListener(new BetActionListener());
    }
}
