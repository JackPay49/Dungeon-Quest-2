import java.util.Random;

public class Boss extends Enemy{


    Boss(GameBoard GB,int difficultyLevel)
    {
        super(GB,'b',difficultyLevel);
        ChangeAppearance(0);

    }
    @Override
    public void DecideEnemyType(int difficultyLevel)
    {
        Random r = new Random();
        int randomNumber = r.nextInt(4);
        if (randomNumber == 0)
        {
            enemyType = "Wizard";
        }
        else if (randomNumber == 1)
        {
            enemyType = "Druid";
        }
        else if (randomNumber == 2)
        {
            enemyType = "Mage";
        }
        else if (randomNumber == 3)
        {
            enemyType = "Alchemist";
        }
        LoadEnemyTypeInfo(difficultyLevel);
    }
    @Override
    public void AttackEntity(Entity eValue)
    {
        ChangeAppearance(1);
        eValue.EntityHurt(damage);
        if (enemyType.equals("Druid"))
        {
            IncreaseHealth();
            healthBar.UpdateStatBar(health);
        }
    }
    @Override
    public void MakeSound(Integer index)//0 idle, 1 attacking, 2 moving, 3 hurt, 4 dead
    {
        String entity = "";
        String action = "";
        String audioFile;
        int numberOfSounds = 0;
        Random r = new Random();
        int randomNumber;
        entity = "\\Enemy\\b";

        if (index == 0)
        {
            action = "\\Idle";
        }
        else if (index == 1)
        {
            action = "\\Attacking";
            if (enemyType.equals("Wizard"))
            {
                numberOfSounds = 3;
            }
            else if ((enemyType.equals("Druid"))|(enemyType.equals("Mage"))|(enemyType.equals("Alchemist")))
            {
                numberOfSounds = 2;
            }

        }
        else if (index == 2)
        {
            action = "\\Moving";

        }
        else if (index == 3)
        {
            action = "\\Hurt";

        }
        else if (index == 4)
        {
            action = "\\Dying";
            numberOfSounds = 1;

        }
        else if (index == 5)
        {
            action = "\\Special";
        }
        else if (index == 6)
        {
            action = "\\SummoningEnemies";
            if (enemyType.equals("Alchemist"))
            {
                numberOfSounds = 2;
            }
        }
        if (numberOfSounds != 0)
        {
            randomNumber = r.nextInt(numberOfSounds) + 1;
            audioFile = directory + entity + "\\" + enemyType + action + randomNumber + ".wav";
            DungeonQuest.PlaySound(audioFile);
        }
    }
    @Override
    public void FireProjectile(GameBoard GB,Entity eValue)
    {
        ChangeAppearance(1);
        String currentDirection ="";
        for (int i = 0;i<4;i++)
        {
            if (i ==0)
            {
                currentDirection = "Right";
            }
            else if (i ==1)
            {
                currentDirection = "Left";
            }
            else if (i ==2)
            {
                currentDirection = "Up";
            }
            else if (i ==3)
            {
                currentDirection = "Down";
            }
            if (CheckMove(GB,currentDirection))
            {
                Projectile fireball = new Projectile(GB,currentDirection,"Fireball",this,damage);
            }
        }

    }

}
