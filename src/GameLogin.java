import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GameLogin extends JFrame {
    private JPanel plTitle;
    private JLabel lbTitle;
    public JTextField txtUsername;
    private JLabel lbUsername;
    private JLabel lbPassword;
    private JPanel plPassword;
    private JPanel plUsername;
    private JPanel plMain;
    private JPanel plSaves;
    private JPanel plButtons;
    private JButton btnLogin;
    private JButton btnLoadSave;
    private JButton btnDeleteSave;
    private JPanel plSave1;
    private JButton btnLogout;
    private JButton btnBack;
    public JPasswordField txtPassword;
    private JButton btnCreateSave;
    private JRadioButton rdbSave1;
    private JRadioButton rdbSave2;
    private JRadioButton rdbSave3;
    private JLabel lbSave1Level;
    private JTextField txtSave1;
    private JTextField txtSave1Level;
    private JPanel plSave1Level;
    private JPanel plSave1Score;
    private JLabel lbSave1Score;
    private JTextField txtSave1Score;
    private JPanel plSave1Relic;
    private JLabel lbSave1Relic;
    private JTextField txtSave1Relic;
    private JTextField txtSave2;
    private JTextField txtSave3;
    private JLabel lbSave2Level;
    private JTextField txtSave2Level;
    private JLabel lbSave2Score;
    private JTextField txtSave2Score;
    private JLabel lbSave2Relic;
    private JTextField txtSave2Relic;
    private JPanel plSave2;
    private JPanel plSave3;
    private JLabel lbSave3Level;
    private JTextField txtSave3Level;
    private JLabel lbSave3Score;
    private JTextField txtSave3Score;
    private JLabel lbSave3Relic;
    private JTextField txtSave3Relic;

    ArrayList<Player> playerSaves = new ArrayList<Player>();
    int numberOfPlayerSaves;

    String username;
    String password;


    GameLogin()
    {
        this.setTitle("Login");
        Image gameIcon = Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\OtherImages\\GameIcon.png"));
        this.setIconImage(gameIcon);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(700, 700);
        this.setLayout(null);
        this.setLocation(100, 100);
        this.setContentPane(this.plMain);
        DungeonQuest.ChangeJOptionPaneFontType("Plain");

        SetUpFrame();
        this.setVisible(true);
    }
    public void SetUpFrame()
    {
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Close();
            }
        });
        rdbSave1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rdbSave1.isSelected())
                {
                    rdbSave2.setSelected(false);
                    rdbSave3.setSelected(false);
                }
            }
        });
        rdbSave2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rdbSave2.isSelected())
                {
                    rdbSave1.setSelected(false);
                    rdbSave3.setSelected(false);
                }
            }
        });
        rdbSave3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rdbSave3.isSelected())
                    {
                        rdbSave2.setSelected(false);
                        rdbSave1.setSelected(false);
                     }

            }
        });
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login();
            }
        });
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Logout();


            }
        });
        btnLoadSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rdbSave1.isSelected())
                {
                    LoadGame(playerSaves.get(0));
                }
                else if (rdbSave2.isSelected())
                {
                    LoadGame(playerSaves.get(1));
                }
                else if (rdbSave3.isSelected())
                {
                    LoadGame(playerSaves.get(2));
                }
                Close();
            }
        });
        btnCreateSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateNewGame();
            }
        });
        btnDeleteSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteCurrentSave();
            }
        });
    }
    public void Close()
    {
        this.dispose();
    }
    public void Login()
    {
        String unValue = txtUsername.getText();
        Player newPlayer;
        char pValueChar[] = txtPassword.getPassword();
        String pValueString ="";
        for (int i = 0;i< pValueChar.length;i++)
        {
            pValueString = pValueString + pValueChar[i];
        }
        username = unValue;
        password = pValueString;
        if (unValue.isBlank())
        {
            JOptionPane.showMessageDialog(null,"Please enter a username!");
        }
        else
        {
            for (int i=0;i<3;i++)
            {
                newPlayer = new Player(unValue,i + 1);
                if (newPlayer.password != null)
                {
                    playerSaves.add(newPlayer);
                    numberOfPlayerSaves ++;
                    if (i ==0)
                    {
                        rdbSave1.setEnabled(true);
                    }
                    else if (i ==1)
                    {
                        rdbSave2.setEnabled(true);
                    }
                    else if (i ==2)
                    {
                        rdbSave3.setEnabled(true);
                    }
                }
            }
            if (numberOfPlayerSaves == 0)
            {
                JOptionPane.showMessageDialog(null,"Account does not exist! Please try again.");
            }
            else
            {
                if (pValueString.equals(playerSaves.get(0).password))
                {
                    DisplayPlayerSaves();
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"Password incorrect! Please try again.");

                }
            }

        }
    }
    public void DisplaySave(Player playerSave)
    {
        ClearAllRadioButtons();
        int fileNumber = playerSave.fileNumber;
        JTextField lvl,score,relic;
        JRadioButton save;
        if (playerSave.password != null)
        {
            switch (fileNumber)
            {
                case 1: lvl = txtSave1Level;
                        score = txtSave1Score;
                        relic = txtSave1Relic;
                        save = rdbSave1;
                        break;
                case 2: lvl = txtSave2Level;
                        score = txtSave2Score;
                        relic = txtSave2Relic;
                        save = rdbSave2;
                        break;
                case 3: lvl = txtSave3Level;
                        score = txtSave3Score;
                        relic = txtSave3Relic;
                        save = rdbSave3;
                        break;
                default: lvl = null;
                        score = null;
                        relic = null;
                        save = null;
            }
            lvl.setText(playerSave.level + "");
            score.setText(playerSave.score + "");
            relic.setText(playerSave.myRelics.size() + "");
            save.setSelected(true);
            save.setEnabled(true);
        }
    }
    public void ChangeStateOfControls(boolean state)
    {
        if (state == true)
        {
            btnLogin.setEnabled(false);

        }
        else if (state == false)
        {
            btnLogin.setEnabled(true);

        }
        btnCreateSave.setEnabled(state);
        btnDeleteSave.setEnabled(state);
        btnLoadSave.setEnabled(state);
        btnLogout.setEnabled(state);

    }
    public void Logout()
    {
        txtUsername.setText("");
        txtPassword.setText("");
        txtSave1Level.setText("");
        txtSave1Score.setText("");
        txtSave1Relic.setText("");
        txtSave2Level.setText("");
        txtSave2Score.setText("");
        txtSave2Relic.setText("");
        txtSave3Level.setText("");
        txtSave3Score.setText("");
        txtSave3Relic.setText("");
        ClearAllRadioButtons();
        DisableRadioButtons();
        ChangeStateOfControls(false);
        username = "";
        password = "";
        playerSaves = null;


    }
    public void LoadGame(Player myPlayer)
    {
        DungeonQuest DQ = new DungeonQuest(myPlayer);
    }
    public void CreateNewGame()
    {
        int fileNumber;
        ArrayList<Integer> possibleFileNumbers = new ArrayList<Integer>();
        int count =0;
        Player newPlayer;
        for (int i = 1;i < 4; i++)
        {
            possibleFileNumbers.add(i);
        }
        if (numberOfPlayerSaves==0)
        {
            //here for making enw account
        }
        else
        {
            for (int i =0;i < numberOfPlayerSaves;i++)
            {
                count =0;
                while (count < possibleFileNumbers.size())
                {
                    if (playerSaves.get(i).fileNumber == possibleFileNumbers.get(count))
                    {
                        possibleFileNumbers.remove((Integer) count);
                    }
                    else
                    {
                        count++;
                    }

                }
            }
            if (possibleFileNumbers.isEmpty())
            {
                JOptionPane.showMessageDialog(null,"You cannot have more than 3 player saves! Please delete one if you wish to make a new save.");
            }
            else
            {
                fileNumber = possibleFileNumbers.get(0);
                newPlayer = new Player(txtUsername.getText(),fileNumber,password);
                DisplaySave(newPlayer);
                playerSaves.add(newPlayer);
                numberOfPlayerSaves++;
                newPlayer.SavePlayer();
            }
        }
    }
    public void ClearAllRadioButtons()
    {
        rdbSave1.setSelected(false);
        rdbSave2.setSelected(false);
        rdbSave3.setSelected(false);
    }
    public void DeleteCurrentSave()
    {
        Player fileToDelete;
        int reply;
        int saveNumber = 0;
        if (rdbSave1.isSelected())
        {
            saveNumber = 1;
        }
        else if (rdbSave2.isSelected())
        {
            saveNumber = 2;

        }
        else if (rdbSave3.isSelected())
        {
            saveNumber = 3;

        }
        if (saveNumber == 0)
        {
            JOptionPane.showMessageDialog(null,"No file to delete!");
        }
        else
        {
            fileToDelete = playerSaves.get((saveNumber - 1));
            reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete save number " + saveNumber + "?", "", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION)
            {
                DeleteFile(fileToDelete);
            }
            else
            {
                JOptionPane.showMessageDialog(null,"File has NOT been deleted!");
            }
        }
    }
    public void DeleteFile(Player playerSave)
    {
        String filePath = DungeonQuest.directory + "\\Players\\" + playerSave.name + "-" + playerSave.fileNumber + ".txt";
        File fileToDelete = new File(filePath);
        if (fileToDelete.delete())
        {
            JOptionPane.showMessageDialog(null,"File has been deleted!");
            playerSaves.remove(playerSave);
            numberOfPlayerSaves--;
            DisplayPlayerSaves();
        }
    }
    public void DisplayPlayerSaves()
    {
        ClearPlayerSaves();
        for (int i =0;i<numberOfPlayerSaves;i++)
        {
            DisplaySave(playerSaves.get(i));
        }
        ChangeStateOfControls(true);
    }
    public void ClearPlayerSaves()
    {
        txtSave1Level.setText("");
        txtSave1Score.setText("");
        txtSave1Relic.setText("");
        txtSave2Level.setText("");
        txtSave2Score.setText("");
        txtSave2Relic.setText("");
        txtSave3Level.setText("");
        txtSave3Score.setText("");
        txtSave3Relic.setText("");
        ClearAllRadioButtons();
        DisableRadioButtons();
    }
    public void DisableRadioButtons()
    {
        rdbSave1.setEnabled(false);
        rdbSave2.setEnabled(false);
        rdbSave3.setEnabled(false);
    }


}
