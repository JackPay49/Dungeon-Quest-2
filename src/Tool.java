import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class Tool {
    String name;
    String description;
    String action;
    int toolID;
    Icon symbol;
    Icon selectedSymbol;

    char toolType;//w for sword, h for shield, r for relic

    Tool(int tid, char tt)
    {
        toolID = tid;
        toolType = tt;
        LoadTool();
    }

    public void LoadTool()
    {
        try
        {
            BufferedReader br;
            br = new BufferedReader(new FileReader(DungeonQuest.directory + "\\Tool\\" + toolType + "\\" + toolID + ".txt"));
            name = br.readLine();
            description = br.readLine();
            action = br.readLine();
            br.close();
        }
        catch (Exception exc)
        {

        }
        symbol = new ImageIcon(Toolkit.getDefaultToolkit().getImage(DungeonQuest.directory + "\\Tool\\" + toolType + "\\" + toolID + ".png"));
        selectedSymbol = new ImageIcon(Toolkit.getDefaultToolkit().getImage(DungeonQuest.directory + "\\Tool\\" + toolType + "\\" + toolID + "Selected.png"));
    }
}
