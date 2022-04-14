import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class Enemy extends Entity{
    VisionZone visionZone = new VisionZone();
    String enemyType;//Skeleton,Assassin,Warrior
    int damage;

    boolean stunned = false;
    int stunCount = -1;

    int animationCount = 0;

    Enemy(GameBoard GB, int difficultyLevel){
        super(GB,EntityType.ENEMY);
        DecideEnemyType(difficultyLevel);
        ChangeAppearance(0);
        RandomlyPlace(GB);
    }
    Enemy(GameBoard GB, EntityType tValue, int difficultyLevel)
    {
        super(GB,tValue);
        DecideEnemyType(difficultyLevel);
        ChangeAppearance(0);
        RandomlyPlace(GB);
    }
    public void StunEntity(boolean sValue)
    {
        stunned = sValue;
        if (sValue == true)
        {
            ChangeAppearance(5);
            stunCount = 0;
            healthBar.ChangeColour("Green");
        }
        else
        {
            ChangeAppearance(0);
            stunCount = -1;
            healthBar.ChangeColour("Red");
        }
    }
    @Override
    public void ChangeAppearance(Integer index) //0 idle, 1 attacking, 2 moving, 3 hurt, 4 dead, 5 stunned, 6 Appearing
    {
        ImageIcon tempIcon = new ImageIcon();
        String entity = "";
        String action = "";
        String entityFacing = facing;
        String imageFile;
        if (animationCount <= 0) {
            if (type == EntityType.ENEMY) {
                entity = "\\Enemy\\ENEMY";
            } else if (type == EntityType.BOSS) {
                entity = "\\Enemy\\BOSS";
            }else if (type == EntityType.RANGEDENEMY) {
                entity = "\\Enemy\\RANGEDENEMY";
            }

            if (index == 0) {
                action = "\\Idle";
            } else if (index == 1) {
                action = "\\Attacking";

            } else if (index == 2) {
                action = "\\Moving";

            } else if (index == 3) {
                action = "\\Hurt";

            } else if (index == 4) {
                action = "\\Dying";
            } else if (index == 5) {
                action = "\\Stunned";
            } else if (index == 6) {
                if (type == EntityType.BOSS) {
                    action = "\\SummoningEnemies";
                    animationCount = 1;
                } else {
                    action = "\\Appearing";
                    animationCount = 1;
                }
            }
            imageFile = DungeonQuest.directory + entity + "\\" + enemyType + action + entityFacing + ".gif";
            tempIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(imageFile));
            tempIcon.getImage().flush();
            icon.setIcon(tempIcon);
        }
        MakeSound(index);
    }
    @Override
    public Boolean CheckPosition(GameBoard GB, int xPosition,int yPosition)//returns true if its a valid position to move into, false if not
    {
        boolean valid = true;
        int maxXPosition = GB.origin[0] + (GB.xDimension * 100);
        int maxYPosition = GB.origin[1] + (GB.yDimension * 100);
        int minXPosition = GB.origin[0];
        int minYPosition = GB.origin[1];
        for (int i=0;i < GB.unavailableSpaces.size();i++)//unavailable spaces
        {
            if ((xPosition == GB.unavailableSpaces.get(i).getX()) & (yPosition == GB.unavailableSpaces.get(i).getY()))
            {
                valid = false;
            }
        }
        if (valid == true)
        {
            if ((xPosition == GB.playerPosition.getX()) & (yPosition == GB.playerPosition.getY()))
            {
                valid = false;
            }
        }
        if (valid ==true)
        {
            if ((xPosition >= maxXPosition) | (xPosition < minXPosition) | (yPosition >= maxYPosition) | (yPosition < minYPosition))//borders
            {
                valid = false;
            }
            if (valid ==true)
            {
                if (((xPosition == minXPosition) & (yPosition == minYPosition)))//origin
                {
                    valid = false;
                }
                if (valid ==true)
                {
                    for (int i =0;i < GB.enemies.size();i++)
                    {
                        if (GB.enemies.get(i) != null)
                        {
                            if ((GB.enemies.get(i).icon.getX() == xPosition) & (GB.enemies.get(i).icon.getY() == yPosition))//other enemies
                            {
                                valid = false;
                            }
                        }
                    }
                    if (valid ==true)
                    {
                        if ((xPosition == GB.playerPosition.getX()) & (yPosition == GB.playerPosition.getY()))
                        {
                            valid = false;
                        }

                    }
                }
                if (valid ==true)
                {
                    for (int i =0;i < GB.allies.size();i++)
                    {
                        if (GB.allies.get(i) != null)
                        {
                            if ((GB.allies.get(i).icon.getX() == xPosition) & (GB.allies.get(i).icon.getY() == yPosition) & (GB.allies.get(i).dead ==false ))//check not on an alive enemy
                            {
                                valid = false;
                            }
                        }
                    }
                }
            }

        }
        if (valid ==true)
        {
            for (int i =0;i<GB.allProjectiles.size();i++)
            {
                if ((xPosition == GB.allProjectiles.get(i).icon.getX()) & (yPosition == GB.allProjectiles.get(i).icon.getY()))
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
        if (valid == true)
        {
            for (int i =0;i<GB.otherEntities.size();i++)
            {
                if (GB.otherEntities.get(i).dead == false) {
                    if ((xPosition == GB.otherEntities.get(i).icon.getX()) & (yPosition == GB.otherEntities.get(i).icon.getY())) {
                        valid = false;
                    }
                }
            }
        }
        if (valid ==true)
        {
            for (int i =0;i<GB.numberOfInteractSpaces;i++)
            {
                if ((xPosition == GB.allInteractSpaces.get(i).getX()) & (yPosition == GB.allInteractSpaces.get(i).getY()))
                {
                    valid = false;
                }
            }
        }
        if (valid ==true)
        {
            for (int i =0;i<GB.otherEntities.size();i++)
            {
                if (GB.otherEntities.get(i).itemType.equals("Trap")) {
                    if ((xPosition == GB.otherEntities.get(i).icon.getX()) & (yPosition == GB.otherEntities.get(i).icon.getY())) {
                        valid = false;
                    }
                }
            }
        }

        return valid;
    }
    public Boolean CheckPositionWithoutOrigin(GameBoard GB, int xPosition,int yPosition)//returns true if its a valid position to move into, false if not
    {
        boolean valid = true;
        int maxXPosition = GB.origin[0] + (GB.xDimension * 100);
        int maxYPosition = GB.origin[1] + (GB.yDimension * 100);
        int minXPosition = GB.origin[0];
        int minYPosition = GB.origin[1];
        for (int i=0;i < GB.unavailableSpaces.size();i++)//unavailable spaces
        {
            if ((xPosition == GB.unavailableSpaces.get(i).getX()) & (yPosition == GB.unavailableSpaces.get(i).getY()))
            {
                valid = false;
            }
        }
        if (valid ==true)
        {
            if ((xPosition >= maxXPosition) | (xPosition < minXPosition) | (yPosition >= maxYPosition) | (yPosition < minYPosition))//borders
            {
                valid = false;
            }
            if (valid ==true)
            {
                if (valid ==true)
                {
                    for (int i =0;i < GB.enemies.size();i++)
                    {
                        if (GB.enemies.get(i) != null)
                        {
                            if ((GB.enemies.get(i).icon.getX() == xPosition) & (GB.enemies.get(i).icon.getY() == yPosition))//other enemies
                            {
                                valid = false;
                            }
                        }
                    }
                }
            }

        }
        return valid;
    }
    public boolean CheckIfFacingWall(GameBoard GB, String direction)
    {
        int xPosition =0;
        int yPosition=0;
        Boolean facingWall= false;
        int borderCheck =0;
        Point tempPoint = new Point();
        if (direction.equals("Up"))
        {
            yPosition = icon.getY() - 100;
            xPosition = icon.getX();
            borderCheck = GB.origin[1];
        }
        else if (direction.equals("Down"))
        {
            yPosition = icon.getY() + 100;
            xPosition = icon.getX();
            borderCheck = ((GB.yDimension -1 ) * 100) + GB.origin[1];
        }
        else if (direction.equals("Right"))
        {
            xPosition = icon.getX() + 100;
            yPosition = icon.getY();
            borderCheck = ((GB.xDimension-1) * 100) + GB.origin[0];
        }
        else if (direction.equals("Left"))
        {
            xPosition = icon.getX() - 100;
            yPosition = icon.getY();
            borderCheck = GB.origin[0];

        }
        tempPoint.setLocation(xPosition,yPosition);
        for (int i = 0; i< GB.unavailableSpaces.size();i++)//check if they're facing an unavailable space
        {
            if ((GB.unavailableSpaces.get(i).getX() == tempPoint.getX()) & (GB.unavailableSpaces.get(i).getY() ==tempPoint.getY()))
            {
                facingWall = true;
            }
        }
        if ((direction).equals("Left") | (direction.equals("Right")))
        {
            if (icon.getX() == borderCheck)
            {
                facingWall = true;
            }
        }
        else if ((direction).equals("Up") | (direction.equals("Down")))
        {
            if (icon.getY() == borderCheck)
            {
                facingWall = true;
            }
        }
        return facingWall;
    }
    public void CheckFacing(GameBoard GB)
    {
        Boolean facingRightWall = false;
        Boolean facingLeftWall = false;
        if (facing.equals("Right"))
        {
            facingRightWall = CheckIfFacingWall(GB,"Right");
        }
        else if (facing.equals("Left"))
        {
            facingLeftWall = CheckIfFacingWall(GB, "Left");
        }
        if ((facingRightWall ==true) & facingLeftWall ==true)
        {

        }
        else if (((facingRightWall ==true) & (facingLeftWall ==false)) | ((facingRightWall ==false) & (facingLeftWall ==true)))
        {
            SwapFacing();
        }
    }
    @Override
    public void RandomlyPlace(GameBoard GB)
    {
        super.RandomlyPlace(GB);
        CheckFacing(GB);
        SetAttackZone();
        SetVisionZone(GB);
        UpdateStatBarLocations();
    }
    public void DoEnemyCycle(Entity eValue, GameBoard GB)//will either attack an entity or move towards them
    {
        Random r = new Random();
        int randomNumber = -1;
        int randomBound = 4;
        Boolean withinAttackZone = CheckIfEntityWithinAttackZone(eValue);
        Boolean canSeeEntity = CheckIfCanSeeEntity(eValue);
        boolean onSameRow = CheckIfOnSameRow(eValue);
        boolean onSameColumn = CheckIfOnSameColumn(eValue);
        if (animationCount <= 0) {
            if (enemyType.equals("Alchemist")) {
                randomBound = 6;
            }
            randomNumber = r.nextInt(randomBound);
            if (stunned == false) {
                FaceEntity(eValue);
                if (canSeeEntity == true) {
                    if (withinAttackZone == true) {
                        AttackEntity(eValue);
                        GB.ChangeStateOfControls(false, eValue);
                        GB.skipRound = true;
                    } else {
                        if (((onSameRow == true) | (onSameColumn == true)) & ((type == EntityType.RANGEDENEMY) | (enemyType.equals("Mage")))) {
                            FireProjectile(GB, eValue);
                        } else {
                            if (randomNumber < 4) {
                                MoveTowardsEntity(eValue, GB);
                            } else if (randomNumber >= 4) {
                                SummonMoreEnemies(GB);
                            }
                        }
                    }
                } else {
                    if (GB.allies.size() == 0) {
                        DoRandomMovement(GB);
                    } else {
                        eValue = GB.allies.get(0);
                        canSeeEntity = CheckIfCanSeeEntity(eValue);
                        withinAttackZone = CheckIfEntityWithinAttackZone(eValue);
                        FaceEntity(eValue);
                        if (canSeeEntity == true) {
                            if (withinAttackZone == true) {
                                AttackEntity(eValue);
                                GB.HurtAlly(0, damage);
                                GB.skipRound = true;

                            } else {
                                MoveTowardsEntity(eValue, GB);
                            }
                        } else {
                            if (randomNumber == 0) {
                                ChangeAppearance(0);
                            } else if ((randomNumber >= 1) & (randomNumber < 4)) {
                                DoRandomMovement(GB);
                            } else if (randomNumber >= 5) {
                                SummonMoreEnemies(GB);
                            }
                        }
                    }
                }
            }
        }
        else
        {
            animationCount--;
        }
    }
    public void DoAllyCycle(GameBoard GB)
    {
        Boolean withinAttackZone;
        ArrayList<Enemy> enemiesInVision = new ArrayList<Enemy>();
        if (GB.enemies.size() > 0)
        {
            for (int i=0;i<GB.enemies.size();i++)
            {
                FaceEntity(GB.enemies.get(i));
                if (CheckIfCanSeeEntity(GB.enemies.get(i)))
                {
                    enemiesInVision.add(GB.enemies.get(i));
                }
            }
            if (!enemiesInVision.isEmpty())
            {
                withinAttackZone = CheckIfEntityWithinAttackZone(enemiesInVision.get(0));
                if (withinAttackZone ==true)
                {
                    ChangeAppearance(1);
                    GB.HurtEnemy(enemiesInVision.get(0),damage);
                    GB.skipRound = true;
                }
                else
                {
                    MoveTowardsEntity(enemiesInVision.get(0),GB);
                }

            }
            else
            {
                DoRandomMovement(GB);
            }
        }
        else
        {
            ChangeAppearance(0);
        }

    }
    public void SummonMoreEnemies(GameBoard GB)
    {
        int xPosition = icon.getX();
        int yPosition = icon.getY();
        int enemyNumberLimit = 0;
        if (GB.size.equals("Small"))
        {
            enemyNumberLimit = 3;
        }
        else if (GB.size.equals("Medium"))
        {
            enemyNumberLimit = 4;
        }
        else if (GB.size.equals("Large"))
        {
            enemyNumberLimit = 6;
        }
        if (GB.enemies.size() < enemyNumberLimit) {
            Enemy newEnemy = new Enemy(GB, GB.difficultyLevel);
            newEnemy.ChangeAppearance(6);
            if (facing.equals("Right")) {
                xPosition = xPosition + 100;
            } else if (facing.equals("Left")) {
                xPosition = xPosition - 100;
            }
            if (CheckPosition(GB, xPosition, yPosition)) {
                newEnemy.icon.setLocation(xPosition, yPosition);
                newEnemy.UpdateStatBarLocations();
            }
            ChangeAppearance(6);
            GB.enemies.add(newEnemy);
        }
    }
    public void AttackEntity(Entity eValue)
    {
        ChangeAppearance(1);
        eValue.EntityHurt(damage);
    }
    public void MoveTowardsEntity(Entity eValue, GameBoard GB)
    {
        Random r = new Random();
        int xPositionEntity = eValue.icon.getX();
        int yPositionEntity = eValue.icon.getY();

        int xPositionThis = this.icon.getX();
        int yPositionThis = this.icon.getY();
        boolean validMove;
        String direction ="";

        int currentPositionThis = 0;
        int currentPositionEntity = 0;
        String axis = "x";
        if (xPositionThis == xPositionEntity)
        {
            axis = "Y";
            currentPositionEntity = yPositionEntity;
            currentPositionThis = yPositionThis;
        }
        else
        {
            axis = "X";
            currentPositionEntity = xPositionEntity;
            currentPositionThis = xPositionThis;
        }
        if (currentPositionThis > currentPositionEntity)
        {
            if (axis.equals("Y"))
            {
                direction = "Up";
            }
            else if (axis.equals("X"))
            {
                direction = "Left";
            }

        }
        else if (currentPositionThis < currentPositionEntity)
        {
            if (axis.equals("Y"))
            {
                direction = "Down";
            }
            else if (axis.equals("X"))
            {
                direction = "Right";
            }

        }
        validMove = CheckMove(GB,direction);
        if (validMove)
        {
            MoveEntity(GB,direction);
        }
        else
        {
            ChangeAppearance(0);
        }

    }
    public boolean CheckIfCanSeeEntity(Entity eValue)
    {
        for (int i=0; i< visionZone.numberOfSpaces;i++)
        {
            if ((eValue.icon.getX() == visionZone.zone[i].getX()) & (eValue.icon.getY() == visionZone.zone[i].getY()))
            {
                return true;
            }
        }
        return false;
    }
    public void FaceEntity(Entity eValue)
    {
        if ((eValue.icon.getX() < icon.getX()) & (facing.equals("Right")))
        {
            SetFacing("Left");
        }
        else if ((eValue.icon.getX() > icon.getX()) & (facing.equals("Left")))
        {
            SetFacing("Right");
        }
    }
    public void DoRandomMovement(GameBoard GB)
    {
        Random r = new Random();
        int randomNumber;
        boolean validMove = false;
        String direction = "";
        String validDirections[] = new String[0];
        String currentDirection = "";

        for (int i =0; i < 4;i++)
        {
            if (i == 0)
            {
                currentDirection = "Up";
            }
            else if (i ==1)
            {
                currentDirection = "Right";
            }
            else if (i ==2)
            {
                currentDirection = "Left";
            }
            else if (i ==3)
            {
                currentDirection = "Down";
            }
            validMove = CheckMove(GB,currentDirection);
            if (validMove)
            {
                validDirections = DungeonQuest.AddStringToArray(validDirections,currentDirection);
            }
        }
        if (validDirections.length > 0)
        {
            randomNumber = r.nextInt(validDirections.length);
            direction = validDirections[randomNumber];
            MoveEntity(GB,direction);
        }
    }
    @Override
    public void MoveEntity(GameBoard GB, String direction)
    {
        direction = direction.toLowerCase();
        int xPosition = 0;
        int yPosition = 0;
        boolean validNewPosition = false;
        validNewPosition = CheckMove(GB,direction);
        if (validNewPosition == true)
        {
            if (direction.equals("up"))
            {
                xPosition =icon.getX();
                yPosition = icon.getY() - 100;
            }
            else if (direction.equals("down"))
            {
                xPosition =icon.getX();
                yPosition = icon.getY() + 100;
            }
            else if (direction.equals("right"))
            {
                xPosition =icon.getX() + 100;
                yPosition = icon.getY();
                SetFacing("Right");
            }
            else if (direction.equals("left"))
            {
                xPosition =icon.getX() - 100;
                yPosition = icon.getY();
                SetFacing("Left");

            }
            icon.setLocation(xPosition,yPosition);
            ChangeAppearance(2);
            SetAttackZone();
            SetVisionZone(GB);
            UpdateStatBarLocations();
        }
        else
        {
            ChangeAppearance(0);
        }
    }
    public void SetVisionZone(GameBoard GB)
    {
        visionZone = new VisionZone();
        visionZone.SetVisionZone(this,GB);
    }
    public void DecideEnemyType(int difficultyLevel)
    {
        Random r = new Random();
        int randomNumber;
        int randomBound = 0;
        randomBound = difficultyLevel;
        if (randomBound == 4)
        {
            randomBound = 5;
        }
        randomNumber = r.nextInt(randomBound);
        if (randomNumber == 0)
        {
            enemyType = "Skeleton";
        }
        else if (randomNumber == 1)
        {
            enemyType = "Archer";
            type = EntityType.RANGEDENEMY;
        }
        else if (randomNumber ==2)
        {
            enemyType = "Warrior";
        }
        else if (randomNumber == 3)
        {
            enemyType = "Warlock";
            type = EntityType.RANGEDENEMY;
        }
        else if (randomNumber == 4)
        {
            enemyType = "Assassin";
        }
        LoadEnemyTypeInfo(difficultyLevel);
    }
    public void LoadEnemyTypeInfo(int difficultyLevel)
    {
        int hValue = 0;
        int dValue = 0;
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(DungeonQuest.directory + "\\Enemy\\" + type + "\\" + enemyType  + "\\Info" + difficultyLevel + ".txt"));
            hValue = Integer.parseInt(br.readLine());
            dValue = Integer.parseInt(br.readLine());
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }
        maxHealth = hValue;
        SetHealth(hValue);
        damage = dValue;
    }
    public void FireProjectile(GameBoard GB,Entity eValue)
    {
        String direction = "";
        if (eValue.icon.getX() == icon.getX())
        {
            if (eValue.icon.getY() > icon.getY())
            {
                direction = "Down";

            }
            else
            {
                direction = "Up";
            }
        }
        else
        {
            if (eValue.icon.getX() > icon.getX())
            {
                direction = "Right";

            }
            else
            {
                direction = "Left";
            }
        }
        ChangeAppearance(1);
        if (enemyType.equals("Archer"))
        {
            Projectile arrow = new Projectile(GB,direction,"Arrow",this,damage);
        }
        else if (enemyType.equals("Warlock"))
        {
            Projectile dreadFireball = new Projectile(GB,direction,"DreadFireball",this,damage);
        }
    }
















}
