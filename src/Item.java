import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Item extends Entity{
    String itemType;//Chest, Key, Exit, LockedChest [to be unlocked], UnlockedChest [to be unlocked], ChestKey [to unlock chest], HealthP, EnergyP, Fountain, Kingsoul
    Label nameLabel;
    Label interactLabel;

    Item(GameBoard GB, String itValue)
    {
        super(GB,EntityType.ITEM);
        facing = "Left";
        itemType = itValue;
        name = itValue;
        if (((itemType.contains("Key"))|(itemType.equals("Potion"))|(itemType.contains("Exit"))|(itemType.contains("Brazier"))) == false)//Things that don't have an interact label
        {
            interactLabel = new Label(GB, itValue, icon.getLocation());
        }
        if (itemType.equals("Fountain"))
        {
            SetLocation(125,200);
            nameLabel = new Label(GB,"Fountain of the King",icon.getLocation());
            SetIconSize(GB,3,2);
        }
        else if (itemType.equals("Kingsoul"))
        {
            SetLocation(725,500);
            facing = "Right";
        }
        else if (itemType.equals("Door"))
        {
            SetLocation(625,0);
            SetIconSize(GB,4,4);
            interactLabel.setLocation(interactLabel.getX(),(interactLabel.getY() + 100));
        }
        else if (itemType.equals("Idol"))
        {
            SetLocation(325,300);
            facing = "Right";
            nameLabel = new Label(GB,"Hero's Idol",icon.getLocation());
            SetIconSize(GB,1,3);
        }
        ChangeAppearance(0);
    }

    public void UpdateLabelLocations()
    {
        int xCentrePosition = icon.getX();
        if (iconXDimension > 2)
        {
            xCentrePosition = xCentrePosition + ((iconXDimension - 1) * 50);
        }
        if (nameLabel != null)
        {
            nameLabel.setLocation((xCentrePosition - 100),(icon.getY() - 40));
        }
        if (interactLabel != null)
        {
            interactLabel.setLocation((xCentrePosition - 100),(icon.getY() - 20));
        }
    }
    public void SetLocation(int x, int y)
    {
        icon.setLocation(x,y);
        UpdateLabelLocations();
    }
    @Override
    public void RandomlyPlace(GameBoard GB)
    {
        super.RandomlyPlace(GB);
        UpdateLabelLocations();
    }
    public void SetIconSize(GameBoard GB,int xValue, int yValue)
    {
        iconXDimension = xValue;
        iconYDimension = yValue;
        icon.setSize((xValue * 100), (yValue * 100));
        SetInteractSpaces(GB);
        UpdateLabelLocations();
    }
    @Override
    public void ChangeAppearance(Integer index) //0 before its been interacted with, 1 is after
    {
        ImageIcon tempIcon = new ImageIcon();
        String entity = "\\OtherEntities";
        String action = "";
        String entityFacing = facing;
        String imageFile;
        if (index == 0)
        {
            action = "\\Before";
        }
        else if (index == 1)
        {
            action = "\\After";

        }
        imageFile = DungeonQuest.directory + entity + "\\" + itemType + action + entityFacing + ".gif";
        tempIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(imageFile));
        tempIcon.getImage().flush();
        icon.setIcon(tempIcon);
        MakeSound(index);
    }
    @Override
    public void MakeSound(Integer index)
    {
        String entity = "\\OtherEntities";
        String action = "";
        String audioFile;
        int numberOfSounds = 0;
        Random r = new Random();
        int randomNumber;
        if (index == 1)
        {
            action = "\\Sound";
            if ((itemType.equals("Chest"))|(itemType.equals("LockedChest"))|(itemType.equals("UnlockedChest")))
            {
                numberOfSounds = 3;
            }
            else if ((itemType.equals("Key"))|(itemType.equals("ChestKey")))
            {
                numberOfSounds =2;
            }
            else if (itemType.equals("Exit"))
            {
                numberOfSounds =2;
            }
            else if (itemType.equals("Potion"))
            {
                numberOfSounds = 2;
            }
            else if (itemType.equals("Fountain"))
            {
                numberOfSounds = 1;
            }
            else if (itemType.equals("Kingsoul"))
            {
                numberOfSounds = 1;
            }
            else if (itemType.equals("Trap"))
            {
                numberOfSounds =3;
            }
        }
        if (numberOfSounds != 0)
        {
            randomNumber = r.nextInt(numberOfSounds) + 1;
            audioFile = DungeonQuest.directory + entity + "\\" + itemType + action + randomNumber + ".wav";
            DungeonQuest.PlaySound(audioFile);
        }
    }
    public void InteractWithItem(GameBoard GB,Player myPlayer,boolean countKeys)
    {
        boolean collectedAllKeys = true;
        Item currentKey;
        int number = -1;
        if (dead != true) {
            if (itemType.equals("Chest")) {
                RewardPlayer(GB,myPlayer,"Relic");
                ChangeToAfterState();
            }
            else if (itemType.equals("Key"))
            {
                ChangeToAfterState();
            }
            else if (itemType.equals("Exit"))
            {
                if (GB.type == 'f')
                {
                    for (int i =0;i<GB.otherEntities.size();i++)
                    {
                        currentKey = (Item) GB.otherEntities.get(i);
                        if (currentKey.itemType.equals("Fountain"))
                        {
                            if (currentKey.dead == false)
                            {
                                collectedAllKeys = false;
                            }
                        }
                    }
                }
                else if (GB.type == 'k')
                {
                    for (int i =0;i<GB.otherEntities.size();i++)
                    {
                        currentKey = (Item) GB.otherEntities.get(i);
                        if (currentKey.itemType.equals("Key"))
                        {
                            if (currentKey.dead == false)
                            {
                                collectedAllKeys = false;
                            }
                        }
                    }
                }
                else if ((GB.type == 'b')| (GB.type == 'e'))
                {
                    if (GB.enemies.size() <= 0)
                    {
                        collectedAllKeys = true;
                    }
                }
                if ((collectedAllKeys == true)|(countKeys == false))
                {
                    ChangeToAfterState();
                }

            }
            else if (itemType.equals("LockedChest"))
            {
                for (int i =0;i<GB.otherEntities.size();i++)
                {
                    currentKey = (Item) GB.otherEntities.get(i);
                    if (currentKey.itemType.equals("ChestKey"))
                    {
                        if (currentKey.dead == false)
                        {
                            collectedAllKeys = false;
                        }
                    }
                }
                if ((collectedAllKeys == true)|(countKeys == false))
                {
                    ChangeToAfterState();
                    RewardPlayer(GB,myPlayer,"NotRelic");
                }
            }
            else if (itemType.equals("UnlockedChest"))
            {
                ChangeToAfterState();
                RewardPlayer(GB,myPlayer,"NotRelic");
            }
            else if (itemType.equals("ChestKey"))
            {
                ChangeToAfterState();
            }
            else if (itemType.equals("Fountain"))
            {
                CinematicScreen CS = new CinematicScreen(GB,myPlayer,"Level60");

            }
            else if (itemType.equals("Kingsoul"))
            {
                myPlayer.SetKingsoul(true);
                GB.allDialog.get(0).DisplayContent();
                ChangeToAfterState();
            }
            else if (itemType.equals("Door"))
            {
                if (myPlayer.myRelics.size() == 6)
                {
                    ChangeToAfterState();
                }
                else
                {

                }
            }
            else if (itemType.equals("Idol"))
            {
                CinematicScreen CS = new CinematicScreen(GB,myPlayer,"Idol");
            }
        }
    }
    public void ChangeToAfterState()
    {
        int index = -1;
        ChangeAppearance(1);
        dead = true;
        if (nameLabel != null)
        {
            nameLabel.setVisible(false);
            nameLabel = null;
        }
        if (interactLabel != null)
        {
            interactLabel.setVisible(false);
            interactLabel = null;
        }
    }
    public void RewardPlayer(GameBoard GB,Player myPlayer,String rewardType)//like Relic or NotRelic
    {
        Random r = new Random();
        int randomNumber;
        Relic newRelic;
        ArrayList<Integer> possibleRelicNumbers = new ArrayList<Integer>();
        for (int i = 0;i < 5; i++)
        {
            possibleRelicNumbers.add(i);
        }
        if (rewardType.equals("Relic"))
        {
            if (myPlayer.myRelics.size() != 5)
            {
                for (int i=0;i< myPlayer.myRelics.size();i++)
                {
                    for (int j=0; j < possibleRelicNumbers.size();j++)
                    {
                        if (myPlayer.myRelics.get(i).toolID == possibleRelicNumbers.get(j))
                        {
                            possibleRelicNumbers.remove((Integer) j);
                        }
                    }
                }
                randomNumber = r.nextInt(possibleRelicNumbers.size());
                newRelic = new Relic(possibleRelicNumbers.get(randomNumber),false);
                CinematicScreen CS = new CinematicScreen(GB,myPlayer,("Relic" + newRelic.toolID));
                myPlayer.CollectNewRelic(newRelic);
            }
            else
            {
                myPlayer.IncreaseScore(500);
                DungeonQuest.rewardDialog[0].DisplayContent();
            }
        }
        else if (rewardType.equals("NotRelic"))
        {
            randomNumber = r.nextInt(4);
            if (randomNumber == 0)//Heart
            {
                if (myPlayer.maxHealth != 7)
                {
                    DungeonQuest.rewardDialog[1].DisplayContent();
                    myPlayer.maxHealth++;
                    myPlayer.healthBar.UpdateStatBar(myPlayer.health);

                }
                else
                {
                    myPlayer.IncreaseScore(300);
                    DungeonQuest.rewardDialog[0].DisplayContent();

                }
            }
            else if (randomNumber == 1)//energy
            {
                if (myPlayer.maxEnergyLevel != 6)
                {
                    DungeonQuest.rewardDialog[2].DisplayContent();
                    myPlayer.maxEnergyLevel++;
                    myPlayer.energy.UpdateStatBar(myPlayer.energyLevel);
                }
                else
                {
                    myPlayer.IncreaseScore(300);
                    DungeonQuest.rewardDialog[0].DisplayContent();

                }
            }
            else if (randomNumber == 2)//Sword
            {
                if (myPlayer.numberOfSwordsCollected != 3)
                {
                    DungeonQuest.rewardDialog[3].DisplayContent();
                    myPlayer.UpgradeWeapon();
                }
                else
                {
                    myPlayer.IncreaseScore(300);
                    DungeonQuest.rewardDialog[0].DisplayContent();
                }
            }
            else if (randomNumber == 3)//Points
            {
                myPlayer.IncreaseScore(300);
                DungeonQuest.rewardDialog[0].DisplayContent();

            }

        }
        myPlayer.FullyRestoreHealth();
    }
    public boolean CheckIfAroundItem(Entity eValue)
    {
        for (int i = 0;i<(iconXDimension + 2);i++)
        {
            for (int j = 0;j<(iconYDimension + 2);j++)
            {
                if ((eValue.icon.getX() == ((icon.getX() - 100) + (100 * i))) & (eValue.icon.getY() == ((icon.getY() - 100) + (100 * j))))
                {
                    return true;
                }
            }
        }
        return false;

    }


}
