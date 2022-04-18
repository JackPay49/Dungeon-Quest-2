import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.spi.CurrencyNameProvider;

public final class Arbiter extends Boss {
    String spellCurrentlyCasting = "";//Teleport, Dying, etc
    int spellCount = -1;

    int invulnerableCount = 0;
    StatBar shield;

    Arbiter(GameBoard GB, int difficultyLevel)
    {
        super(GB,difficultyLevel);
        enemyType = "Arbiter";
        name = "Arbiter";
        facing = "Left";
        ChangeAppearance(0);
        LoadEnemyTypeInfo(difficultyLevel);
    }
    @Override
    public void AttackEntity(GameBoard GB,Entity eValue)
    {
        ChangeAppearance(1);
        eValue.EntityHurt(GB,damage);
    }
    @Override
    public void EntityHurt(GameBoard GB,int amount)
    {
        Random r = new Random();
        int randomNumber;
        if (invulnerableCount > 0)
        {
            invulnerableCount--;
            shield.UpdateStatBar(invulnerableCount);
            if (invulnerableCount == 0) {
                shield.setVisible(false);
                shield = null;
                ChangeAppearance(3);
                DungeonQuest.PlaySound(DungeonQuest.directory + "\\Tool\\r\\1Hit.wav");
            }
            else
            {
                MakeSound(3);
            }
        }
        else if (invulnerableCount <= 0) {
            for (int i = 0; i < amount; i++) {
                DecreaseHealth();
                if (health == 0) {
                    break;
                }
            }
            UpdateHealthBar();
            if (health > 0)
            {
                ChangeAppearance(3);
                randomNumber = r.nextInt(10);
                if (randomNumber == 0)
                {
                    MakeShield(GB);
                }
            }
            else
            {
                KillEntity();
            }
        }
    }
    @Override
    public void UpdateStatBarLocations()
    {
        super.UpdateStatBarLocations();
        if (invulnerableCount > 0)
        {
            shield.UpdateLocation(icon.getLocation());
        }
    }
    public void MakeShield(GameBoard GB)
    {
        MakeSound(8);
        invulnerableCount = 4;
        shield = new StatBar(GB,icon.getLocation(),invulnerableCount,invulnerableCount,"Dread Shield");
        ChangeAppearance(0);
    }
    public void TeleportToNewLocation(GameBoard GB)
    {
        if ((spellCount == 1) & (spellCurrentlyCasting.equals("Teleport")))
        {
            RandomlyPlace(GB);
            CastSpell("");
        }
        else if (spellCount == -1)
        {
            ChangeAppearance(7);
            CastSpell("Teleport");
        }
    }
    public void CastSpell(String spell)
    {
        if (spell.equals(""))
        {
            spellCount = -1;
            spellCurrentlyCasting = "";
        }
        else if (spell.equals("Teleport"))
        {
            spellCount = 1;
            spellCurrentlyCasting = "Teleport";
        }
    }
    @Override
    public void SummonMoreEnemies(GameBoard GB)
    {
        Random r = new Random();
        int randomNumber;
        int numberOfMoreEnemies = -1;
        Enemy newEnemy = null;
        Boss newBoss =  null;
        if (GB.enemies.size() < 5)
        {
            ChangeAppearance(6);
            randomNumber = r.nextInt(5);
            r = new Random();
            if (randomNumber < 4) {
                numberOfMoreEnemies = ((r.nextInt(5 - GB.enemies.size())) + 2);
                for (int i = 0; i < numberOfMoreEnemies; i++) {
                    newEnemy = new Enemy(GB, GB.difficultyLevel);
                    newEnemy.ChangeAppearance(6);
                    GB.enemies.add(newEnemy);
                }
            }
            else if (randomNumber == 4)
            {
                newBoss = new Boss(GB,GB.difficultyLevel);
                newBoss.ChangeAppearance(0);//Chaqnge to 6
                GB.enemies.add(newBoss);

            }
        }
    }
    public void SummonFireball(GameBoard GB)
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
                Projectile dreadFireball = new Projectile(GB,currentDirection,"DreadFireball",this,damage);
            }
        }
    }
    @Override
    public void DoEnemyCycle(Entity eValue, GameBoard GB)//will either attack an entity or move towards them
    {
        Boolean withinAttackZone = CheckIfEntityWithinAttackZone(eValue);
        Boolean canSeeEntity = CheckIfCanSeeEntity(eValue);
        boolean onSameRow = CheckIfOnSameRow(eValue);
        boolean onSameColumn = CheckIfOnSameColumn(eValue);
        Random r = new Random();
        int randomNumber = -1;
        if (animationCount <= 0)
        {
            if (spellCount != -1) {
                if (spellCurrentlyCasting.equals("Teleport")) {
                    TeleportToNewLocation(GB);
                }
            } else {
                if (stunned == false) {
                    FaceEntity(eValue);
                    if (canSeeEntity == true) {
                        if (withinAttackZone == true) {
                            AttackEntity(GB,eValue);
                            GB.ChangeStateOfControls(false, eValue);
                            GB.skipRound = true;
                        } else {
                            if ((onSameRow == true) | (onSameColumn == true)) {
                                SummonFireball(GB);
                            } else {
                                MoveTowardsEntity(eValue, GB);
                            }
                        }
                    } else {
                        if (GB.allies.size() != 0) {
                            eValue = GB.allies.get(0);
                            canSeeEntity = CheckIfCanSeeEntity(eValue);
                            withinAttackZone = CheckIfEntityWithinAttackZone(eValue);
                            FaceEntity(eValue);
                            if (canSeeEntity == true) {
                                if (withinAttackZone == true) {
                                    AttackEntity(GB,eValue);
                                    GB.HurtAlly(0, damage);
                                    GB.skipRound = true;

                                } else {
                                    MoveTowardsEntity(eValue, GB);
                                }
                            } else {
                                randomNumber = r.nextInt(6);
                                if (randomNumber == 0) {
                                    ChangeAppearance(0);
                                } else if ((randomNumber >= 1) & (randomNumber < 3)) {
                                    r = new Random();
                                    randomNumber = r.nextInt(4);
                                    if (randomNumber <= 2) {
                                        TeleportToNewLocation(GB);
                                    } else {
                                        DoRandomMovement(GB);
                                    }
                                } else if ((randomNumber >= 3) & (randomNumber < 7)) {
                                    SummonMoreEnemies(GB);
                                } else if ((randomNumber >= 7)) {
                                    MakeShield(GB);
                                }
                            }
                        } else {
                            randomNumber = r.nextInt(6);
                            if (randomNumber == 0) {
                                ChangeAppearance(0);
                            } else if ((randomNumber >= 1) & (randomNumber < 3)) {
                                r = new Random();
                                randomNumber = r.nextInt(4);
                                if (randomNumber <= 2) {
                                    TeleportToNewLocation(GB);
                                } else {
                                    DoRandomMovement(GB);
                                }
                            } else if ((randomNumber >= 3) & (randomNumber < 7)) {
                                SummonMoreEnemies(GB);
                            } else if ((randomNumber >= 7)) {
                                MakeShield(GB);
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
    @Override
    public void ChangeAppearance(Integer index) //0 idle, 1 attacking, 2 moving, 3 hurt, 4 dead, 5 stunned, 6 Summoning enemies
    {
        ImageIcon tempIcon = new ImageIcon();
        String entity = "\\Enemy\\BOSS";
        String action = "";
        String entityFacing = facing;
        String imageFile;
        if (animationCount <= 0)
        {
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
                action = "\\SummoningEnemies";
                animationCount = 1;
            } else if (index == 7) {
                action = "\\Teleporting";
            }
            if (invulnerableCount > 0) {
                entityFacing = entityFacing + "Invulnerable";
            }
            imageFile = DungeonQuest.directory + entity + "\\" + enemyType + action + entityFacing + ".gif";
            tempIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(imageFile));
            tempIcon.getImage().flush();
            icon.setIcon(tempIcon);
        }
        MakeSound(index);
    }
    @Override
    public void MakeSound(Integer index)//0 idle, 1 attacking, 2 moving, 3 hurt, 4 dead
    {
        String entity = "\\Enemy\\BOSS";
        String action = "";
        String audioFile;
        int numberOfSounds = 0;
        Random r = new Random();
        int randomNumber;
        if (index == 0)
        {
            action = "\\Idle";
        }
        else if (index == 1)
        {
            action = "\\Attacking";
            numberOfSounds = 3;
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
        }
        else if (index == 5)
        {
            action = "\\Stunned";
        }
        else if (index == 6)
        {
            action = "\\SummoningEnemies";
            numberOfSounds = 2;
        }
        else if (index == 7)
        {
            action = "\\Teleporting";
            numberOfSounds = 1;

        }
        else if (index == 8)
        {
            action = "\\Shielding";
            numberOfSounds = 1;
        }
        if (numberOfSounds != 0)
        {
            randomNumber = r.nextInt(numberOfSounds) + 1;
            audioFile = DungeonQuest.directory + entity + "\\" + enemyType + action + randomNumber + ".wav";
            DungeonQuest.PlaySound(audioFile);
        }
    }

}
