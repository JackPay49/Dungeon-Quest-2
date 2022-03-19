import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateNewGame extends JFrame{
    private JPanel plTitle;
    private JLabel lbTitle;
    private JPanel plUsername;
    private JTextField txtUsername;
    private JLabel lbUsername;
    private JPanel plPassword;
    private JLabel lbPassword;
    private JPasswordField txtPassword;
    private JPanel plMain;
    private JPanel plButtons;
    private JButton btnCreateGame;
    private JButton btnCancel;

    CreateNewGame()
    {
        this.setTitle("Create new account");
        Image gameIcon = Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\OtherImages\\GameIcon.png"));
        this.setIconImage(gameIcon);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(300, 300);
        this.setLayout(null);
        this.setLocation(350, 200);
        this.setContentPane(this.plMain);

        SetUpFrame();
        this.setVisible(true);
    }
    public void SetUpFrame()
    {
        btnCreateGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateNewAccount();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Close();
            }
        });
    }
    public void Close()
    {
        this.dispose();
    }
    public void CreateNewAccount()
    {
        String username = txtUsername.getText();
        String password = "";
        boolean userExists;
        Player newPlayer;
        for (int i =0;i<txtPassword.getPassword().length;i++)
        {
            password = password + txtPassword.getPassword()[i];
        }
        if ((username.isEmpty()) | (password.isEmpty()))
        {
            JOptionPane.showMessageDialog(null,"You must enter a username & password!");
        }
        else
        {
            userExists = DungeonQuest.CheckIfPlayerExists(username);
            if (userExists)
            {
                JOptionPane.showMessageDialog(null,"This username exists already! Please try with a different username.");
            }
            else
            {
                newPlayer = new Player(txtUsername.getText(),1,password);
                newPlayer.SavePlayer();
                JOptionPane.showMessageDialog(null,"New account has been made!");
                GameMenu.Login(username,password);
                Close();
            }
        }
    }

}
