package Client.View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class LoginView
/*
        This will be the login page. It will include a way to input the player username and password.
        It will include the option to login or signup. If the user does not have an account the system
        should prompt the user to signup. Alternatively, if the user has an account and selected signup,
        the system should prompt the user to login.
 */
{
    private JFrame jFrame;

    private JTextField username;
    private JPasswordField password;
    private JButton login;
    private JButton signUp;
    private JLabel errorMessage;

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

        errorMessage = new JLabel();
        inputPanel.add(errorMessage);

        jFrame.add(inputPanel, BorderLayout.CENTER);
        inputPanel.add(Box.createVerticalGlue());

    }

    public String getUsername() { return username.getText(); }
    public String getPassword() { return password.getText(); }

    public void setMessage(String message) { errorMessage.setText(message); }
    public void setLoginListener(ActionListener a) { login.addActionListener(a); }
    public void setSignUpListener(ActionListener a) { signUp.addActionListener(a); }

    public void close()
    {
        jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
    }
}
