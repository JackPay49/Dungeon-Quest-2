import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Trap extends Item{

    String trapType = "";

    Trap(GameBoard GB, String ttValue)
    {
        super(GB,"Trap");
        trapType = ttValue;
        name = itemType + ":" + trapType;
        health = 1;
        RandomlyPlace(GB);
        ChangeAppearance(0);
    }
    @Override
    public void ChangeAppearance(Integer index) //0 before its been interacted with, 1 is after, 2 is doing an action
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
        else if (index == 2)
        {
            action = "\\Action";
        }
        imageFile = DungeonQuest.directory + entity + "\\" + itemType + "\\" + trapType + action + entityFacing + ".gif";
        tempIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(imageFile));
        tempIcon.getImage().flush();
        icon.setIcon(tempIcon);
        MakeSound(index);
    }
    public void DoAction(GameBoard GB)
    {
        if (dead != true) {
            ChangeAppearance(2);
            if (trapType.equals("FireWizard")) {
                Projectile fireBall = new Projectile(GB, this.facing, "Fireball", this, 4);
            }
            else if (trapType.equals("ArrowWizard")) {
                Projectile magicArrow = new Projectile(GB, this.facing, "MagicArrow", this, 4);
            }
        }
    }
    @Override
    public void RandomlyPlace(GameBoard GB)
    {
        Random r = new Random();
        int xPosition = 0;
        int yPostion;
        int randomNumber = 0;
        boolean validNewPosition;
        yPostion = (r.nextInt(GB.yDimension) * 100) + GB.origin[1];
        r = new Random();
        randomNumber = r.nextInt(2);
        if (randomNumber == 0)
        {
            xPosition = ((GB.yDimension - 1) * 100) + GB.origin[0];
            facing = "Left";
        }
        else if (randomNumber == 1)
        {
            xPosition = GB.origin[0];
            facing = "Right";
        }
        validNewPosition = CheckPosition(GB,xPosition,yPostion);
        if ((validNewPosition == true) & (yPostion != GB.exitTile.iconYDimension))
        {
            icon.setLocation(xPosition,yPostion);
            SetAttackZone();
        }
        else
        {
            RandomlyPlace(GB);
        }
        UpdateLabelLocations();
    }
    @Override
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
        if ((health > 0) == false)
        {
            ChangeToAfterState();
        }

    }
    @Override
    public void InteractWithItem(GameBoard GB, Player myPlayer, boolean valid)
    {
        ChangeToAfterState();
        health = 0;
    }
}
