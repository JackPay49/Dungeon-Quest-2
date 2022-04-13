import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CinematicScreen extends JFrame {
    private JPanel plMain;
    private JLabel lbMain;
    private JLabel lbOtherIcon;
    private JButton btnOption1;
    private JLabel lbSkip;
    private JButton btnOption2;
    private JPanel plIcon;
    private JPanel plCentre;
    private JPanel plButtons;
    private JPanel plSkiplb;

    Action moveOnAction;
    Action menuAction;

    String type;//Credits, Intro, Outro
    Player myPlayer;
    GameBoard GB;

    CinematicScreen(GameBoard gbValue, Player pValue,String tValue)
    {
        int relicNumber = -1;
        type = tValue;
        myPlayer = pValue;
        GB = gbValue;
        this.setTitle(type);
        Image gameIcon = Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\OtherImages\\MenuIcon.png"));
        this.setIconImage(gameIcon);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(1000, 750);
        this.setLayout(null);
        this.setLocation(900, 100);
        this.setVisible(true);
        SetUpButtons();
        SetUpControls();
        this.setContentPane(this.plMain);
        this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
        plMain.grabFocus();
        if ((type.equals("Intro"))|(type.equals("Idol"))|(type.contains("Relic")))
        {
            DungeonQuest.FreezeAllEntitiesIndefinitely();
            if (type.contains("Relic"))
            {
                lbOtherIcon.setVisible(true);
                relicNumber = Character.getNumericValue(tValue.charAt(tValue.length() - 1));
                lbOtherIcon.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(DungeonQuest.directory + "\\Cinematics\\Relic\\Icons\\" + relicNumber + ".png")));//Look into. The relicnumber is wrong
                if (myPlayer.numberOfRelicsCollected == 0)
                {
                    lbMain.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(DungeonQuest.directory + "\\Cinematics\\Relic\\FirstRelic.gif")));
                }
                else
                {
                    lbMain.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(DungeonQuest.directory + "\\Cinematics\\Relic\\GeneralRelicScreen.gif")));
                }
            }
        }
        else if (type.contains("Level"))
        {
            lbOtherIcon.setVisible(true);
            DungeonQuest.FreezeAllEntitiesIndefinitely();
            lbMain.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(DungeonQuest.directory + "\\Cinematics\\" + type + "\\1.gif")));
            lbOtherIcon.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(DungeonQuest.directory + "\\Cinematics\\" + type + "\\Icon.gif")));
        }
        if (((type.contains("Relic")) == false)&((type.contains("Level")) == false))
        {
            lbMain.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(DungeonQuest.directory + "\\Cinematics\\General\\" + type + ".gif")));
        }
    }
    public void SetUpControls()
    {
        moveOnAction = new MoveOnAction();
        menuAction = new MenuAction();

        plMain.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "moveOnAction");
        plMain.getActionMap().put("moveOnAction", moveOnAction);
        if (type.contains("Relic"))
        {
            plMain.getInputMap().put(KeyStroke.getKeyStroke("E"), "menuAction");
            plMain.getActionMap().put("menuAction", menuAction);
        }
    }
    public void SetUpButtons()
    {
        if (type.equals("Level60"))
        {
            btnOption1.setVisible(true);
            btnOption2.setVisible(true);
            btnOption1.setText("Drop all Relics into the Fountain");
            btnOption2.setText("Walk away from the fountain");
            btnOption1.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    lbMain.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(DungeonQuest.directory + "\\Cinematics\\" + type + "\\2.gif")));
                    DisableButtons();
                    plMain.grabFocus();
                    myPlayer.numberOfRelicsCollected = 0;
                    myPlayer.myRelics = new Relic[0];
                    Relic sixthRelic = new Relic(5,false);
                    myPlayer.CollectNewRelic(sixthRelic);
                    myPlayer.SetCurrentRelic(sixthRelic.toolID,GB);

                }
            });
            btnOption2.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    lbMain.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(DungeonQuest.directory + "\\Cinematics\\" + type + "\\3.gif")));
                    DisableButtons();
                    plMain.grabFocus();
                }
            });

        }
    }
    public void DisableButtons()
    {
        btnOption1.setVisible(false);
        btnOption2.setVisible(false);
        btnOption1.setEnabled(false);
        btnOption2.setEnabled(false);
    }
    public class MoveOnAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            Close();
            if (type.equals("Outro"))
            {
                GameMenu GM = new GameMenu();
            }
            else if ((type.equals("Intro"))|(type.equals("Idol"))|(type.contains("Level")))
            {
                DungeonQuest.UnfreezeAllEntities();
            }
        }
    }
    public class MenuAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e){
            myPlayer.ChangeAppearance(0);
            PlayerMenu PM = new PlayerMenu(myPlayer, GB);
            DungeonQuest.PlaySound(DungeonQuest.directory + "\\OtherSounds\\MenuOpen.wav");
            Close();
        }
    }
    public void Close()
    {
        this.dispose();
    }
}
