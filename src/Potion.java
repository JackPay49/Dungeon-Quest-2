import javax.swing.*;
import java.awt.*;

public class Potion extends Item{
    String potionType;//Health or Energy

    Potion(GameBoard GB,String ptValue)
    {
        super(GB,"Potion");
        potionType = ptValue;
        name = itemType + ":" + potionType;
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
        imageFile = directory + entity + "\\" + itemType + "\\" + potionType + action + entityFacing + ".gif";
        tempIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(imageFile));
        tempIcon.getImage().flush();
        icon.setIcon(tempIcon);
        MakeSound(index);
    }
    @Override
    public void InteractWithItem(GameBoard GB, Player myPlayer, boolean valid) {
        if (dead != true)
        {
            if (potionType.equals("Health"))
            {
                myPlayer.IncreaseHealth();
            }
            else if (potionType.equals("Energy"))
            {
                myPlayer.IncreaseEnergyLevel();
            }
            ChangeToAfterState();
        }
    }


}
