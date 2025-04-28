package Client.View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameView
{
    private JFrame jFrame;

    private JLabel first ;
    private JLabel second;
    private JLabel third ;

    private JLabel balance;
    private JTextField betAmount;
    private JButton headBet;
    private JButton tailBet;
    private JLabel result;

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

        first  = new JLabel("1. Test person");
        second = new JLabel("2. Test person");
        third  = new JLabel("3. Test person");

        JPanel leaderBoard = new JPanel();
        leaderBoard.setLayout(new BoxLayout(leaderBoard, BoxLayout.Y_AXIS));
        leaderBoard.add(first);
        leaderBoard.add(Box.createRigidArea(new Dimension(0, 10)));
        leaderBoard.add(second);
        leaderBoard.add(Box.createRigidArea(new Dimension(0, 10)));
        leaderBoard.add(third);

        balance = new JLabel("$100.00");
        betAmount = new JTextField(25);
        headBet = new JButton("Heads");
        tailBet = new JButton("Tails");


        JPanel betBoard = new JPanel();
        leaderBoard.setLayout(new BoxLayout(leaderBoard, BoxLayout.Y_AXIS));
        betBoard.add(balance);
        betBoard.add(betAmount);

        JPanel subBoard = new JPanel();
        subBoard.add(headBet);
        subBoard.add(Box.createRigidArea(new Dimension(10, 0)));
        subBoard.add(tailBet);

        betBoard.add(subBoard);


        result = new JLabel("Pending...");

        jFrame.add(result, BorderLayout.EAST);
        jFrame.add(leaderBoard, BorderLayout.WEST);
        jFrame.add(betBoard, BorderLayout.SOUTH);


    }

    public void setLeaders(String first, String second, String third)
    {
        this.first.setText(first);
        this.second.setText(first);
        this.third.setText(first);
    }

    public void setBalance(String bal) { balance.setText("$"+bal); }
    public void setResult(String res) { result.setText(res); }
    public String readBet() { return betAmount.getText(); }

    public void setHeadBetListener(ActionListener a) { headBet.addActionListener(a); }
    public void setTailBetListener(ActionListener a) { tailBet.addActionListener(a); }
}
