import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Projectile extends Entity {
    int damage;
    Entity owner;
    String projectileType;

    Projectile(GameBoard GB, String fValue, String ptValue, Entity oValue, int dValue)
    {
        super(GB,'o');
        int xPosition = oValue.icon.getX();
        int yPosition = oValue.icon.getY();
        facing = fValue;
        owner = oValue;
        name = ptValue;
        projectileType = ptValue;
        damage = dValue;
        GB.AddProjectile(this);
        ChangeAppearance(2);
        if (facing.equals("Right"))
        {
            xPosition = xPosition + 100;
        }
        else if (facing.equals("Left"))
        {
            xPosition = xPosition - 100;
        }
        else if (facing.equals("Up"))
        {
            yPosition = yPosition - 100;
        }
        else if (facing.equals("Down"))
        {
            yPosition = yPosition + 100;
        }
        icon.setLocation(xPosition,yPosition);
    }
    @Override
    public void ChangeAppearance(Integer index) //0 idle, 1 attacking, 2 moving, 3 hurt, 4 dead, 5 stunned
    {
        ImageIcon tempIcon = new ImageIcon();
        String entity = "";
        String action = "";
        String entityFacing = facing;
        String imageFile;
        entity = "\\Projectile";
        if (index == 0)
        {
            action = "\\Moving";
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
            action = "\\Stunned";
        }
        imageFile = DungeonQuest.directory + entity + "\\" + projectileType + action + entityFacing + ".gif";
        tempIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(imageFile));
        tempIcon.getImage().flush();
        icon.setIcon(tempIcon);
        MakeSound(index);
    }
    @Override
    public void SetAttackZone()
    {
        int xPosition = icon.getX();
        int yPosition = icon.getY();
        attackZone = new Point[1];
        attackZone[0] = new Point();
        if (facing.equals("Right"))
        {
            xPosition = xPosition + 100;
        }
        else if (facing.equals("Left"))
        {
            xPosition = xPosition - 100;

        }
        else if (facing.equals("Up"))
        {
            yPosition = yPosition - 100;
        }
        else if (facing.equals("Down"))
        {
            yPosition = yPosition + 100;
        }
        attackZone[0].setLocation(xPosition,yPosition);
    }
    @Override
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
    public void MoveEntity(GameBoard GB, Player myPlayer)
    {
        Point newPosition;
        int xPosition = this.icon.getX();
        int yPosition = this.icon.getY();
        boolean validMove = false;
        if (dead == false)
        {
            newPosition = new Point();
            if (facing.equals("Right"))
            {
                xPosition = xPosition + 100;
            }
            else if (facing.equals("Left"))
            {
                xPosition = xPosition - 100;
            }
            else if (facing.equals("Up"))
            {
                yPosition = yPosition - 100;
            }
            else if (facing.equals("Down"))
            {
                yPosition = yPosition + 100;
            }
            newPosition.setLocation(xPosition,yPosition);
            validMove = CheckPosition(GB, xPosition,yPosition);
            if (validMove)
            {
                CheckPositionForEntities(GB,myPlayer,newPosition);
                ChangeAppearance(0);
            }
            else
            {
                ChangeAppearance(1);
                dead = true;
            }
        }
    }
    public void CheckPositionForEntities(GameBoard GB, Player myPlayer,Point newPosition)
    {
        if ((owner.type =='p')|(owner.type == 'i'))
        {
            for (int i=0;i<GB.numberOfEnemies;i++)
            {
                if ((GB.enemies[i].icon.getY() == newPosition.getY()) & (GB.enemies[i].icon.getX() == newPosition.getX()))
                {
                    AttackEntity(GB,GB.enemies[i]);
                }
                else
                {
                    this.icon.setLocation(newPosition);
                }
            }
            if (owner.type == 'i')
            {
                if ((myPlayer.icon.getX() == newPosition.getX()) & (myPlayer.icon.getY() == newPosition.getY()))
                {
                    AttackEntity(GB,myPlayer);
                }
                else
                {
                    this.icon.setLocation(newPosition);
                }
            }
        }
        else
        {
            if ((myPlayer.icon.getX() == newPosition.getX()) & (myPlayer.icon.getY() == newPosition.getY()))
            {
                AttackEntity(GB,myPlayer);
            }
            else
            {
                this.icon.setLocation(newPosition);
            }
        }
    }
    public void AttackEntity(GameBoard GB, Entity eValue)
    {
        if (eValue.type == 'p')
        {
            eValue.EntityHurt(damage);
        }
        else
        {
            GB.HurtEnemy((Enemy) eValue,damage);
        }
        ChangeAppearance(1);
        dead = true;
    }
    public void HurtProjectile()
    {
        ChangeAppearance(1);
        dead = true;
    }
    @Override
    public void MakeSound(Integer index)//0 idle, 1 attacking, 2 moving, 3 hurt, 4 dead
    {
        String entity = "";
        String action = "";
        String audioFile;
        int numberOfSounds = 0;
        Random r = new Random();
        int randomNumber;
        if (type=='o')
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
            if (projectileType.contains("Fireball"))
            {
                numberOfSounds = 2;
            }
            else if (projectileType.contains("Arrow"))
            {
                numberOfSounds = 2;
            }
        }
        else if (index == 2)
        {
            action = "\\Moving";
            if (projectileType.contains("Fireball"))
            {
                numberOfSounds = 1;
            }
            else if (projectileType.contains("Arrow"))
            {
                numberOfSounds = 1;
            }

        }
        if (numberOfSounds != 0)
        {
            randomNumber = r.nextInt(numberOfSounds) + 1;
            audioFile = DungeonQuest.directory + entity + "\\" + projectileType + action + randomNumber + ".wav";
            DungeonQuest.PlaySound(audioFile);
        }
    }




}
