package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView
{
    private JFrame jFrame;

    private JTextField username;
    private JPasswordField password;
    private JButton login;
    private JButton signUp;

    //  Sources
    //      https://www.cs.rutgers.edu/courses/111/classes/fall_2011_tjang/texts/notes-java/GUI/layouts/42boxlayout-spacing.html

    public LoginView()
    {
        loginPage();
        jFrame.setSize(400, 300);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void loginPage()
    {

        jFrame = new JFrame();

        JLabel userLabel = new JLabel("Username");
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        username = new JTextField(25);
        username.setMaximumSize( username.getPreferredSize() );
        username.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel passLabel = new JLabel("Password");
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        password = new JPasswordField(25);
        password.setMaximumSize( password.getPreferredSize() );
        password.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        inputPanel.add(Box.createVerticalGlue());
        inputPanel.add(userLabel);
        inputPanel.add(username);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(passLabel);
        inputPanel.add(password);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        login  = new JButton("Login");
        signUp = new JButton("Sign Up");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(login);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(signUp);
        buttonPanel.add(Box.createHorizontalGlue());

        inputPanel.add(buttonPanel);

        jFrame.add(inputPanel, BorderLayout.CENTER);
        inputPanel.add(Box.createVerticalGlue());

    }

    public String getUsername() { return username.getText(); }
    public String getPassword() { return username.getText(); }

    public void setLoginListener(ActionListener a) { login.addActionListener(a); }
    public void setSignUpListener(ActionListener a) { signUp.addActionListener(a); }
}
