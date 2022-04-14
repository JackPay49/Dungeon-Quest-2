import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public final class Player extends Entity {
    int score = 0;
    int level = 0;

    int fileNumber;
    String password;

    Sword currentSword = new Sword(1);
    Sword[] mySwords = {currentSword};
    int numberOfSwordsCollected =1;

    Shield currentShield = new Shield(1);
    Shield[] myShields = {currentShield};
    int numberOfShieldsCollected = 1;

    int maxEnergyLevel = 4;
    int energyLevel = maxEnergyLevel;
    StatBar energy;

    Relic currentRelic;
    ArrayList<Relic> myRelics = new ArrayList<Relic>();

    int invulnerableCount = 0;
    StatBar bubble;

    boolean blocking = false;

    boolean kingsoul = false;

    Player(GameBoard GB,String nValue, int fnValue){
        super(GB,EntityType.PLAYER);
        maxHealth =5;
        SetHealth(5);
        name = nValue;
        fileNumber = fnValue;
        SetEnergyLevel(0);
        LoadPlayer(name,fileNumber);
        energy = new StatBar(GB,icon.getLocation(),energyLevel,maxEnergyLevel,"Energy");
        if (level == 120)
        {
            icon.setLocation(725,800);
        }
        else if (level == 119)
        {
            icon.setLocation(725,800);
        }
        UpdateStatBarLocations();
        ChangeAppearance(0);
    }
    Player(String unValue,int fnValue)
    {
        super(unValue);
        LoadPlayer(unValue,fnValue);
    }
    Player(String unValue, int fnValue, String pValue)
    {
        super(unValue);
        maxHealth =5;
        health = 5;
        score = 0;
        level =0;
        currentRelic = null;
        fileNumber = fnValue;
        password = pValue;
        energyLevel = 0;
        currentSword = new Sword(1);
        currentShield = new Shield(1);
        numberOfSwordsCollected =1;
        numberOfShieldsCollected = 1;
    }

    public void SetCurrentRelic(int relicNumber, GameBoard GB)
    {
        if (currentRelic != null)
        {
            currentRelic.ChangeSelected(false);

        }
        for (int i=0;i< this.myRelics.size();i++)
        {
            if (myRelics.get(i).toolID == relicNumber)
            {
                currentRelic = myRelics.get(i);
                currentRelic.ChangeSelected(true);
                GB.SetCurrentRelicIcon(relicNumber,kingsoul);
            }
        }


    }
    public void SetCurrentSword(GameBoard GB, int sid)
    {
        currentSword = new Sword(sid);
        GB.SetCurrentSwordIcon(currentSword);
    }
    public void SetCurrentShield(GameBoard GB, int sid)
    {
        currentShield = new Shield(sid);
        GB.SetCurrentShieldIcon(currentShield);
    }
    public void SavePlayer()
    {
        try {
            FileWriter fw = new FileWriter(DungeonQuest.directory + "\\Players\\" + name + "-" + fileNumber + ".txt");
            fw.write(name);
            fw.write("\r\n");
            fw.write(password + "");
            fw.write("\r\n");
            fw.write(level + "");
            fw.write("\r\n");
            fw.write(score + "");
            fw.write("\r\n");
            if (kingsoul)
            {
                fw.write("k");
            }
            else
            {
                fw.write("0");
            }
            fw.write("\r\n");
            fw.write(maxHealth + "");
            fw.write("\r\n");
            fw.write(health + "");
            fw.write("\r\n");
            fw.write(myRelics.size() + "");
            for (int i = 0; i < myRelics.size(); i++) {
                fw.write("\r\n");
                fw.write(myRelics.get(i).toolID + "");
            }
            fw.write("\r\n");
            if (currentRelic != null)
            {
                fw.write(currentRelic.toolID + "");
                fw.write("\r\n");
            }
            else
            {
                fw.write(-1 + "");
                fw.write("\r\n");
            }
            fw.write(maxEnergyLevel + "");
            fw.write("\r\n");
            fw.write(energyLevel + "");
            fw.write("\r\n");
            //like with relics. we have number of swords, each sword damage and then the current sword damage used to get the currently equiped sword
            fw.write(numberOfSwordsCollected + "");
            for (int i =0;i<numberOfSwordsCollected;i++)
            {
                fw.write("\r\n");
                fw.write(mySwords[i].toolID + "");
            }
            fw.write("\r\n");
            fw.write(currentSword.toolID + "");
            fw.write("\r\n");

            fw.write(numberOfShieldsCollected + "");
            for (int i =0;i<numberOfShieldsCollected;i++)
            {
                fw.write("\r\n");
                fw.write(myShields[i].toolID + "");
            }
            fw.write("\r\n");
            fw.write(currentShield.toolID + "");
            fw.write("\r\n");
            fw.close();


        }
        catch (Exception exc)
        {
            exc.printStackTrace();

        }
    }
    public void LoadPlayer(String nValue, int fnValue)
    {
        int currentRelicNumber =0;
        int numRelics = 0;
        name = nValue;
        fileNumber = fnValue;
        String kingsoulState;
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(DungeonQuest.directory + "\\Players\\" + nValue + "-" + fileNumber + ".txt"));
            name = br.readLine();
            password = br.readLine();
            level = Integer.parseInt(br.readLine());
            score = Integer.parseInt(br.readLine());
            kingsoulState = br.readLine();
            if (kingsoulState.equals("k"))
            {
                kingsoul = true;
            }
            else if (kingsoulState.equals("0"))
            {
                kingsoul = false;
            }
            maxHealth = Integer.parseInt(br.readLine());
            health = Integer.parseInt(br.readLine());
            numRelics = Integer.parseInt(br.readLine());
            myRelics = new ArrayList<Relic>();
            for (int i = 0; i < numRelics; i++) {
                currentRelicNumber = Integer.parseInt(br.readLine());
                myRelics.add(new Relic(currentRelicNumber,kingsoul));
            }
            currentRelicNumber = Integer.parseInt(br.readLine());
            if (currentRelicNumber != -1)
            {
                currentRelic = new Relic(currentRelicNumber,kingsoul);
            }
            CheckRelicCosts();
            maxEnergyLevel = Integer.parseInt(br.readLine());
            energyLevel = Integer.parseInt(br.readLine());
            numberOfSwordsCollected = Integer.parseInt(br.readLine());
            mySwords = new Sword[numberOfSwordsCollected];
            for (int i =0;i<numberOfSwordsCollected;i++)
            {
                mySwords[i] = new Sword(Integer.parseInt(br.readLine()));
            }
            currentSword = new Sword(Integer.parseInt(br.readLine()));
            numberOfShieldsCollected = Integer.parseInt(br.readLine());
            myShields = new Shield[numberOfShieldsCollected];
            for (int i =0;i<numberOfShieldsCollected;i++)
            {
                myShields[i] = new Shield(Integer.parseInt(br.readLine()));
            }
            currentShield = new Shield(Integer.parseInt(br.readLine()));
            br.close();
        }
        catch (Exception exc)
        {

        }
    }
    public void CheckRelicCosts()
    {
        boolean relic6Collected = false;
        for (int i =0 ;i <myRelics.size();i++)
        {
            if (myRelics.get(i).toolID == 5)
            {
                relic6Collected = true;
            }
        }
        if (relic6Collected == true)
        {
            for (int i =0;i<myRelics.size();i++)
            {
                myRelics.get(i).cost = 3;
                myRelics.get(i).relic6Collected = true;
            }
        }
    }
    @Override
    public Boolean CheckPosition(GameBoard GB, int xPosition,int yPosition)//returns true if its a valid position to move into, false if not
    {
        boolean valid = true;
        int maxXPosition = GB.origin[0] + (GB.xDimension * 100);
        int maxYPosition = GB.origin[1] + (GB.yDimension * 100);
        int minXPosition = GB.origin[0];
        int minYPosition = GB.origin[1];
        for (int i=0;i < GB.unavailableSpaces.size();i++)
        {
            if ((xPosition == GB.unavailableSpaces.get(i).getX()) & (yPosition == GB.unavailableSpaces.get(i).getY()))
            {
                valid = false;
            }
        }
        if (valid ==true)
        {
            if ((xPosition >= maxXPosition) | (xPosition < minXPosition) | (yPosition >= maxYPosition) | (yPosition < minYPosition))
            {
                valid = false;
            }
            if (valid ==true)
            {
                for (int i =0;i < GB.enemies.size();i++)
                {
                    if (GB.enemies.get(i) != null)
                    {
                        if ((GB.enemies.get(i).icon.getX() == xPosition) & (GB.enemies.get(i).icon.getY() == yPosition) & (GB.enemies.get(i).dead ==false ))//check not on an alive enemy
                        {
                            valid = false;
                        }
                    }
                }
            }
            if (valid ==true)
            {
                for (int i =0;i < GB.allies.size();i++)
                {
                    if (GB.allies.get(i) != null)
                    {
                        if ((GB.allies.get(i).icon.getX() == xPosition) & (GB.allies.get(i).icon.getY() == yPosition) & (GB.allies.get(i).dead ==false ))//check not on an alive enemy
                        {
                            valid = false;
                        }
                    }
                }
            }
        }
        if (valid ==true)
        {
            for (int i =0;i<GB.allProjectiles.size();i++)
            {
                if ((xPosition == GB.allProjectiles.get(i).icon.getX()) & (yPosition == GB.allProjectiles.get(i).icon.getY()))
                {
                    valid = false;
                }
            }
        }
        if (valid == true)
        {
            for (int i =0;i<GB.otherEntities.size();i++)
            {
                if (((GB.otherEntities.get(i).dead == false) & ((GB.otherEntities.get(i).name.contains("Chest"))|(GB.otherEntities.get(i).name.equals("Brazier"))|(GB.otherEntities.get(i).name.equals("Kingsoul"))|(GB.otherEntities.get(i).itemType.equals("Trap"))))) {
                    if ((xPosition == GB.otherEntities.get(i).icon.getX()) & (yPosition == GB.otherEntities.get(i).icon.getY())) {
                        valid = false;
                    }
                }
            }
        }
        if (valid ==true)
        {
            for (int i =0;i<GB.numberOfInteractSpaces;i++)
            {
                if ((xPosition == GB.allInteractSpaces.get(i).getX()) & (yPosition == GB.allInteractSpaces.get(i).getY()))
                {
                    valid = false;
                }
            }
        }
        return valid;
    }
    @Override
    public void EntityHurt(int amount)
    {
        if (blocking)
        {
            BlockBroken(true);
        }
        else
        {
            if (invulnerableCount > 0)
            {
                invulnerableCount--;
                bubble.UpdateStatBar(invulnerableCount);
                if (invulnerableCount == 0) {
                    bubble.setVisible(false);
                    bubble = null;
                    ChangeAppearance(3);
                    DungeonQuest.PlaySound(DungeonQuest.directory + "\\Tool\\r\\1Hit.wav");
                }
                else
                {
                    MakeSound(3);
                }
            }
            else if (invulnerableCount <= 0)
            {
                for (int i =0; i <amount;i++)
                {
                    DecreaseHealth();
                    if (health ==0)
                    {
                        break;
                    }

                }
                UpdateHealthBar();
                if (health > 0)
                {
                    ChangeAppearance(3);
                }
                else
                {
                    ChangeAppearance(4);
                    dead = true;
                    JOptionPane.showMessageDialog(null,"Game Over!!!");
                }
            }
        }


    }
    @Override
    public void UpdateStatBarLocations()
    {
        super.UpdateStatBarLocations();
        energy.UpdateLocation(icon.getLocation());
        if (invulnerableCount > 0)
        {
            bubble.UpdateLocation(icon.getLocation());
        }
    }
    public void IncreaseEnergyLevel()
    {
        if (energyLevel < maxEnergyLevel)
        {
            energyLevel++;
            energy.UpdateStatBar(energyLevel);
        }
    }
    public void SetEnergyLevel(int elValue)
    {
        energyLevel = elValue;
        if (energy != null)
        {
            energy.UpdateStatBar(energyLevel);
        }
    }
    public void DecreaseEnergy(int amount)
    {
        energyLevel = energyLevel - amount;
        if (energy != null)
        {
            energy.UpdateStatBar(energyLevel);
        }
    }
    public void UseRelic(GameBoard GB)
    {
        if (energyLevel >= currentRelic.cost)
        {
            currentRelic.UseRelic(this,GB);
        }
        else
        {
            DungeonQuest.PlaySound(DungeonQuest.directory + "\\Tool\\r\\fail.wav");
        }
    }
    public Relic GetOneRelic(int relicNumber)
    {
        for (int i =0;i < myRelics.size(); i ++)
        {
            if (myRelics.get(i).toolID == relicNumber)
            {
                return myRelics.get(i);
            }
        }
        return null;
    }
    public Sword GetOneSword(int swordID)
    {
        for (int i =0;i < numberOfSwordsCollected; i ++)
        {
            if (mySwords[i].toolID == swordID)
            {
                return mySwords[i];
            }
        }
        return null;
    }
    public Shield GetOneShield(int shieldID)
    {
        for (int i =0;i < numberOfShieldsCollected; i ++)
        {
            if (myShields[i].toolID == shieldID)
            {
                return myShields[i];
            }
        }
        return null;
    }
    @Override
    public void IncreaseHealth() {
        super.IncreaseHealth();
        UpdateHealthBar();
    }
    @Override
    public void ChangeAppearance(Integer index)
    {
        ImageIcon tempIcon = new ImageIcon();
        String entity = "";
        String action = "";
        String entityFacing = facing;
        String imageFile;
        if (type ==EntityType.PLAYER)
        {
            entity = "\\Hero";
        }
        if (index == 0)
        {
            action = "\\Idle";
        }
        else if (index == 1)
        {
            action = "\\Attacking";

        }
        else if (index == 2)
        {
            action = "\\Moving";

        }
        else if (index == 3)
        {
            action = "\\Hurt";

        }
        else if (index == 4)
        {
            action = "\\Dying";

        }
        else if (index == 5)
        {
            action = "\\Special";
        }
        else if (index == 6)
        {
            action = "\\UsingRelic6";
        }
        else if (index == 7)
        {
            action = "\\Blocking"; //Need Blocking sound
        }
        else if (index == 8)
        {
            action = "\\BlockBroken";//Need block broken sound
        }
        if (invulnerableCount > 0)
        {
            entityFacing = entityFacing + "Invulnerable";
        }
        imageFile = DungeonQuest.directory + entity + action + entityFacing + ".gif";
        tempIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(imageFile));
        tempIcon.getImage().flush();
        icon.setIcon(tempIcon);
        MakeSound(index);
    }
    @Override
    public void MoveEntity(GameBoard GB, String direction)
    {
        BlockBroken(false);
        super.MoveEntity(GB, direction);
    }
    public void BlockAction(GameBoard GB)
    {
        blocking = true;
        healthBar.ChangeColour("Grey");
        ChangeAppearance(7);
    }
    public void AttackAction(GameBoard GB,String attackType)
    {
        int entityIndex;
        int attackIndex=0;
        int damage =0;
        int enemyHealth;
        EntityType entityType;
        Item tempItem;
        Enemy tempEnemy;
        BlockBroken(false);
        if (attackType.equals("Normal"))
        {
            attackIndex = 1;
            damage = this.currentSword.damage;
        }
        else if (attackType.equals("Special"))
        {
            attackIndex = 5;
            damage = this.currentSword.damage * 2;
        }
        else if (attackType.equals("Special2"))
        {
            attackIndex = 5;
            damage = this.currentSword.damage * 2;
            for (int i = 0;i<this.currentSword.damage;i++)
            {
                IncreaseHealth();
            }
        }
        ChangeAppearance(attackIndex);
        GB.RefreshGameBoard();
        entityIndex = GB.CheckForEnemies(this);
        if (entityIndex !=-1)
        {
            entityType = GB.enemies.get(entityIndex).type;
            enemyHealth = GB.enemies.get(entityIndex).health;
            if (attackType.equals("Normal"))
            {
                IncreaseEnergyLevel();
            }
            if ((GB.enemies.get(entityIndex).health - damage) <= 0)
            {
                if (entityType ==EntityType.ENEMY)
                {
                    IncreaseScore(25);
                }
                else if (entityType == EntityType.BOSS)
                {
                    IncreaseScore(100);
                }
            }
            if (damage >= enemyHealth)
            {
                GB.HurtEnemy(entityIndex,enemyHealth);
                currentSword.UseSword(this,true);
            }
            else
            {
                GB.HurtEnemy(entityIndex,damage);
                currentSword.UseSword(this,false);
            }

        }
        else
        {
            entityIndex = GB.CheckForOtherEntities(this);
            if (entityIndex !=-1)
            {
                tempItem = (Item) GB.otherEntities.get(entityIndex);
                entityType = tempItem.type;
                if ((entityType == EntityType.ITEM) & ((tempItem.itemType.equals("Chest"))|(tempItem.itemType.equals("Trap"))))
                {
                    tempItem.InteractWithItem(GB,this,true);
                }
            }
            else
            {
                entityIndex = GB.CheckForAllies(this);
                if (entityIndex !=-1)
                {
                    GB.MakeAllyEnemy(entityIndex);
                    AttackAction(GB,attackType);
                }
                else
                {
                    entityIndex = GB.CheckForProjectiles(this);
                    if (entityIndex != -1)
                    {
                        GB.allProjectiles.get(entityIndex).HurtProjectile();
                    }
                }
            }
        }
    }
    public void IncreaseScore(int value)
    {
        score = score + value;
    }
    public void CollectNewRelic(Relic newRelic)
    {
        myRelics.add(newRelic);
    }
    public void InteractAction(GameBoard GB,boolean countKeys)
    {
        Item itemInteractingWith = null;
        int index = -1;
        for (int i =0;i< attackZone.length;i++)
        {
            if ((GB.exitTile.icon.getX() == attackZone[i].getX()) & (GB.exitTile.icon.getY() == attackZone[i].getY()))
            {
                itemInteractingWith = GB.exitTile;
            }
            if ((GB.exitTile.icon.getX() == icon.getX()) & (GB.exitTile.icon.getY() == icon.getY()))
            {
                itemInteractingWith = GB.exitTile;
            }
        }
        if (itemInteractingWith == null)
        {
            for (int i = 0; i < GB.otherEntities.size(); i++)
            {
                for (int j = 0; j < attackZone.length; j++)
                {
                    if ((GB.otherEntities.get(i).icon.getX() == attackZone[j].getX()) & (GB.otherEntities.get(i).icon.getY() == attackZone[j].getY()))
                    {
                        index = i;
                    }
                }
                if ((GB.otherEntities.get(i).icon.getX() == icon.getX()) & (GB.otherEntities.get(i).icon.getY() == icon.getY()))
                {
                    index = i;
                }
            }
            if (index != -1)
            {
                itemInteractingWith = (Item) GB.otherEntities.get(index);
            }
        }
        if (itemInteractingWith == null)
        {
            for (int i = 0;i<GB.numberOfInteractSpaces;i++)
            {
                for (int j = 0; j < attackZone.length; j++)
                {
                    if ((GB.allInteractSpaces.get(i).getX() == attackZone[j].getX()) & (GB.allInteractSpaces.get(i).getY() == attackZone[j].getY()))
                    {
                        if (GB.exitTile == GB.allInteractSpaces.get(i).owner)
                        {
                            index = -2;
                        }
                        else {
                            for (int k = 0; k < GB.otherEntities.size(); k++) {
                                if (GB.allInteractSpaces.get(i).owner == GB.otherEntities.get(k)) {
                                    index = k;
                                }
                            }
                        }

                    }
                }
            }
            if (index == -2)
            {
                itemInteractingWith = (Item) GB.exitTile;
            }
            else if (index != -1)
            {
                itemInteractingWith = (Item) GB.otherEntities.get(index);
            }

        }
        if (itemInteractingWith != null)
        {
            itemInteractingWith.InteractWithItem(GB,this,countKeys);
        }
    }
    public void FullyRestoreHealth()
    {
        SetHealth(maxHealth);
    }
    public void UpgradeWeapon()
    {
        if (Arrays.asList(mySwords).contains(3))
        {
            return;
        }
        if (Arrays.asList(mySwords).contains(2))
        {
            numberOfSwordsCollected++;
            currentSword = new  Sword(3);
            mySwords = DungeonQuest.AddSwordToArray(mySwords,currentSword);
            return;
        }
        if (Arrays.asList(mySwords).contains(1))
        {
            numberOfSwordsCollected++;
            currentSword = new  Sword(2);
            mySwords = DungeonQuest.AddSwordToArray(mySwords,currentSword);
            return;
        }

    }

    public void SetKingsoul(boolean ksValue)
    {
        kingsoul = ksValue;
        if (ksValue)
        {
            for (int i =0;i<myRelics.size();i++)
            {
                if (myRelics.get(i).toolID == 5)
                {
                    myRelics.set(i,new Relic(5,true));
                }
            }
        }
    }
    public void BlockBroken(boolean successful)
    {
        blocking = false;
        healthBar.ChangeColour("Red");
        if (successful)
        {
            ChangeAppearance(8);
            currentShield.UseShield(this);
        }
    }





}
