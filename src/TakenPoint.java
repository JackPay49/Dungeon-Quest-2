import java.awt.*;

public class TakenPoint extends Point {

    Entity owner;

    TakenPoint(Entity oValue,int xPosition,int yPosition)
    {
        x = xPosition;
        y = yPosition;
        owner = oValue;
    }
}
