package View;

import javax.swing.*;
import java.awt.*;

public class View
{
    private JFrame jFrame;

    private JTextField username;
    private JPasswordField password;
    private JButton login;
    private JButton signUp;

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


    public View()
    {
        gamePage();

        jFrame.setSize(400, 300);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void loginPage()
    {

        jFrame = new JFrame();

        JLabel userLabel = new JLabel("Username");
        username = new JTextField(25);
        JLabel passLabel = new JLabel("Password");
        password = new JPasswordField(25);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.add(Box.createVerticalGlue());
        inputPanel.add(userLabel);
        inputPanel.add(username);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(passLabel);
        inputPanel.add(password);
        inputPanel.add(Box.createVerticalGlue());


        login  = new JButton("Login");
        signUp = new JButton("Sign Up");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(login);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(signUp);
        buttonPanel.add(Box.createHorizontalGlue());

        jFrame.add(inputPanel, BorderLayout.CENTER);
        jFrame.add(buttonPanel, BorderLayout.SOUTH);
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



        jFrame.add(leaderBoard, BorderLayout.WEST);
        jFrame.add(betBoard, BorderLayout.SOUTH);


        result = new JLabel("Pending...");
    }
}
