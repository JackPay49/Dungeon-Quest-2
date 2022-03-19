import javax.swing.*;
import java.awt.*;

public class StatBar extends JProgressBar{

    int maxValue;
    int currentValue;
    String type;

    StatBar(GameBoard GB, Point location, int cValue, int mValue, String tValue)
    {
        int xPosition = (int) location.getX();
        int yPosition = (int) location.getY();
        type = tValue;
        maxValue = mValue;
        currentValue = cValue;
        if (type.equals("Health"))
        {
            yPosition = yPosition - 10;
            ChangeColour("Red");
        }
        else if (type.equals("Energy"))
        {
            yPosition = yPosition;
            ChangeColour("Blue");
        }
        else if (type.contains("Shield"))
        {
            yPosition = yPosition - 20;
            ChangeColour("Light Blue");
            if (type.equals("Dread Shield"))
            {
                ChangeColour("Light Green");
            }
        }
        this.setBounds(xPosition,yPosition,100,10);
        this.setOrientation(SwingConstants.HORIZONTAL);
        this.setVisible(true);
        this.setMinimum(0);
        this.setMaximum(maxValue);
        this.setStringPainted(false);
        this.setBackground(new Color(74,72,72));
        UpdateStatBar(cValue);
        GB.pane.add(this,JLayeredPane.DRAG_LAYER);
    }
    public void UpdateStatBar(int cValue)
    {
        currentValue =cValue;
        this.setValue(currentValue);
    }
    public void UpdateLocation(Point location)
    {
        int xPosition = (int) location.getX();
        int yPosition = (int) location.getY();
        if (type.equals("Health"))
        {
            yPosition =yPosition - 10;
        }
        else if (type.equals("Energy"))
        {
            yPosition = yPosition;
        }
        else if ((type.equals("Shield"))|(type.equals("Dread Shield")))
        {
            yPosition = yPosition - 20;
        }
        this.setLocation(xPosition,yPosition);
    }
    public void ChangeBarBounds(int cValue,int mValue)
    {
        this.setMaximum(mValue);
        this.setMinimum(0);
        UpdateStatBar(cValue);
    }
    public void ChangeColour(String colour)
    {
        this.setBackground(new Color(63, 63, 64));
        if (colour.equals("Purple"))
        {
            this.setForeground(new Color(109, 2, 142));
        }
        else if (colour.equals("Red"))
        {
            this.setForeground(new Color(140,6,33));
        }
        else if (colour.equals("Blue"))
        {
            this.setForeground(new Color(37,60,230));
        }
        else if (colour.equals("Light Blue"))
        {
            this.setForeground(new Color(0, 235, 255));

        }
        else if (colour.equals("Green"))
        {
            this.setForeground(new Color(49, 167, 6));
        }
        else if (colour.equals("Light Green"))
        {
            this.setForeground(new Color(34, 90, 0));
        }
        else if (colour.equals("Grey"))
        {
            this.setForeground(new Color(123, 123, 123, 255));

        }
    }




}
