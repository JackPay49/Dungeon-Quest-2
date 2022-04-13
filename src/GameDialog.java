import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class GameDialog extends JOptionPane {
    String type;
    String ID;

    int numberOfLines = 0;
    String content[] = new String[0];

    String fullContent = "";

    String displayType = "Plain";


    GameDialog(String tValue, String idValue)
    {
        type = tValue;
        ID = idValue;
        LoadInContent();
        MakeFullContent();
    }
    public void LoadInContent()
    {
        String tempContent = "";
        String filename = DungeonQuest.directory + "\\GameDialog\\" + type + "\\" + ID + ".txt";
        boolean done = false;
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            displayType = br.readLine();
            while (done == false)
            {
                tempContent = br.readLine();
                if (tempContent != null)
                {
                    numberOfLines++;
                    content = DungeonQuest.AddStringToArray(content,tempContent);
                }
                else
                {
                    done = true;
                }
            }
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }
    }
    public void MakeFullContent()
    {
        for (int i =0;i<numberOfLines;i++)
        {
            fullContent = fullContent + content[i] + "\n";
        }
    }
    public void DisplayContent()
    {
        DungeonQuest.ChangeJOptionPaneFontType(displayType);
        this.showMessageDialog(null, fullContent, type, JOptionPane.PLAIN_MESSAGE);
    }


}
