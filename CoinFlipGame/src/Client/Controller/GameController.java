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
            String packet = String.format(
                "%d:%d:%f",
                currentMode,
                view.getBetOption(),
                view.getBet()
            );
            ClientNetwork.sendString(packet);

            try
            {
                String[] output = ClientNetwork.recieveString().split(":");
                view.setBalance(output[0]);
                view.setLeaders(output[1], output[2], output[3]);
                view.setResult(betOptions[currentMode][Integer.parseInt(output[4])]);
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

        this.view.setSwapListener(new swapActionListener());
        this.view.setBetOption(betOptions[currentMode]);

        this.view.setBetListener(new BetActionListener());
    }
}
