import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerMenu extends JFrame {

    private JPanel mainPanel;
    private JPanel topLeftPanel;
    private JTextField txtName;
    private JTextField txtLevel;
    private JTextField txtScore;
    private JLabel lbHealthPoint1;
    private JLabel lbHealthPoint2;
    private JLabel lbHealthPoint3;
    private JLabel lbHealthPoint4;
    private JLabel playerIcon;
    private JPanel panelPlayerIcon;
    private JButton btnHelp;
    private JButton btnClose;
    private JLabel lbHealthPoint5;
    private JButton btnQuit;
    private JPanel midLeftPanel;
    private JPanel midRightPanel;
    private JLabel lbEnergyPoint4;
    private JLabel lbEnergyPoint3;
    private JLabel lbEnergyPoint2;
    private JLabel lbEnergyPoint1;
    private JButton btnRelic1;
    private JButton btnRelic2;
    private JButton btnRelic3;
    private JButton btnRelic4;
    private JButton btnRelic5;
    private JButton btnRelic6;
    private JPanel relicPanel;
    private JPanel descriptionPanel;
    private JTextField txtRelicName;
    private JLabel lbRelicName;
    private JLabel lbRelicDescription;
    private JPanel panelRelicDescription;
    private JPanel panelRelicAction;
    private JLabel lbRelicAction;
    private JPanel panelRelicName;
    private JTextArea txtRelicAction;
    private JTextArea txtRelicDescription;
    private JLabel lbEnergyPoint5;
    private JLabel lbEnergyPoint6;
    private JLabel lbHealthPoint6;
    private JLabel lbHealthPoint7;
    private JButton btnSword1;
    private JButton btnSword2;
    private JButton btnSword3;
    private JTextField txtDamage;
    private JButton btnControls;
    private JLabel lbSwordIcon;
    private JTextArea txtSwordAction;
    private JPanel generalPlayerStatsPanel;
    private JLabel lbName;
    private JLabel lbLevel;
    private JLabel lbScore;
    private JButton btnSword4;
    private JButton btnSword5;
    private JButton btnSword6;
    private JTextField txtSwordName;
    private JPanel panelButtons;
    private JPanel panelSwordDetails;
    private JPanel panelDamage;
    private JLabel lbDamage;
    private JPanel panelInfo;
    private JPanel panelSwordImage;
    private JPanel panelSwordName;
    private JLabel lbSword;
    private JPanel panelRelicDetails;
    private JPanel panelSwordDescription;
    private JLabel lbSwordDescription;
    private JTextArea txtSwordDescription;
    private JPanel panelShieldDetails;
    private JLabel lbShieldAction;
    private JLabel lbShieldName;
    private JTextField txtShieldName;
    private JLabel lbShieldDescription;
    private JTextArea txtShieldDescription;
    private JLabel lbSwordAction;
    private JPanel panelShieldName;
    private JPanel panelShieldAction;
    private JPanel panelShieldSelect;
    private JTextArea txtShieldAction;
    private JButton btnShield1;
    private JButton btnShield2;
    private JButton btnShield3;

    Player myPlayer;
    GameBoard GB;
    Color backgroundColour = new Color(74,72,72);
    Color highlightedColour = new Color(187,187,187);

    GameDialog help = new GameDialog("Help","Help");
    GameDialog controls = new GameDialog("Help","Controls");




    PlayerMenu(Player pValue, GameBoard gbValue)
    {
        myPlayer = pValue;
        GB = gbValue;
        DisplayPlayerStats();
        this.setTitle("Player Menu");
        Image gameIcon = Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\OtherImages\\MenuIcon.png"));
        this.setIconImage(gameIcon);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(1600, 750);
        this.setLayout(null);
        this.setLocation(600, 100);
        this.setVisible(true);
        SetUpControls();
        if (myPlayer.currentRelic != null)
        {
            SelectRelic(myPlayer.currentRelic.toolID);
        }
        SelectSword(myPlayer.currentSword);
        SelectShield(myPlayer.currentShield);
        this.setContentPane(this.mainPanel);
    }
    public void DisplayPlayerStats()
    {
        txtName.setText(myPlayer.name);
        txtLevel.setText(myPlayer.level + "");
        txtScore.setText(myPlayer.score + "");
        DisplayHealth();
        DisplayEnergyLevel();
        DisplayRelics();
        DisplaySwords();
        DisplayShields();
    }
    public void SelectSword(Sword currentSword)
    {
        JButton currentButton = new JButton();
        if (myPlayer.GetOneSword(currentSword.toolID) == null)
        {
            DungeonQuest.PlaySound(DungeonQuest.directory + "\\Tool\\w\\fail.wav");
        }
        else {
            myPlayer.SetCurrentSword(GB, currentSword.toolID);
            txtSwordName.setText(currentSword.name);
            txtSwordDescription.setText(currentSword.description);
            txtSwordAction.setText(currentSword.action);
            txtDamage.setText(String.valueOf(currentSword.damage));
            DisplaySwords();
            switch (currentSword.toolID)
            {
                case 0: currentButton = btnSword1;
                    break;
                case 1: currentButton = btnSword2;
                    break;
                case 2: currentButton = btnSword3;
                    break;
                case 3: currentButton = btnSword4;
                    break;
                case 4: currentButton = btnSword5;
                    break;
                case 5: currentButton = btnSword6;
                    break;
            }
            currentButton.setIcon(currentSword.selectedSymbol);
            currentButton.setBackground(highlightedColour);
        }
    }
    public void DisplaySwords()
    {
        Icon unknownSword = new ImageIcon(Toolkit.getDefaultToolkit().getImage(DungeonQuest.directory + "\\Tool\\w\\Unknown.png"));
        JButton[] swords = {btnSword1,btnSword2,btnSword3,btnSword4,btnSword5,btnSword6};
        for (int i = 0; i < DungeonQuest.numberOfSwords;i++)
        {
            swords[i].setBackground(backgroundColour);
            if (myPlayer.GetOneSword(i) == null)
            {
                swords[i].setIcon(unknownSword);
            }
            else
            {
                swords[i].setIcon(myPlayer.GetOneSword(i).symbol);
            }
        }
    }

    public void SelectShield(Shield currentShield) {
        JButton currentButton = new JButton();
        if (myPlayer.GetOneShield(currentShield.toolID) == null) {
            DungeonQuest.PlaySound(DungeonQuest.directory + "\\Tool\\h\\fail.wav");
        } else {
            myPlayer.SetCurrentShield(GB, currentShield.toolID);
            txtShieldName.setText(currentShield.name);
            txtShieldDescription.setText(currentShield.description);
            txtShieldAction.setText(currentShield.action);
            DisplayShields();
            switch (currentShield.toolID)
            {
                case 0: currentButton = btnShield1;
                        break;
                case 1: currentButton = btnShield2;
                        break;
                case 2: currentButton = btnShield3;
                        break;
            }
            currentButton.setIcon(currentShield.selectedSymbol);
            currentButton.setBackground(highlightedColour);
        }
    }
        public void DisplayShields()
        {
            Icon unknownShield = new ImageIcon(Toolkit.getDefaultToolkit().getImage(DungeonQuest.directory + "\\Tool\\h\\Unknown.png"));
            JButton[] shields = {btnShield1,btnShield2,btnShield3};
            for (int i = 0; i < DungeonQuest.numberOfShields;i++)
            {
                shields[i].setBackground(backgroundColour);
                if (myPlayer.GetOneShield(i) == null)
                {
                    shields[i].setIcon(unknownShield);
                }
                else
                {
                    shields[i].setIcon(myPlayer.GetOneShield(i).symbol);
                }
            }
        }
    public void DisplayHealth()
    {
        int health = myPlayer.health;
        Icon heartFull = new ImageIcon(Toolkit.getDefaultToolkit().getImage(myPlayer.directory + "\\Heart\\Full.png"));
        Icon heartEmpty = new ImageIcon(Toolkit.getDefaultToolkit().getImage(myPlayer.directory + "\\Heart\\Empty.png"));
        JLabel[] healthPoints = {lbHealthPoint1,lbHealthPoint2,lbHealthPoint3,lbHealthPoint4,lbHealthPoint5,lbHealthPoint6,lbHealthPoint7};
        for (int i=0;i< myPlayer.maxHealth;i++)
        {
            healthPoints[i].setIcon(heartEmpty);
        }
        switch (health)
        {//Doesn't include break statements so that each lower energy point is filled up
            case 7: lbHealthPoint7.setIcon(heartFull);
            case 6: lbHealthPoint6.setIcon(heartFull);
            case 5: lbHealthPoint5.setIcon(heartFull);
            case 4: lbHealthPoint4.setIcon(heartFull);
            case 3: lbHealthPoint3.setIcon(heartFull);
            case 2: lbHealthPoint2.setIcon(heartFull);
            case 1: lbHealthPoint1.setIcon(heartFull);
        }
    }
    public void DisplayEnergyLevel()
    {
        int energy = myPlayer.energyLevel;
        Icon energyFull = new ImageIcon(Toolkit.getDefaultToolkit().getImage(myPlayer.directory + "\\Energy\\Full.png"));
        Icon energyEmpty = new ImageIcon(Toolkit.getDefaultToolkit().getImage(myPlayer.directory + "\\Energy\\Empty.png"));
        JLabel[] energyPoints = {lbEnergyPoint1,lbEnergyPoint2,lbEnergyPoint3,lbEnergyPoint4,lbEnergyPoint5,lbEnergyPoint6};
        for (int i=0;i<myPlayer.maxEnergyLevel;i++)
        {
            energyPoints[i].setIcon(energyEmpty);
        }
        switch (energy)
        {//Doesn't include break statements so that each lower energy point is filled up
            case 6: lbEnergyPoint6.setIcon(energyFull);
            case 5: lbEnergyPoint5.setIcon(energyFull);
            case 4: lbEnergyPoint4.setIcon(energyFull);
            case 3: lbEnergyPoint3.setIcon(energyFull);
            case 2: lbEnergyPoint2.setIcon(energyFull);
            case 1: lbEnergyPoint1.setIcon(energyFull);
        }
    }

    public void SetUpControls()
    {
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Close();
            }

        });
        btnQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnRelic1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectRelic(0);
            }
        });
        btnRelic2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectRelic(1);
            }
        });
        btnRelic3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectRelic(2);
            }
        });
        btnRelic4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectRelic(3);
            }
        });
        btnRelic5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectRelic(4);
            }
        });
        btnRelic6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectRelic(5);
            }
        });
        btnHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                help.DisplayContent();
            }
        });
        btnControls.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controls.DisplayContent();
            }
        });
        btnSword1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectSword(new Sword(0));
            }
        });
        btnSword2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectSword(new Sword(1));
            }
        });
        btnSword3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectSword(new Sword(2));
            }
        });
        btnSword4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectSword(new Sword(3));
            }
        });
        btnSword5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectSword(new Sword(4));
            }
        });
        btnSword6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectSword(new Sword(5));
            }
        });
        btnShield1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectShield(new Shield(0));
            }
        });
        btnShield2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectShield(new Shield(1));
            }
        });
        btnShield3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectShield(new Shield(2));
            }
        });

    }
    public void Close()
    {
        this.dispose();
    }

    public void SelectRelic(int relicNumber)
    {
        JButton currentButton = new JButton();
        if (myPlayer.GetOneRelic(relicNumber) == null)
        {
            DungeonQuest.PlaySound(DungeonQuest.directory + "\\Tool\\r\\fail.wav");
        }
        else
        {
            myPlayer.SetCurrentRelic(relicNumber, GB);
            txtRelicName.setText(myPlayer.currentRelic.name);
            txtRelicDescription.setText(myPlayer.currentRelic.description);
            txtRelicAction.setText(myPlayer.currentRelic.action);
            DisplayRelics();
            switch (relicNumber)
            {
                case 0: currentButton = btnRelic1;
                        break;
                case 1: currentButton = btnRelic2;
                        break;
                case 2: currentButton = btnRelic3;
                        break;
                case 3: currentButton = btnRelic4;
                        break;
                case 4: currentButton = btnRelic5;
                        break;
                case 5: currentButton = btnRelic6;
                        break;
            }
            currentButton.setIcon(myPlayer.currentRelic.selectedSymbol);
            currentButton.setBackground(highlightedColour);
        }


    }
    public void DisplayRelics()
    {
        Icon unknownRelic = new ImageIcon(Toolkit.getDefaultToolkit().getImage(DungeonQuest.directory + "\\Tool\\r\\Unknown.png"));
        JButton[] relicButtons = {btnRelic1,btnRelic2,btnRelic3,btnRelic4,btnRelic5,btnRelic6};
        for (int i =0; i < DungeonQuest.numberOfRelics;i++)
        {
            relicButtons[i].setBackground(backgroundColour);
            if (myPlayer.GetOneRelic(i) == null)
            {
                relicButtons[i].setIcon(unknownRelic);
            }
            else
            {
                relicButtons[i].setIcon(myPlayer.GetOneRelic(i).symbol);
            }
        }
    }





}
