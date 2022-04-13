import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class Sword extends Tool{
    int damage;

    Sword(int sid)
    {
        super(sid,'w');
    }

    @Override
    public void LoadTool()
    {
        try
        {
            BufferedReader br;
            br = new BufferedReader(new FileReader(DungeonQuest.directory + "\\Tool\\w\\" + toolID + ".txt"));
            name = br.readLine();
            damage = Integer.parseInt(br.readLine());
            description = br.readLine();
            action = br.readLine();
            br.close();
        }
        catch (Exception exc)
        {

        }
        symbol = new ImageIcon(Toolkit.getDefaultToolkit().getImage(DungeonQuest.directory + "\\Tool\\w\\" + toolID + ".png"));
        selectedSymbol = new ImageIcon(Toolkit.getDefaultToolkit().getImage(DungeonQuest.directory + "\\Tool\\w\\" + toolID + "Selected.png"));
    }
    public void UseSword(Player myPlayer,boolean killedEnemy)
    {
        if (toolID == 4)
        {
            if (killedEnemy)
            {
                myPlayer.IncreaseHealth();
            }
        }

    }

}
