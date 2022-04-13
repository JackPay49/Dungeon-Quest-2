import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class GameMenu extends JFrame {
    JLabel lbBackgroundImage = new JLabel();
    JLayeredPane pane = new JLayeredPane();

    int width = 960;
    int height = 540;
    Dimension frameSize = new Dimension(width, height);

    Font largeFont = new Font("Candara",Font.BOLD,50);
    Font smallerFont = new Font("Candara",Font.BOLD,30);

    Color fontColour = new Color(255,255,255);
    Color buttonColour = new Color(44,62,95);

    GameMenu() {
        //Game Menu
        //--------------------------------------------------------------------------------------------------------------
        this.setTitle("Dungeon Quest");
        Image gameIcon = Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\OtherImages\\GameIcon.png"));
        this.setIconImage(gameIcon);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(200,200,width,height);
        this.setMinimumSize(frameSize);
        this.setMaximumSize(frameSize);
        this.setPreferredSize(frameSize);
        this.setResizable(false);
        this.setLayout(null);
        //--------------------------------------------------------------------------------------------------------------
        SetUpGameMenu();
        this.setVisible(true);
        DungeonQuest.ChangeJOptionPaneFontType("Plain");
    }

    public void SetUpGameMenu() {
        pane.setBounds(0,0,width,height);
        pane.setVisible(true);

        Image backgroundImage = Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\OtherImages\\Menu Background.gif"));
        lbBackgroundImage = new JLabel(new ImageIcon(backgroundImage));
        lbBackgroundImage.setBounds(0, 0, width, height);
        lbBackgroundImage.setVisible(true);
        pane.add(lbBackgroundImage,JLayeredPane.DEFAULT_LAYER);

        JLabel lbTitle = new JLabel("Dungeon Quest 2");
        lbTitle.setFont(largeFont);
        lbTitle.setForeground(fontColour);
        lbTitle.setBounds(275,20,500,50);
        pane.add(lbTitle,JLayeredPane.DRAG_LAYER);

        JButton btnPlayGame = new JButton("Play Game");
        btnPlayGame.setBounds(300,100,300,50);
        btnPlayGame.setFont(smallerFont);
        btnPlayGame.setForeground(fontColour);
        btnPlayGame.setBackground(buttonColour);
        pane.add(btnPlayGame,JLayeredPane.PALETTE_LAYER);
        btnPlayGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login();

            }
        });

        JButton btnCreateNewGame = new JButton("Create New Game");
        btnCreateNewGame.setBounds(300,225,300,50);
        btnCreateNewGame.setFont(smallerFont);
        btnCreateNewGame.setForeground(fontColour);
        btnCreateNewGame.setBackground(buttonColour);
        pane.add(btnCreateNewGame,JLayeredPane.PALETTE_LAYER);
        btnCreateNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateNewGame();
            }
        });

        JButton btnQuitGame = new JButton("Quit Game");
        btnQuitGame.setBounds(300,350,300,50);
        btnQuitGame.setFont(smallerFont);
        btnQuitGame.setForeground(fontColour);
        btnQuitGame.setBackground(buttonColour);
        pane.add(btnQuitGame,JLayeredPane.PALETTE_LAYER);
        btnQuitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        this.add(pane);

    }

    public static void main(String[] arg)
    {
        GameMenu GM = new GameMenu();

    }
    public void CreateNewGame()
    {
        CreateNewGame CWG = new CreateNewGame();
    }
    public static void Login()
    {
        GameLogin GL = new GameLogin();
    }
    public static void Login(String username,String password)
    {
        GameLogin GL = new GameLogin();
        GL.txtUsername.setText(username);
        GL.txtPassword.setText(password);
        GL.Login();
    }
}
