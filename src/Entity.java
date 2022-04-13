import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.xml.stream.Location;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class Entity
{
    String name;
    String entityID;
    char type;
    String facing = "Right";
    int health;
    Boolean dead = false;
    JLabel icon = new JLabel();
    int maxHealth;

    int iconXDimension = 1;
    int iconYDimension = 1;

    Point attackZone[] = new Point[3];

    StatBar healthBar;



    Entity(GameBoard GB, char tValue){//p player, e normal enemy, o projectile, x exit tile, i item
        name = "entity";
        type = tValue;
        CreateIcon(GB);
        health =1;
        facing = "Right";
        SetAttackZone();
        if ((type !='o') & (type !='i'))
        {
            healthBar = new StatBar(GB,icon.getLocation(),health,health, "Health");
        }
    }
    Entity(String nValue, char tValue, String fValue, int hValue,GameBoard GB)
    {
        name = nValue;
        type = tValue;
        CreateIcon(GB);
        facing = fValue;
        health = hValue;
        ChangeAppearance(0);
        SetAttackZone();
        if ((type !='o') & (type !='i'))
        {
            healthBar = new StatBar(GB,icon.getLocation(),health,health, "Health");
        }
    }
    Entity(String nValue)
    {
        name = nValue;
    }
    public void SetAttackZone()
    {
        int xPosition = icon.getX();
        int yPosition = icon.getY();
        for (int i =0;i<3;i++)
        {
            attackZone[i] = new Point();
        }
        if (facing.equals("Right"))
        {
            xPosition = xPosition + 100;
        }
        else if (facing.equals("Left"))
        {
            xPosition = xPosition - 100;

        }
        attackZone[0].setLocation(xPosition,yPosition);
        xPosition = icon.getX();
        yPosition = yPosition + 100;
        attackZone[1].setLocation(xPosition,yPosition);
        yPosition = yPosition - 200;
        attackZone[2].setLocation(xPosition,yPosition);
    }



    public void SwapFacing()
    {
        if (facing.equals("Right"))
        {
            facing = "Left";
            ChangeAppearance(0);
        }
        else if (facing.equals("Left"))
        {
            facing = "Right";
            ChangeAppearance(0);
        }
    }
    public void DecreaseHealth()
    {
        health--;
    }
    public void IncreaseHealth()
    {
        if (health < maxHealth)
        {
            health++;
        }
    }
    public void ChangeAppearance(Integer index) //0 idle, 1 attacking, 2 moving, 3 hurt, 4 dead
    {
        ImageIcon tempIcon = new ImageIcon();
        String entity = "";
        String action = "";
        String entityFacing = facing;
        String imageFile;
        if (type =='p')
        {
            entity = "\\Hero";
        }
        else if (type =='e')
        {
            entity = "\\Enemy\\e";
        }
        else if (type =='o')
        {
            entity = "\\Projectile";
        }
        else if (type =='x')
        {
            entity = "\\ExitTile";
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
        imageFile = DungeonQuest.directory + entity + action + entityFacing + ".gif";
        tempIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(imageFile));
        tempIcon.getImage().flush();
        icon.setIcon(tempIcon);
        MakeSound(index);
    }
    public void MakeSound(Integer index)//0 idle, 1 attacking, 2 moving, 3 hurt, 4 dead
    {
        String entity = "";
        String action = "";
        String audioFile;
        int numberOfSounds = 0;
        Random r = new Random();
        int randomNumber;
        if (type =='p')
        {
            entity = "\\Hero";
        }
        else if (type =='e')
        {
            entity = "\\Enemy\\e";
        }
        else if (type =='b')
        {
            entity = "\\Enemy\\b";
        }
        else if (type=='o')
        {
            entity = "\\Projectile";
        }
        if (index == 0)
        {
            action = "\\Idle";
        }
        else if (index == 1)
        {
            action = "\\Attacking";
            if (type=='p')
            {
                numberOfSounds = 3;
            }
            else if (type=='o')
            {
                numberOfSounds =2;
            }
            else if (type =='b')
            {
                numberOfSounds = 3;
            }

        }
        else if (index == 2)
        {
            action = "\\Moving";
            if (type=='o')
            {
                numberOfSounds =1;
            }

        }
        else if (index == 3)
        {
            action = "\\Hurt";
            if (type=='p')
            {
                numberOfSounds = 3;
            }
        }
        else if (index == 4)
        {
            action = "\\Dying";
            if (type=='p')
            {
                numberOfSounds = 1;
            }
            else if (type =='e')
            {
                numberOfSounds =2;
            }
            else if (type == 'b')
            {
                numberOfSounds = 1;
            }
        }
        else if (index == 5)
        {
            action = "\\Special";
            if (type =='p')
            {
                numberOfSounds =1;
            }
        }
        if (numberOfSounds != 0)
        {
            randomNumber = r.nextInt(numberOfSounds) + 1;
            audioFile = DungeonQuest.directory + entity + action + randomNumber + ".wav";
            DungeonQuest.PlaySound(audioFile);
        }
    }
    public void MoveEntity(GameBoard GB,String direction)
    {
        direction = direction.toLowerCase();
        int xPosition = 0;
        int yPosition = 0;
        boolean validNewPosition = false;
        validNewPosition = CheckMove(GB,direction);
        if (direction.equals("right"))
        {
            SetFacing("Right");

        }
        else if (direction.equals("left"))
        {
            SetFacing("Left");
        }
        if (validNewPosition == true)
        {
            if (direction.equals("up"))
            {
                xPosition =icon.getX();
                yPosition = icon.getY() - 100;
            }
            else if (direction.equals("down"))
            {
                xPosition =icon.getX();
                yPosition = icon.getY() + 100;
            }
            else if (direction.equals("right"))
            {
                xPosition =icon.getX() + 100;
                yPosition = icon.getY();
            }
            else if (direction.equals("left"))
            {
                xPosition =icon.getX() - 100;
                yPosition = icon.getY();

            }
            icon.setLocation(xPosition,yPosition);
            ChangeAppearance(2);
            SetAttackZone();
            UpdateStatBarLocations();
        }
        else
        {
            ChangeAppearance(0);
        }
    }
    public void UpdateStatBarLocations()
    {
        healthBar.UpdateLocation(icon.getLocation());
    }


    public void CreateIcon(GameBoard GB)
    {
        Image playerIcon = Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\GameBoard\\BasicBoard.png"));
        JLabel tempLabel = new JLabel(new ImageIcon(playerIcon));
        icon = tempLabel;
        icon.setBounds(GB.origin[0],GB.origin[1], (100 * iconXDimension), (100 * iconYDimension));
        icon.setVisible(true);
        if (type =='p')
        {
            GB.pane.add(icon,JLayeredPane.POPUP_LAYER);
        }
        else if ((type =='e') | (type == 'b')) {
            GB.pane.add(icon, JLayeredPane.PALETTE_LAYER);
        }
        else if (type =='o')
        {
            GB.pane.add(icon,JLayeredPane.DRAG_LAYER);

        }
        else if (type =='x')
        {
            GB.pane.add(icon,JLayeredPane.MODAL_LAYER);
        }
        else if (type =='i')
        {
            GB.pane.add(icon,JLayeredPane.PALETTE_LAYER);
        }
        else
        {
            GB.pane.add(icon);
        }
    }
    public void RandomlyPlace(GameBoard GB)
    {
        Random r = new Random();
        int xPosition;
        int yPostion;
        boolean validNewPosition;
        xPosition = (r.nextInt(GB.xDimension) * 100) + GB.origin[0];
        yPostion = (r.nextInt(GB.yDimension) * 100) + GB.origin[1];
        if ((type == 'i') & (name.equals("Exit")))
        {
            xPosition = (((GB.xDimension -1) * 100) + GB.origin[0]);
        }
        validNewPosition = CheckPosition(GB,xPosition,yPostion);
        if (validNewPosition == true)
        {
            icon.setLocation(xPosition,yPostion);
            SetAttackZone();
        }
        else
        {
            RandomlyPlace(GB);
        }
    }
    public Boolean CheckPosition(GameBoard GB, int xPosition, int yPosition)//returns true if its a valid position to move into, false if not
    {
        boolean valid = true;
        int maxXPosition = GB.origin[0] + ((GB.xDimension-1) * 100);
        int maxYPosition = GB.origin[1] + ((GB.yDimension -1 )* 100);
        int minXPosition = GB.origin[0];
        int minYPosition = GB.origin[1];
        for (int i=0;i < GB.numberOfUnavailableSpaces;i++)
        {
            if ((xPosition == GB.unavailableSpaces[i].getX()) & (yPosition == GB.unavailableSpaces[i].getY()))
            {
                valid = false;
            }
        }
        if (valid == true)
        {
            if (type != 'o')
            {
                if ((xPosition == GB.playerPosition.getX()) & (yPosition == GB.playerPosition.getY())) {
                    valid = false;
                }
            }
        }
        if (valid ==true)
        {
            if ((xPosition > maxXPosition) | (xPosition < minXPosition) | (yPosition > maxYPosition) | (yPosition < minYPosition))
            {
                valid = false;
            }
        }
        if (valid ==true)
        {
            for (int i =0;i<GB.numberOfProjectilesCurrently;i++)
            {
                if ((xPosition == GB.allProjectiles[i].icon.getX()) & (yPosition == GB.allProjectiles[i].icon.getY()))
                {
                    valid = false;
                }
            }
        }
        if (valid == true)
        {
            for (int i =0;i<GB.numberOfOtherEntities;i++)
            {
                if (GB.otherEntities[i].dead == false) {
                    if ((xPosition == GB.otherEntities[i].icon.getX()) & (yPosition == GB.otherEntities[i].icon.getY())) {
                        valid = false;
                    }
                }
            }
        }
        if (valid ==true)
        {
            for (int i =0;i<GB.numberOfTakenPoints;i++)
            {
                if ((xPosition == GB.allTakenPoints[i].getX()) & (yPosition == GB.allTakenPoints[i].getY()))
                {
                    valid = false;
                }
            }
        }
        if (valid ==true)
        {
                if ((xPosition == GB.exitTile.icon.getX()) & (yPosition == GB.exitTile.icon.getY()))
                {
                    valid = false;
                }
        }

        return valid;
    }
    public Boolean CheckMove(GameBoard GB, String direction)//returns true if its a valid position to move into, false if not
    {
        direction = direction.toLowerCase(Locale.ROOT);
        int xPosition =0;
        int yPosition = 0;
        if (direction.equals("up"))
        {
            xPosition =icon.getX();
            yPosition = icon.getY() - 100;
        }
        else if (direction.equals("down"))
        {
            xPosition =icon.getX();
            yPosition = icon.getY() + 100;
        }
        else if (direction.equals("right"))
        {
            xPosition =icon.getX() + 100;
            yPosition = icon.getY();
        }
        else if (direction.equals("left"))
        {
            xPosition =icon.getX() - 100;
            yPosition = icon.getY();
        }
        return CheckPosition(GB,xPosition,yPosition);
    }
    public boolean CheckIfAroundEntity(Entity eValue)
    {
        int xPosition = icon.getX();
        int yPosition = icon.getY();
        boolean withinRange = false;

        xPosition = xPosition + 100;
        yPosition = yPosition - 200;
        for (int i =0;i < 3;i++)
        {
            yPosition = yPosition + 100;
            if ((eValue.icon.getX() == xPosition) & (eValue.icon.getY() == yPosition))
            {
                withinRange = true;
            }
        }
        if (withinRange == false) {
            xPosition = icon.getX() - 100;
            yPosition = icon.getY() - 200;

            for (int i = 0; i < 3; i++) {
                yPosition = yPosition + 100;
                if ((eValue.icon.getX() == xPosition) & (eValue.icon.getY() == yPosition)) {
                    withinRange = true;
                }
            }
            if (withinRange == false)
            {
                xPosition = icon.getX();
                yPosition = icon.getY() + 100;
                if ((eValue.icon.getX() == xPosition) & (eValue.icon.getY() == yPosition)) {
                    withinRange = true;
                }
                yPosition = icon.getY() - 100;
                if ((eValue.icon.getX() == xPosition) & (eValue.icon.getY() == yPosition)) {
                    withinRange = true;
                }
            }
        }
        return withinRange;
    }
    public void UpdateHealthBar()
    {
        healthBar.UpdateStatBar(health);
    }

    public void EntityHurt(int amount)
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
            KillEntity();
        }
    }
    public void KillEntity()
    {
        ChangeAppearance(4);
        dead = true;
        healthBar.setVisible(false);
    }
    public boolean CheckIfEntityWithinAttackZone(Entity eValue)
    {
        int xPosition = eValue.icon.getX();
        int yPosition = eValue.icon.getY();
        for (int i =0;i < 3;i++)
        {
            if ((attackZone[i].getX() == xPosition) & (attackZone[i].getY() ==yPosition))
            {
                return true;
            }
        }
        return false;
    }
    public void SetFacing(String fValue)
    {
        facing = fValue;
        ChangeAppearance(0);
    }
    public void SetHealth(int hValue)
    {
        health =hValue;
        healthBar.ChangeBarBounds(hValue,maxHealth);
    }
    public void SetInteractSpaces(GameBoard GB)
    {
        int xPosition;
        int yPosition;
        int count = 0;
        while(count < GB.numberOfInteractSpaces)
        {
            if (GB.allInteractSpaces[count].owner == this)
            {
                GB.allInteractSpaces = DungeonQuest.RemoveTakenPointFromArray(GB.allInteractSpaces,GB.allInteractSpaces[count]);
                GB.numberOfInteractSpaces--;
            }
            else
            {
                count++;
            }
        }
        for (int i = 0; i < iconYDimension; i++)
        {
            for (int j = 0;j<iconXDimension;j++)
            {
                xPosition = this.icon.getX() + (j * 100);
                yPosition = this.icon.getY() + (i * 100);
                GB.allInteractSpaces = DungeonQuest.AddTakenPointToArray(GB.allInteractSpaces,(new TakenPoint(this,xPosition,yPosition)));
                GB.numberOfInteractSpaces++;
            }
        }
    }
    public boolean CheckIfOnSameRow(Entity eValue)
    {
        if (eValue.icon.getY() == icon.getY())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean CheckIfOnSameColumn(Entity eValue)
    {
        if (eValue.icon.getX() == icon.getX())
        {
            return true;
        }
        else
        {
            return false;
        }
    }












}
