public final class Shield extends Tool{
    Shield(int shid) {
        super(shid,'h');
    }
    public void UseShield(GameBoard GB,Player myPlayer)
    {
        int enemyIndex;
        if (toolID ==1)
        {
            if (GB != null)
            {
                enemyIndex = GB.CheckForEnemies(myPlayer);
                GB.HurtEnemy(GB.enemies.get(enemyIndex),1);
            }
        }
        else if (toolID ==2)
        {

        }
    }
}
