package Client.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameView
/*
        This will be the primary game page. It will include the leaderboard which shows the top 3 players
        with the highest balance, the authenticated players balance, a way to bet (bet amount, tail and
        heads option), a way to submit the bet, and a way to view results.
 */
{

    private JFrame jFrame;

    private JLabel first ;
    private JLabel second;
    private JLabel third ;

    private JLabel result;


    private JLabel balance;
    private JTextField betAmount;
    private JComboBox dropdown;
    private JButton bet;
    private JButton swap;

    //  Sources
    //      https://www.cs.rutgers.edu/courses/111/classes/fall_2011_tjang/texts/notes-java/GUI/layouts/42boxlayout-spacing.html

    public GameView()
    {
        gamePage();

        jFrame.setSize(400, 300);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void gamePage()
    {
        jFrame = new JFrame();

        first  = new JLabel("1. ");
        second = new JLabel("2. ");
        third  = new JLabel("3. ");

        JPanel leaderBoard = new JPanel();
        leaderBoard.setLayout(new BoxLayout(leaderBoard, BoxLayout.Y_AXIS));
        leaderBoard.add(first);
        leaderBoard.add(Box.createRigidArea(new Dimension(0, 10)));
        leaderBoard.add(second);
        leaderBoard.add(Box.createRigidArea(new Dimension(0, 10)));
        leaderBoard.add(third);

        jFrame.add(leaderBoard, BorderLayout.WEST);


        result = new JLabel("Pending...");
//        result.setAlignmentX(Component.CENTER_ALIGNMENT);
        jFrame.add(result, BorderLayout.CENTER);

        JPanel betPanel = new JPanel();
        betPanel.setLayout(new BoxLayout(betPanel, BoxLayout.Y_AXIS));

        balance = new JLabel("$0.00");
        balance.setAlignmentX(Component.CENTER_ALIGNMENT);
        betAmount = new JTextField(25);
        betAmount.setMaximumSize( betAmount.getPreferredSize() );

        dropdown = new JComboBox();

        bet  = new JButton("Place Bet");
        swap = new JButton("Swap Game");

        JPanel betBoard = new JPanel();
        betBoard.setLayout(new BoxLayout(betBoard, BoxLayout.Y_AXIS));
        betBoard.add(balance);
        betBoard.add(betAmount);

        JPanel subBoard = new JPanel();
        subBoard.add(dropdown);
        subBoard.add(Box.createRigidArea(new Dimension(5, 0)));
        subBoard.add(bet);
        subBoard.add(Box.createRigidArea(new Dimension(5, 0)));
        subBoard.add(swap);

        betBoard.add(subBoard);


        betPanel.add(betBoard);
        jFrame.add(betPanel, BorderLayout.SOUTH);

    }

    public void setLeader(int leader, String value)
    {
        switch (leader)
        {
            case 2:
                this.first.setText(" 1. " +value);
                break;
            case 3:
                this.second.setText(" 2. "+value);
                break;
            case 4:
                this.third.setText(" 3. " +value);
                break;
        }
    }

    public void setResult(String res) { result.setText(res); }
    public void setBalance(String bal) { balance.setText("$"+bal); }
    public void setSwapListener(ActionListener a) { swap.addActionListener(a); }
    public void setBetOption(String[] options)
    {
        dropdown.removeAllItems();
        for (String item : options)
        {
            dropdown.addItem(item);
        }
        jFrame.setVisible(true);
        dropdown.setSelectedIndex(0);
    }

    public int getBetOption() { return dropdown.getSelectedIndex(); }
    public String getBet() { return betAmount.getText(); }

    public void setBetListener(ActionListener a) { bet.addActionListener(a); }
}
