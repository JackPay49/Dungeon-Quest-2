import javax.swing.*;
import java.awt.*;

public class Label extends JLabel
{
    Label(GameBoard GB,String tValue, Point location)
    {
        int xPosition = location.x - 100;
        int yPosition = location.y - 40;
        String text;
        if ((tValue.equals("Fountain"))|(tValue.equals("Door"))|(tValue.equals("Idol"))|(tValue.equals("Kingsoul")))
        {
            text = "⎯⎯⎯ Press R to Interact ⎯⎯⎯";
            yPosition = yPosition + 20;
        }
        else if ((tValue.equals("Chest"))|(tValue.equals("LockedChest"))|(tValue.equals("UnlockedChest")))
        {
            text = "⎯⎯⎯ Press R to Open ⎯⎯⎯";
            yPosition = yPosition + 20;
        }
        else if (tValue.equals("Trap"))
        {
            text = "⎯⎯⎯ Press R to Disable ⎯⎯⎯";
            yPosition = yPosition + 20;
        }
        else
        {
            text = tValue;
        }
        setFont(new Font("Century",Font.BOLD,16));
        setForeground(new Color(255,255,255));
        setBounds(xPosition,yPosition,300,20);
        setHorizontalAlignment(JLabel.CENTER);
        setFont(Font.getFont("Century"));
        setText(text);
        setVisible(false);
        GB.pane.add(this,JLayeredPane.DRAG_LAYER);
    }
}
