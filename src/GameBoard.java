import javax.swing.*;
import javax.swing.border.Border;
import javax.xml.stream.Location;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class GameBoard extends JFrame {
    JLabel lbBackgroundImage;

    Item exitTile;


    ArrayList<Point> unavailableSpaces = new ArrayList<Point>();

    JLayeredPane pane = new JLayeredPane();

    ArrayList<Projectile> allProjectiles = new ArrayList<Projectile>();

    JLabel currentRelic;

    JLabel currentSword;
    Sword playerSword = new Sword(1);
    JLabel currentShield;
    Shield playerShield = new Shield(1);


    ArrayList<Enemy> deadEnemies = new ArrayList<Enemy>();

    int difficultyLevel;
    boolean won = false;

    boolean skipRound = false;

    Point playerPosition = new Point();
    int levelNumber;

    char type = 'e';//such as key level(k), enemy level(e), boss level(b), fountain level(f)
    String size;//this is to randomly decide how many enemies
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    ArrayList<Enemy> allies = new ArrayList<Enemy>();
    int origin[] = new int[2];
    int width;
    int height;
    int xDimension;
    int yDimension;

    ArrayList<Item> otherEntities = new ArrayList<Item>();//used to store the Key, exit tile, etc

    ArrayList<GameDialog> allDialog = new ArrayList<GameDialog>();
    boolean storyDone = false;

    ArrayList<Point> allTakenPoints = new ArrayList<Point>();

    int numberOfInteractSpaces = 0;
    ArrayList<TakenPoint> allInteractSpaces = new ArrayList<TakenPoint>();




    GameBoard() {
        //Gameboard
        //--------------------------------------------------------------------------------------------------------------
        this.setTitle("Game Board");
        Image gameIcon = Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\OtherImages\\GameIcon.png"));
        this.setIconImage(gameIcon);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(750, 750);
        this.setLayout(null);
        this.setLocation(0, 0);
        this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
        pane.setBounds(0,0,width,height);
        this.add(pane);
        origin[0] = 25;
        origin[1] = 100;
        //--------------------------------------------------------------------------------------------------------------
    }
    public void SetUpCurrentRelicIcon()
    {
        int borderSize = 5;
        Color borderColor = new Color(74,72,72);
        Border greyborder = BorderFactory.createMatteBorder(borderSize,borderSize,borderSize,borderSize, borderColor);
        currentRelic = new JLabel();
        currentRelic.setBounds(((origin[0] * 2) + (xDimension * 100)),2 * (50 + (borderSize * 2)),50 + (borderSize * 2),50 + (borderSize * 2));
        currentRelic.setBackground(new Color(160,160,160));
        currentRelic.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(DungeonQuest.directory + "\\Relic\\Unknown.png")));
        currentRelic.setBorder(greyborder);
        currentRelic.setVisible(true);
        this.add(currentRelic);
    }
    public void SetUpCurrentSwordIcon()
    {
        int borderSize = 5;
        Color borderColor = new Color(74,72,72);
        Border greyborder = BorderFactory.createMatteBorder(borderSize,borderSize,borderSize,borderSize, borderColor);
        currentSword = new JLabel();
        currentSword.setBounds(((origin[0] * 2) + (xDimension * 100)),0,50 + (borderSize * 2),50 + (borderSize * 2));
        currentSword.setBackground(new Color(160,160,160));
        currentSword.setIcon(playerSword.selectedSymbol);
        currentSword.setBorder(greyborder);
        currentSword.setVisible(true);
        this.add(currentSword);
    }
    public void SetUpCurrentShieldIcon()
    {
        int borderSize = 5;
        Color borderColor = new Color(74,72,72);
        Border greyborder = BorderFactory.createMatteBorder(borderSize,borderSize,borderSize,borderSize, borderColor);
        currentShield = new JLabel();
        currentShield.setBounds(((origin[0] * 2) + (xDimension * 100)),50 + (borderSize * 2),50 + (borderSize * 2),50 + (borderSize * 2));
        currentShield.setBackground(new Color(160,160,160));
        currentShield.setIcon(playerShield.selectedSymbol);
        currentShield.setBorder(greyborder);
        currentShield.setVisible(true);
        this.add(currentShield);
    }
    public void SetCurrentRelicIcon(int relicNumber,boolean kingsoul)
    {
        if ((relicNumber == 5) & (kingsoul))
        {
            currentRelic.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(DungeonQuest.directory + "\\Tool\\r\\" + relicNumber + "KingsoulSelected.png")));
        }
        else
        {
            currentRelic.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(DungeonQuest.directory + "\\Tool\\r\\" + relicNumber + "Selected.png")));
        }
    }
    public void SetCurrentSwordIcon(Sword sValue)
    {
        playerSword = sValue;
        currentSword.setIcon(sValue.selectedSymbol);
    }
    public void SetCurrentShieldIcon(Shield sValue)
    {
        playerShield = sValue;
        currentShield.setIcon(sValue.selectedSymbol);
    }
    public void SetupBoard() //hierachy of entity placement. PLayer, enemies, keys, exit tiles, board
    {
        Random r = new Random();
        int randomNumber = r.nextInt(4) + 1;
        Image backgroundImage;
        exitTile = new Item(this,"Exit");
        if ((type == 'e')|(type=='k')|(type=='c'))
        {
            backgroundImage = Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\GameBoard\\Board" + randomNumber + ".png"));
            lbBackgroundImage = new JLabel(new ImageIcon(backgroundImage));
            LoadInBoardInfo(randomNumber);
            if (type =='k')//now add the key[s]
            {
                r = new Random();
                randomNumber = r.nextInt(2) + 1;
                for (int i =0;i<randomNumber;i++)
                {
                    Item key = new Item(this,"Key");
                    otherEntities.add(key);
                    key.ChangeAppearance(0);
                    key.RandomlyPlace(this);
                }
            }
            else if (type == 'c')
            {
                exitTile.dead = true;
                exitTile.ChangeToAfterState();
                Item chest = new Item(this,"LockedChest");
                otherEntities.add(chest);
                chest.ChangeAppearance(0);
                chest.SetLocation((origin[0] + (100 * (xDimension -1))),(origin[1] + (100 * (yDimension -1))));

                Item chestKey = new Item(this,"ChestKey");
                otherEntities.add(chestKey);
                chestKey.ChangeAppearance(0);
                chestKey.RandomlyPlace(this);
            }
            exitTile.RandomlyPlace(this);
        }
        else if (type == 'i')//setup Idol level
        {
            exitTile.dead = true;
            exitTile.ChangeToAfterState();

            backgroundImage = Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\GameBoard\\BoardIdol.png"));
            lbBackgroundImage = new JLabel(new ImageIcon(backgroundImage));
            LoadInBoardInfo(50);

            exitTile.SetLocation((origin[0] + ((xDimension -1) * 100)),(origin[1] + (100 * (yDimension -1))));

            Item idol = new Item(this,"Idol");
            otherEntities.add(idol);
            idol.ChangeAppearance(0);

            Item brazier1 = new Item(this,"Brazier");
            otherEntities.add(brazier1);
            brazier1.icon.setLocation(425,500);
            Item brazier2 = new Item(this,"Brazier");
            otherEntities.add(brazier2);
            brazier2.icon.setLocation(225,500);
        }
        else if (type == 'b')
        {
            backgroundImage = Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\GameBoard\\BoardBoss.png"));
            lbBackgroundImage = new JLabel(new ImageIcon(backgroundImage));
            LoadInBoardInfo(100);
            exitTile.SetLocation((origin[0] + ((xDimension -1) * 100)),(origin[1] + (100 * (yDimension -1))));
            Item chest = new Item(this,"Chest");
            otherEntities.add(new Item(this,"Chest"));
            chest.ChangeAppearance(0);
            chest.SetLocation((origin[0] + ((xDimension -1) * 100)),(origin[1] + (100 * 2)));
        }
        GenerateTraps();
        GenerateEnemies();
        SetUpFinalBoardParts();

    }
    public void SetUpLevel()
    {
        int levelMod = levelNumber % 10;
        Random r = new Random();
        int randomNumber = r.nextInt(13);
        FindDifficultyLevel(levelNumber);
        if (levelNumber == 0)
        {
            SetUpLevel0();
        }
        else if (levelNumber == 60)
        {
            SetUpLevel60();
        }
        else if (levelNumber == 119)
        {
            SetUpLevel119();
        }
        else if (levelNumber == 120)
        {
            SetUpLevel120();
        }
        else {
            if ((levelMod == 0) & (levelNumber != 0))
            {
                type = 'b';//make boss level if the level number is a multiple of 10
            }
            else
            {
                if ((randomNumber <= 3))
                {
                    type = 'e';
                }
                else if ((randomNumber > 3)|(randomNumber<=7))
                {
                    type = 'k';
                }
                else if ((randomNumber>7)|(randomNumber <=11))
                {
                    type = 'c';
                }
                else if (randomNumber == 12)
                {
                    type = 'i';//Idol level
                }
            }
            SetupBoard();
        }
    }

    public void RefreshGameBoard()
    {
        this.setVisible(true);
    }
    public void GenerateEnemies()
    {
        Random r = new Random();
        int numberOfEnemies = 0 ;
        int noeValue = 0;
        if (type == 'e' | type == 'k' | type == 'c')
        {
            if (size.equals("Small"))
            {
                numberOfEnemies = r.nextInt(1) + 2;
            }
            else if (size.equals("Medium"))
            {
                numberOfEnemies = r.nextInt(3) + 3;
            }
            else if (size.equals("Large"))
            {
                numberOfEnemies = r.nextInt(4) + 5;

            }
            for (int i = 0;i < numberOfEnemies; i ++)
            {
                Enemy newEnemy = new Enemy(this,difficultyLevel);
                enemies.add(newEnemy);
            }
        }
        else if (type == 'i')
        {
            numberOfEnemies = 3;
            for (int i = 0;i<numberOfEnemies;i++)
            {
                Boss newEnemy = new Boss(this,difficultyLevel);
                enemies.add(newEnemy);
            }
        }
        else if (type =='b')
        {
            if ((difficultyLevel == 1) | (difficultyLevel == 2))
            {
                noeValue = 1;
            }
            else if (difficultyLevel == 3)
            {
                noeValue = 2;

            }
            else if (difficultyLevel == 4)
            {
                noeValue = 3;

            }
            for (int i=0;i<noeValue;i++)
            {
                Boss bossEnemy = new Boss(this,difficultyLevel);
                enemies.add(bossEnemy);
            }
        }

    }
    public void GenerateTraps()
    {
        Random r = new Random();
        int numberOfTraps = 0;
        String trapType = "";
        int randomNumber;
        if (type == 'e' | type == 'k' | type == 'c')
        {
            if (size.equals("Small"))
            {
                numberOfTraps = 0;
            }
            else if (size.equals("Medium"))
            {
                numberOfTraps = r.nextInt(3);
            }
            else if (size.equals("Large"))
            {
                numberOfTraps = r.nextInt(4);
            }
            for (int i = 0;i < numberOfTraps;i++)
            {
                r = new Random();
                randomNumber = r.nextInt(2);
                if (randomNumber == 0)
                {
                    trapType = "FireWizard";
                }
                else if (randomNumber == 1)
                {
                    trapType = "ArrowWizard";
                }
                otherEntities.add(new Trap(this,trapType));
            }

        }
    }
    public void SetPlayerPosition(Player myPlayer)
    {
        playerPosition = myPlayer.icon.getLocation();
    }
    public void LoadInBoardInfo(int number)
    {
        String xPosition = "";
        String yPosition = "";
        Point newPoint;
        int halfWayPoint =0;
        int numberOfUnavailableSpaces,numberOfTakenPoints;
        String tempLine;
        BufferedReader br;
        try
        {
            if (number == 100)
            {
                br = new BufferedReader(new FileReader(DungeonQuest.directory +"\\GameBoard\\BoardBoss.txt"));
            }
            else if (number == 50)
            {
                br = new BufferedReader(new FileReader(DungeonQuest.directory +"\\GameBoard\\BoardIdol.txt"));
            }
            else
            {
                if ((number == 0)|(number == 60)|(number == 120)|(number == 119))
                {
                    br = new BufferedReader(new FileReader(DungeonQuest.directory +"\\GameBoard\\BoardLevel" + number + ".txt"));
                }
                else
                {
                    br = new BufferedReader(new FileReader(DungeonQuest.directory +"\\GameBoard\\Board" + number + ".txt"));
                }
            }
            width = Integer.parseInt(br.readLine());
            height = Integer.parseInt(br.readLine());
            size = br.readLine();
            xDimension = Integer.parseInt(br.readLine());
            yDimension = Integer.parseInt(br.readLine());
            numberOfUnavailableSpaces = Integer.parseInt(br.readLine());
            for (int i = 0;i < numberOfUnavailableSpaces;i++)
            {
                xPosition = "";
                yPosition = "";
                halfWayPoint = 0;
                tempLine = br.readLine();
                for (int j = 0;j < tempLine.length();j++)
                {

                    if (tempLine.charAt(j) != ',')
                    {
                        xPosition = xPosition + tempLine.charAt(j);
                    }
                    else
                    {
                        halfWayPoint = j + 1;
                        break;
                    }
                }
                for (int j= halfWayPoint;j < tempLine.length();j++)
                {
                    yPosition = yPosition + tempLine.charAt(j);
                }
                newPoint = new Point();
                newPoint.setLocation(Integer.parseInt(xPosition),Integer.parseInt(yPosition));
                unavailableSpaces.add(newPoint);
            }

            numberOfTakenPoints = Integer.parseInt(br.readLine());
            for (int i = 0;i < numberOfTakenPoints;i++) {
                xPosition = "";
                yPosition = "";
                halfWayPoint = 0;
                tempLine = br.readLine();
                for (int j = 0; j < tempLine.length(); j++) {

                    if (tempLine.charAt(j) != ',') {
                        xPosition = xPosition + tempLine.charAt(j);
                    } else {
                        halfWayPoint = j + 1;
                        break;
                    }
                }
                for (int j = halfWayPoint; j < tempLine.length(); j++) {
                    yPosition = yPosition + tempLine.charAt(j);
                }
                allTakenPoints.add(new TakenPoint(null,Integer.parseInt(xPosition),Integer.parseInt(yPosition)));
            }
            br.close();
        }
        catch (Exception exc)
        {

        }
    }
    public int CheckForEnemies(Player myPlayer)
    {
        int xPositionPlayer;
        int yPositionPlayer;
        int xPositionEnemy;
        int yPositionEnemy;
        for (int j = 0;j <3;j++)
        {
            xPositionPlayer = (int) myPlayer.attackZone[j].getX();
            yPositionPlayer = (int) myPlayer.attackZone[j].getY();
            for (int i =0; i < enemies.size(); i++)
            {
                xPositionEnemy = enemies.get(i).icon.getX();
                yPositionEnemy = enemies.get(i).icon.getY();
                if ((xPositionPlayer == xPositionEnemy) & (yPositionPlayer == yPositionEnemy))
                {
                    return i;
                }
            }
        }
        return -1;
    }
    public int CheckForOtherEntities(Player myPlayer)
    {
        int xPositionPlayer;
        int yPositionPlayer;
        int xPositionEnemy;
        int yPositionEnemy;
        for (int j = 0;j <3;j++)
        {
            xPositionPlayer = (int) myPlayer.attackZone[j].getX();
            yPositionPlayer = (int) myPlayer.attackZone[j].getY();
            for (int i =0; i < otherEntities.size(); i++)
            {
                xPositionEnemy = otherEntities.get(i).icon.getX();
                yPositionEnemy = otherEntities.get(i).icon.getY();
                if ((xPositionPlayer == xPositionEnemy) & (yPositionPlayer == yPositionEnemy))
                {
                    return i;
                }
            }
        }
        return -1;
    }
    public int CheckForProjectiles(Player myPlayer)
    {
        int xPositionPlayer;
        int yPositionPlayer;
        int xPositionEnemy;
        int yPositionEnemy;
        for (int j = 0;j <3;j++)
        {
            xPositionPlayer = (int) myPlayer.attackZone[j].getX();
            yPositionPlayer = (int) myPlayer.attackZone[j].getY();
            for (int i =0; i < allProjectiles.size(); i++)
            {
                xPositionEnemy = allProjectiles.get(i).icon.getX();
                yPositionEnemy = allProjectiles.get(i).icon.getY();
                if ((xPositionPlayer == xPositionEnemy) & (yPositionPlayer == yPositionEnemy))
                {
                    return i;
                }
            }
        }
        return -1;
    }
    public int CheckForAllies(Player myPlayer)
    {
        int xPositionPlayer;
        int yPositionPlayer;
        int xPositionEnemy;
        int yPositionEnemy;
        for (int j = 0;j <3;j++)
        {
            xPositionPlayer = (int) myPlayer.attackZone[j].getX();
            yPositionPlayer = (int) myPlayer.attackZone[j].getY();
            for (int i =0; i < allies.size(); i++)
            {
                xPositionEnemy = allies.get(i).icon.getX();
                yPositionEnemy = allies.get(i).icon.getY();
                if ((xPositionPlayer == xPositionEnemy) & (yPositionPlayer == yPositionEnemy))
                {
                    return i;
                }
            }
        }
        return -1;
    }
    public void RemoveEnemy(int enemyNumber)//go from here, move each enemy down 1 place and then put it into a new array
    {
        Enemy enemyToRemove = enemies.get(enemyNumber);
        if (enemyToRemove.type == 'e')
        {
            DropRandomPotion(enemyToRemove.icon.getLocation());
        }
        deadEnemies.add(enemyToRemove);
        enemies.remove(enemyToRemove);
    }
    public void DropRandomPotion(Point location)
    {
        Random r = new Random();
        int randomNumber = -1;
        Potion newPotion = null;
        if (difficultyLevel == 1)
        {
            randomNumber = r.nextInt(2);
        }
        else if (difficultyLevel == 2)
        {
            randomNumber = r.nextInt(5);
        }
        else if (difficultyLevel == 3)
        {
            randomNumber = r.nextInt(8);

        }
        else if (difficultyLevel == 4)
        {
            randomNumber = r.nextInt(10);
        }
        if (randomNumber == 0)
        {
            newPotion = new Potion(this,"Health");
        }
        else if (randomNumber == 2)
        {
            newPotion = new Potion(this,"Energy");
        }

        if (newPotion != null)
        {
            otherEntities.add((newPotion));
            newPotion.ChangeAppearance(0);
            newPotion.icon.setLocation(location);
        }
    }
    public void EnsureDeadEnemiesHidden()
    {
        for (int i=0;i< deadEnemies.size();i++)
        {
            deadEnemies.get(i).icon.setVisible(false);
        }
    }
    public void ChangeStateOfControls(boolean state,Entity eValue)
    {
        if (state ==true)
        {
            eValue.icon.grabFocus();
        }
        else
        {
            lbBackgroundImage.grabFocus();
        }
    }
    public void CheckAllProjectiles()
    {
        int i =0;
        while (i < allProjectiles.size())
        {
            if (allProjectiles.get(i).dead == true)
            {
                allProjectiles.remove(i);
            }
            else
            {
                i++;
            }
        }


    }
    public void MoveAllProjectiles(Player myPlayer)
    {
        for (int i =0;i < allProjectiles.size();i++)
        {
            allProjectiles.get(i).MoveEntity(this,myPlayer);
        }
    }
    public void HurtEnemy(int enemyIndex,int amount)
    {
        Enemy currentEnemy = enemies.get(enemyIndex);
        Arbiter tempArbiter;
            if (currentEnemy.health < amount)
            {
                if (currentEnemy.name.equals("Arbiter"))
                {
                    tempArbiter = (Arbiter) currentEnemy;
                    tempArbiter.EntityHurt(this, currentEnemy.health);
                }
                else
                {
                    currentEnemy.EntityHurt(currentEnemy.health);
                }

            } else
            {
                if (currentEnemy.name.equals("Arbiter"))
                {
                    tempArbiter = (Arbiter) currentEnemy;
                    tempArbiter.EntityHurt(this, amount);
                }
                else
                {
                    currentEnemy.EntityHurt(amount);
                }

            }
            if (currentEnemy.dead == true)
            {
                RemoveEnemy(enemyIndex);
            }
    }
    public void HurtEnemy(Enemy eValue,int amount)
    {
        for (int i=0;i<enemies.size();i++)
        {
            if (enemies.get(i) == eValue)
            {
                HurtEnemy(i,amount);
            }
        }

    }
    public void HurtAlly(int allyIndex,int amount)
    {
        Enemy currentAlly = allies.get(allyIndex);
        if (currentAlly.health < amount) {
            currentAlly.EntityHurt(currentAlly.health);
        } else {
            for (int i = 0; i < amount; i++) {
                currentAlly.EntityHurt(1);
            }
        }
        if (currentAlly.dead == true) {
            allies.remove(currentAlly);
        }
    }

    public void HurtItem(int itemIndex,int amount)
    {
        Item currentItem = otherEntities.get(itemIndex);
        if (currentItem.health < amount) {
            currentItem.EntityHurt(currentItem.health);
        } else {
            for (int i = 0; i < amount; i++) {
                currentItem.EntityHurt(1);
            }
        }
        if (currentItem.dead == true) {
            otherEntities.remove(currentItem);
        }
    }
    public void CheckIfWon(Player myPlayer)
    {
        Item chest = null;
        Item fountain = null;
        if (((myPlayer.icon.getX() == 725) | (myPlayer.icon.getX() == 825)) & (myPlayer.icon.getY() == 400) & levelNumber == 119)
        {
            if (exitTile.dead == true)
            {
                won = true;
            }
        }
        else if ((playerPosition.getX() == exitTile.icon.getX()) & (playerPosition.getY() == exitTile.icon.getY()))
            {
                if (exitTile.dead == true)
                {
                    won = true;
                }
                else
                {
                    if (type == 'e')
                    {
                        JOptionPane.showMessageDialog(null, "You must kill all enemies to proceed!");
                    }
                    else if (type == 'k')
                    {
                        JOptionPane.showMessageDialog(null, "You must collect all the keys and unlock the exit to proceed! To unlock the exit press [R] when standing on or next to the exit.");
                    }
                    else if (type == 'b')
                    {
                        JOptionPane.showMessageDialog(null, "You must Kill the boss to proceed and open the chest to proceed!");

                    }
                    else if (type == 'f')
                    {
                        JOptionPane.showMessageDialog(null, "You must approach the fountain!");
                    }
                }
            }
        else if (exitTile.dead == false)
        {
            if (type == 'e')
                {
                    if (enemies.size() <= 0)
                    {
                        exitTile.ChangeToAfterState();
                    }
                }
                else if ((type == 'b')|(type == 'f'))
                {
                    if ((enemies.size() <= 0) & (otherEntities.size() <= 0))
                    {
                        exitTile.ChangeToAfterState();
                    }
                }
                else if (type =='z')//This is level 120. Last level so last letter of alphabet
                {
                    if (myPlayer.kingsoul)
                    {
                        exitTile.icon.setVisible(true);
                        exitTile.ChangeToAfterState();
                    }
                }
                else if (type == 'y')//This is level 119 so not applicable. Second to last letter of alphabet for seond to last level
                {

                }
            }
            if ((won == true) & (levelNumber !=120)) {
                JOptionPane.showMessageDialog(null, "Level Complete! Saving & moving to next level");
            }
    }

    public void CheckOtherEntities(Player myPlayer)
    {
        int count = 0;
        for (int i =0;i< otherEntities.size();i++)
        {
            if (otherEntities.get(i).dead == false) {
                if (otherEntities.get(i).type == 'i') {
                    if ((otherEntities.get(i).itemType.equals("Key"))|(otherEntities.get(i).itemType.equals("ChestKey"))|(otherEntities.get(i).itemType.equals("Potion"))) {
                        if ((playerPosition.getX() == otherEntities.get(i).icon.getX()) & (playerPosition.getY() == otherEntities.get(i).icon.getY())) {
                            myPlayer.InteractAction(this,true);
                        }
                    }
                }
            }
        }
        while (count != otherEntities.size())
        {
            if (otherEntities.get(count).dead == false)
            {
                count++;
            }
            else
            {
                otherEntities.remove(count);
            }
        }

    }
    public void FindDifficultyLevel(int levelNumber)
    {
        if (levelNumber <= 10)
        {
            difficultyLevel = 1;
        }
        else if((levelNumber > 10) & (levelNumber < 50))
        {
            difficultyLevel = 2;
        }
        else if ((levelNumber >=50) & (levelNumber < 80))
        {
            difficultyLevel = 3;

        }
        else if (levelNumber >= 80)
        {
            difficultyLevel = 4;
        }
    }
    public void MakeEnemyAlly(int index)
    {
        Enemy currentEnemy =  enemies.get(index);
        currentEnemy.healthBar.ChangeColour("Purple");
        allies.add(currentEnemy);
        enemies.remove(currentEnemy);
    }
    public void MakeAllyEnemy(int index)
    {
        Enemy currentAlly = allies.get(index);
        currentAlly.healthBar.ChangeColour("Red");
        enemies.add(currentAlly);
        allies.remove((currentAlly));
    }
    public void SetUpLevel0()
    {
        LoadSpecificLevelBoard();
        exitTile = new Item(this,"Exit");
        exitTile.icon.setLocation((origin[0] + ((xDimension -1) * 100)),(origin[1] + (100 * (yDimension -1))));

        Item key = new Item(this,"Key");
        otherEntities.add(key);
        key.ChangeAppearance(0);
        key.SetLocation(525,100);

        Item chestKey = new Item(this,"ChestKey");
        otherEntities.add(chestKey);
        chestKey.ChangeAppearance(0);
        chestKey.SetLocation(25,500);

        Item chest = new Item(this,"LockedChest");
        otherEntities.add(chest);
        chest.ChangeAppearance(0);
        chest.SetLocation(325,800);

        enemies.add(new Enemy(this,difficultyLevel));
        enemies.get(0).icon.setLocation(725,200);
        enemies.get(0).UpdateStatBarLocations();
        type = 'k';
        LoadLevelDialog();

        SetUpFinalBoardParts();

    }
    public void LoadLevelDialog()
    {
        int numberOfDialogs;
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(DungeonQuest.directory + "\\GameDialog\\Level " + levelNumber + "\\Info.txt"));
            numberOfDialogs = Integer.parseInt(br.readLine());
            for (int i = 0;i<numberOfDialogs;i++)
            {
                allDialog.add(new GameDialog(("Level " + levelNumber),(i + 1) + ""));
            }
        }
        catch(Exception exc)
        {

        }
    }
    public void SetUpLevel60()
    {
        LoadSpecificLevelBoard();
        exitTile = new Item(this,"Exit");
        exitTile.icon.setLocation((origin[0] + ((xDimension -1) * 100)),(origin[1] + (100 * (yDimension -1))));

        Item fountain = new Item(this,"Fountain");
        otherEntities.add(fountain);
        fountain.ChangeAppearance(0);
        fountain.SetInteractSpaces(this);

        type = 'f';
        LoadLevelDialog();
        SetUpFinalBoardParts();
    }
    public void SetUpLevel119()
    {
        Item newItem;
        Potion newPotion;
        String potionType = "";
        LoadSpecificLevelBoard();

        exitTile = new Item(this,"Door");
        exitTile.icon.setLocation(625,0);
        exitTile.icon.setVisible(true);

        Item brazier1 = new Item(this,"Brazier");
        otherEntities.add(brazier1);
        brazier1.icon.setLocation(525,500);
        Item brazier2 = new Item(this,"Brazier");
        otherEntities.add(brazier2);
        brazier2.icon.setLocation(1025,500);

        for (int i = 0;i<5;i++)
        {
            newItem = new Item(this,"UnlockedChest");
            newItem.RandomlyPlace(this);
            newItem.ChangeAppearance(0);
            otherEntities.add(newItem);
        }
        for (int i = 0;i<13;i++)
        {
            if (i <7)
            {
                potionType = "Health";
            }
            else if (i > 6)
            {
                potionType = "Energy";
            }
            newPotion = new Potion(this,potionType);
            newPotion.RandomlyPlace(this);
            newPotion.ChangeAppearance(0);
            otherEntities.add(newPotion);

        }

        type = 'y';


        SetUpFinalBoardParts();


    }
    public void SetUpLevel120()
    {
        LoadSpecificLevelBoard();

        exitTile = new Item(this,"Exit");
        exitTile.icon.setLocation((origin[0] + ((xDimension-1) * 100)),(origin[1] + (100 * (yDimension-1))));
        exitTile.icon.setVisible(false);

        Arbiter theArbiter = new Arbiter(this,difficultyLevel);
        enemies.add(theArbiter);
        theArbiter.icon.setLocation(725,300);
        theArbiter.UpdateStatBarLocations();


        type = 'z';
        LoadLevelDialog();



        SetUpFinalBoardParts();


    }
    public void LoadSpecificLevelBoard()
    {
        Image backgroundImage;
        if (levelNumber !=60)
        {
            backgroundImage = Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\GameBoard\\BoardLevel" + levelNumber + ".png"));
        }
        else
        {
            backgroundImage = Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\GameBoard\\BoardLevel" + levelNumber + ".gif"));
        }
        lbBackgroundImage = new JLabel(new ImageIcon(backgroundImage));
        LoadInBoardInfo(levelNumber);
    }
    public void SetUpFinalBoardParts()
    {
        lbBackgroundImage.setBounds(0, 0, width, height); //size of the label must perfectly match that of the image, so we need to change the size when the image changes too
        lbBackgroundImage.setVisible(true);
        pane.setSize(width,height);
        pane.add(lbBackgroundImage,JLayeredPane.DEFAULT_LAYER);//defualt is the bottom, then palette, then modal, then popup, then Drag
        SetUpCurrentRelicIcon();
        SetUpCurrentSwordIcon();
        SetUpCurrentShieldIcon();
        RefreshGameBoard();
    }
    public void CheckStunnedEntities(Player myPlayer)
    {
        for (int i =0;i<enemies.size();i++)
        {
            if (enemies.get(i).stunned == true)
            {
                if (enemies.get(i).stunCount < 3)
                {
                    enemies.get(i).stunCount++;
                }
                else
                {
                    enemies.get(i).StunEntity(false);
                }
            }
        }
        for (int i =0;i<allies.size();i++)
        {
            if (allies.get(i).stunned == true)
            {
                if (allies.get(i).stunCount < 3)
                {
                    allies.get(i).stunCount++;
                }
                else
                {
                    allies.get(i).StunEntity(false);
                }
            }
        }
    }
    public void CheckStoryOptions(Player myPlayer)
    {
        if (levelNumber == 0)
        {
            DoLevel0Cycle(myPlayer);
        }
        else if (levelNumber == 60)
        {
            DoLevel60Cycle(myPlayer);

        }
        else if (levelNumber ==120)
        {
            DoLevel120Cycle(myPlayer);
        }
    }
    public void DoLevel0Cycle(Player myPlayer)
    {
         if ((playerPosition.getX() == origin[0] + (100 * 2)) & (playerPosition.getY() == (origin[1] + (100 * 3))))
        {
            allDialog.get(0).DisplayContent();

        }
        else if ((playerPosition.getX() == origin[0] + (100 * 4)) & (playerPosition.getY() == (origin[1] + (100 * 6))))
        {
            allDialog.get(1).DisplayContent();

        }
        else if ((playerPosition.getX() == origin[0] + (100 * 9)) & (playerPosition.getY() == (origin[1] + (100 * 5))))
        {
            allDialog.get(2).DisplayContent();
        }
        if (storyDone == false)
        {
            CinematicScreen CS = new CinematicScreen(this,myPlayer,"Intro");
            storyDone = true;
        }
    }
    public void DoLevel60Cycle(Player myPlayer)
    {
        for (int i = 0;i<otherEntities.size();i++)
        {
            if ((otherEntities.get(i).itemType.equals("Fountain")) & (myPlayer.myRelics.get(0).toolID == 5))
            {
                otherEntities.get(i).ChangeToAfterState();
            }
        }
    }
    public void DoLevel120Cycle(Player myPlayer)
    {
        boolean arbiterDead = true;
        Image backgroundImage = null;
        Arbiter tempArbiter;
        boolean valid = true;

        int index = -1;
        for (int i =0;i<enemies.size();i++)
        {
            if (enemies.get(i).name.equals("Arbiter"))
            {
                arbiterDead = false;
                index = i;
            }
        }

        if (DungeonQuest.entitiesFrozenCount == 5)
        {
            EnsureDeadEnemiesHidden();
        }

        if (allDialog.size() > 1)
        {
           while (allDialog.size() > 1)
           {
               allDialog.get(0).DisplayContent();
               allDialog.remove(0);
           }
           DungeonQuest.ChangeJOptionPaneFontType("Plain");
           for (int i = 0;i<enemies.size();i++)
           {
               if (enemies.get(i).name.equals("Arbiter"))
               {
                   tempArbiter = (Arbiter) enemies.get(i);
                   tempArbiter.SummonMoreEnemies(this);
               }
           }
        }

        if ((arbiterDead) & (storyDone == false))
        {
            storyDone = true;
            DungeonQuest.FreezeAllEntities(14);
            myPlayer.icon.setLocation((int) allTakenPoints.get(0).getX(),(int) allTakenPoints.get(0).getY());
            myPlayer.UpdateStatBarLocations();
            CheckLevel120Spaces();
            backgroundImage = Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\GameBoard\\BoardLevel120Ending.gif"));
            lbBackgroundImage.setIcon(new ImageIcon(backgroundImage));
        }

        for (int i =0;i<otherEntities.size();i++)
        {
            if (otherEntities.get(i).itemType.equals("Kingsoul"))
            {
                valid = false;
            }

        }

        if ((arbiterDead) & (DungeonQuest.entitiesFrozenCount == 0) & (myPlayer.kingsoul == false) & (valid == true))
        {
            KillAllEntities();
            backgroundImage = Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\GameBoard\\BoardLevel120.png"));
            lbBackgroundImage.setIcon(new ImageIcon(backgroundImage));
            Item kingsoul = new Item(this,"Kingsoul");
            otherEntities.add(kingsoul);
        }
    }
    public void CheckLevel120Spaces()
    {
        for (int j = 0;j< allTakenPoints.size();j++) {
            for (int i = 0; i < enemies.size(); i++) {
                if ((enemies.get(i).icon.getX() == allTakenPoints.get(j).getX()) & (enemies.get(i).icon.getY() == allTakenPoints.get(j).getY()))
                {
                    enemies.get(i).KillEntity();
                    enemies.get(i).icon.setVisible(false);
                }
            }
            for (int i = 0; i < allies.size(); i++) {
                if ((allies.get(i).icon.getX() == allTakenPoints.get(j).getX()) & (allies.get(i).icon.getY() == allTakenPoints.get(j).getY()))
                {
                    allies.get(i).KillEntity();
                    allies.get(i).icon.setVisible(false);
                }
            }
            for (int i = 0; i < otherEntities.size(); i++) {
                if ((otherEntities.get(i).icon.getX() == allTakenPoints.get(j).getX()) & (otherEntities.get(i).icon.getY() == allTakenPoints.get(j).getY()))
                {
                    otherEntities.get(i).ChangeToAfterState();

                }
            }
        }
    }
    public void KillAllEntities()
    {
        while (enemies.size() > 0)
        {
            HurtEnemy(0,50);
        }
        while (otherEntities.size() > 0)
        {
            otherEntities.get(0).ChangeToAfterState();
            otherEntities.remove(0);
        }
        while (allies.size() > 0)
        {
            HurtAlly(0,50);
        }
    }
    public void TrapActions(int cycleNumber)
    {
        Trap currentTrap;
        int modCycle = cycleNumber%2;
        int modi;
        for (int i = 0;i<otherEntities.size();i++)
        {
            if (otherEntities.get(i).itemType.equals("Trap"))
            {
                modi = i%2;
                if (modCycle == modi) {
                    currentTrap = (Trap) otherEntities.get(i);
                    currentTrap.DoAction(this);
                }
            }
        }
    }
    public void CheckPlayerLocation(Player myPlayer)
    {
        for (int i =0; i < otherEntities.size();i++)
        {
            if (otherEntities.get(i).nameLabel != null)
            {
                if (otherEntities.get(i).CheckIfAroundItem(myPlayer))
                {
                    otherEntities.get(i).nameLabel.setVisible(true);
                }
                else
                {
                    otherEntities.get(i).nameLabel.setVisible(false);
                }

            }
            if (otherEntities.get(i).interactLabel != null)
            {

                if (otherEntities.get(i).CheckIfAroundItem(myPlayer))
                {
                    otherEntities.get(i).interactLabel.setVisible(true);
                }
                else
                {
                    otherEntities.get(i).interactLabel.setVisible(false);
                }
            }
        }
    }




}



