import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;

public final class Relic extends  Tool{
    boolean relic6Collected = false;
    boolean kingSoul = false;

    int cost = 4;

    Relic(int rid, boolean ksValue)
    {
        super(rid,'r');
        kingSoul = ksValue;
        if ((toolID == 5) & (kingSoul))
        {
            symbol = new ImageIcon(Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\Tool\\r\\" + toolID + "Kingsoul.png")));
            selectedSymbol = new ImageIcon(Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\Tool\\r\\" + toolID + "KingsoulSelected.png")));
        }
    }
    public void UseRelic(Player myPlayer, GameBoard GB)
    {
        if (toolID ==0)
        {
            RelicAction0(myPlayer);
        }
        if (toolID ==1)
        {
            RelicAction1(GB,myPlayer);
        }
        if (toolID ==2)
        {
            RelicAction2(GB,myPlayer);
        }
        if (toolID ==3)
        {
            RelicAction3(GB,myPlayer);
        }
        if (toolID == 4)
        {
            RelicAction4(GB,myPlayer);
        }
        if (toolID == 5)
        {
            RelicAction5(GB,myPlayer);
        }
        myPlayer.DecreaseEnergy(cost);
    }
    public void ChangeSelected(boolean value)
    {
        if (value ==true)
        {
            if ((toolID == 5) & (kingSoul))
            {
                symbol = new ImageIcon(Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\Tool\\r\\" + toolID + "KingsoulSelected.png")));
            }
            else
            {
                symbol = new ImageIcon(Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\Tool\\r\\" + toolID + "Selected.png")));
            }
        }
        else
        {
            if ((toolID == 5) & (kingSoul))
            {
                symbol = new ImageIcon(Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\Tool\\r\\" + toolID + "Kingsoul.png")));
            }
            else
            {
                symbol = new ImageIcon(Toolkit.getDefaultToolkit().getImage((DungeonQuest.directory + "\\Tool\\r\\" + toolID + ".png")));
            }
        }
    }
    public void RelicAction0(Player myPlayer)
    {
        if (myPlayer.health != myPlayer.maxHealth)
        {
            myPlayer.IncreaseHealth();
            myPlayer.IncreaseHealth();
            if (relic6Collected)
            {
                myPlayer.IncreaseHealth();
            }
            DungeonQuest.PlaySound(DungeonQuest.directory + "\\Tool\\r\\" + toolID + ".wav");
        }
        else
        {
            DungeonQuest.PlaySound(DungeonQuest.directory + "\\Tool\\r\\fail.wav");
        }



    }
    public void RelicAction1(GameBoard GB,Player myPlayer)
    {
        int shieldHealth =0;
        if ((myPlayer.invulnerableCount > 0) & (relic6Collected == false))
        {
            DungeonQuest.PlaySound(DungeonQuest.directory + "\\Tool\\r\\fail.wav");
        }
        else
        {
            if (relic6Collected)
            {
                shieldHealth = 2;
            }
            else
            {
                shieldHealth = 1;
            }
            if (myPlayer.bubble != null)
            {
                myPlayer.bubble.setVisible(false);
                myPlayer.bubble = null;
            }
            myPlayer.invulnerableCount = shieldHealth;
            myPlayer.bubble = new StatBar(GB,myPlayer.icon.getLocation(),shieldHealth,shieldHealth,"Shield");
            myPlayer.ChangeAppearance(0);
            DungeonQuest.PlaySound(DungeonQuest.directory + "\\Tool\\r\\" + toolID + ".wav");
        }

    }
    public void RelicAction2(GameBoard GB, Player myPlayer)
    {
        if (relic6Collected)
        {
            myPlayer.AttackAction(GB,"Special2");
        }
        else
        {
            myPlayer.AttackAction(GB,"Special");
        }
    }
    public void RelicAction3(GameBoard GB, Player myPlayer)
    {
        int damageValue = 2;
        if (relic6Collected)
        {
            damageValue = 4;
        }
        if (myPlayer.CheckMove(GB,myPlayer.facing))
        {
            Projectile fireball = new Projectile(GB,myPlayer.facing,"Fireball",myPlayer,damageValue);
        }
        else
        {
            DungeonQuest.PlaySound(DungeonQuest.directory + "\\Tool\\r\\fail.wav");
        }
    }
    public void RelicAction4(GameBoard GB, Player myPlayer)
    {
        int enemyIndex = -1;
        myPlayer.InteractAction(GB,false);
        for (int i =0;i<GB.enemies.size();i++)
        {
            for (int j =0 ; j< myPlayer.attackZone.length;j++)
            {
                if ((myPlayer.attackZone[j].getX() == GB.enemies.get(i).icon.getX()) & (myPlayer.attackZone[j].getY() == GB.enemies.get(i).icon.getY()))
                {
                    if ((GB.enemies.get(i).type == EntityType.ENEMY)|(GB.enemies.get(i).type == EntityType.RANGEDENEMY)) {
                        enemyIndex = i;
                    }
                    else
                    {
                        DungeonQuest.PlaySound(DungeonQuest.directory + "\\Tool\\r\\fail.wav");
                    }
                }
            }
        }
        if (enemyIndex != -1)
        {
            GB.MakeEnemyAlly(enemyIndex);
            if (relic6Collected)
            {
                GB.allies.get((GB.allies.size() -1)).damage++;
                GB.allies.get((GB.allies.size() -1)).IncreaseHealth();
            }
            DungeonQuest.PlaySound(DungeonQuest.directory + "\\Tool\\r\\" + toolID + ".wav");
        }
    }
    public void RelicAction5(GameBoard GB,Player myPlayer)
    {
        DungeonQuest.PlaySound(DungeonQuest.directory + "\\Tool\\r\\" + toolID + ".wav");
        myPlayer.ChangeAppearance(6);
        for (int i = 0;i<GB.enemies.size();i++)
        {
            if (myPlayer.CheckIfAroundEntity(GB.enemies.get(i)))
            {
                GB.enemies.get(i).StunEntity(true);
                GB.HurtEnemy(i,1);
            }

        }
    }




}
