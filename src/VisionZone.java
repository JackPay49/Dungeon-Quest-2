import java.awt.*;

public final class VisionZone {
    int numberOfSpaces;
    Point zone[];

    VisionZone()
    {
        numberOfSpaces =0;
        zone = new Point[0];
    }
    public void AddPoint(Point pValue)
    {
        Point tempzone[] = new Point[numberOfSpaces + 1];
        for (int i=0;i<numberOfSpaces;i++)
        {
            tempzone[i] = zone[i];
        }
        tempzone[numberOfSpaces] =pValue;
        numberOfSpaces++;
        zone = tempzone;
    }
    public void SetVisionZone(Enemy eValue, GameBoard GB)
    {
        int xEntity = eValue.icon.getX();
        int yEntity = eValue.icon.getY();

        int xCurrent = 0;
        int yCurrent = 0;

        Point newPoint = new Point();
        boolean available = false;

        int facingCoefficient =0;

        if (eValue.facing.equals("Right"))
        {
            facingCoefficient =1;
        }
        else if (eValue.facing.equals("Left"))
        {
            facingCoefficient = -1;
        }
        for (int i=0;i<6;i++)
        {
        xCurrent = xEntity + (100 * i * facingCoefficient);
        for (int j=0;j<(6-i);j++)
        {
            newPoint = new Point();
            yCurrent = yEntity - (100 * j);
            if ((xCurrent != eValue.icon.getX()) | ((yCurrent != eValue.icon.getY())))
            {
                newPoint.setLocation(xCurrent,yCurrent);
                available = eValue.CheckPositionWithoutOrigin(GB, xCurrent, yCurrent);
                if (available ==true)
                {
                    AddPoint(newPoint);
                }
                else if (available ==false)
                {
                    break;
                }
            }
        }
     }
        for (int i=0;i<6;i++)
        {
            xCurrent = xEntity + (100 * i * facingCoefficient);
            for (int j=1;j<(6-i);j++)
            {
                newPoint = new Point();
                yCurrent = yEntity + (100 * j);
                if ((xCurrent != eValue.icon.getX()) |((yCurrent != eValue.icon.getY())))
                {
                    newPoint.setLocation(xCurrent, yCurrent);
                    available = eValue.CheckPosition(GB, xCurrent, yCurrent);
                    if (available == true) {
                        AddPoint(newPoint);
                    } else if (available == false) {
                        break;
                    }
                }
            }
        }
    }


}
