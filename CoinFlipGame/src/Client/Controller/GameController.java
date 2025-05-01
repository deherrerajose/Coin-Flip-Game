package Client.Controller;

import Client.Network.ClientNetwork;
import Client.View.GameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public GameController(GameView view)
    {
        this.view = view;

        this.view.setSwapListener(new swapActionListener());
        this.view.setBetOption(betOptions[currentMode]);
    }
}
