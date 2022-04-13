import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Random;

public class DungeonQuest {
    Action upAction;
    Action leftAction;
    Action rightAction;
    Action downAction;
    Action menuAction;
    Action attackAction;
    Action abilityAction;
    Action interactAction;
    Action blockAction;

    Action relic1Action;
    Action relic2Action;
    Action relic3Action;
    Action relic4Action;
    Action relic5Action;
    Action relic6Action;


    Action testAction;

//    static String directory = "C:\\Users\\jack\\Dropbox\\Jack's Stuff\\Leisure\\New Coding stuff\\Java Programs\\Dungeon-Quest-2\\src";
    static String directory = Path.of("").toAbsolutePath().toString() + "\\src";

    GameBoard GB;
    Player myPlayer;
    int cycleNumber;
    Timer gameCycle;
    PlayerMenu PM;

    static boolean allEntitiesFrozen = false;
    static int entitiesFrozenCount = 0;

    static int numberOfRelics = 6;
    static int numberOfSwords = 6;
    static int numberOfShields = 3;


    static GameDialog rewardDialog[];
    static int numberOfRewardDialogs = 0;


    DungeonQuest(Player pValue)
    {
        CreateNewLevel(pValue);
    }
    public void BeginGame()
    {
        int cycleDelay = 500;//Milliseconds
        myPlayer.SetHealth(myPlayer.health);
        cycleNumber=0;
        gameCycle = new Timer(cycleDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GB.skipRound == false) {
                    PerformGameCycle(cycleNumber);
                    cycleNumber++;
                    if (cycleNumber > 10)
                        {
                            cycleNumber = 0;
                        }
                    }
                    else
                    {
                        GB.skipRound = false;
                    }
            }
        });
        GB.ChangeStateOfControls(true,myPlayer);
        gameCycle.start();
    }
    public void PerformGameCycle(int cycleNumber)
    {
        int modCycle;
        int modi;
        modCycle = cycleNumber%2;
        CheckIfPlayerDead();
        CheckIfEntitiesAreFrozen();
        if (allEntitiesFrozen == false)
        {
            GB.CheckStoryOptions(myPlayer);
            if (allEntitiesFrozen == false)
            {
                GB.ChangeStateOfControls(true, myPlayer);
            }
        }
            GB.CheckPlayerLocation(myPlayer);
            GB.CheckAllProjectiles();
            GB.MoveAllProjectiles(myPlayer);
            GB.TrapActions(cycleNumber);
            for (int i=0;i<GB.numberOfEnemies;i++)
            {
                modi = i%2;
                if (modi == modCycle)
                {
                    if (allEntitiesFrozen == false)
                    {
                        GB.enemies[i].DoEnemyCycle(myPlayer, GB);
                    }
                }
                else
                {
                    if ((cycleNumber==5)|(cycleNumber==10))
                    {
                        GB.enemies[i].ChangeAppearance(0);
                    }
                }
            }
            for (int i=0;i<GB.numberOfAllies;i++)
            {
                if (allEntitiesFrozen == false) {
                    GB.allies[i].DoAllyCycle(GB);
                }
            }
            modCycle =cycleNumber%5;
            if (modCycle == 0)
            {
                GB.EnsureDeadEnemiesHidden();
            }
            GB.CheckOtherEntities(myPlayer);
            GB.CheckStunnedEntities(myPlayer);
            GB.CheckIfWon(myPlayer);
        if (GB.won == true)
        {
            if (myPlayer.level < 120)
            {
                myPlayer.level++;
                MoveToNewLevel();
            }
            else
            {
                myPlayer.level++;
                gameCycle.stop();
                GB.dispose();
                CinematicScreen CS = new CinematicScreen(GB,myPlayer,"Outro");
                myPlayer.SavePlayer();
            }
        }
    }
    public static void FreezeAllEntities(int count)
    {
        allEntitiesFrozen = true;
        entitiesFrozenCount = count;
    }
    public static void FreezeAllEntitiesIndefinitely()
    {
        allEntitiesFrozen = true;
        entitiesFrozenCount = -1;
    }
    public static void UnfreezeAllEntities()
    {
        allEntitiesFrozen = false;
        entitiesFrozenCount = 0;
    }

    public void CheckIfEntitiesAreFrozen()
    {
        if (allEntitiesFrozen == true)
        {
            if (entitiesFrozenCount !=-1) {
                entitiesFrozenCount--;
                if (entitiesFrozenCount == 0) {
                    allEntitiesFrozen = false;
                }
            }
        }
    }

    public class InteractAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            myPlayer.InteractAction(GB,true);
        }
    }
    public class UpAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            myPlayer.MoveEntity(GB,"Up");
            myPlayer.SetAttackZone();
            GB.SetPlayerPosition(myPlayer);
        }
    }
    public class LeftAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            myPlayer.MoveEntity(GB,"Left");
            myPlayer.SetAttackZone();
            GB.SetPlayerPosition(myPlayer);

        }
    }
    public class RightAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            myPlayer.MoveEntity(GB,"Right");
            myPlayer.SetAttackZone();
            GB.SetPlayerPosition(myPlayer);

        }
    }
    public class DownAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            myPlayer.MoveEntity(GB,"Down");
            myPlayer.SetAttackZone();
            GB.SetPlayerPosition(myPlayer);
        }
    }
    public class MenuAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e){
            ChangeStateOfGame(false);
            myPlayer.ChangeAppearance(0);
            PlayerMenu PM = new PlayerMenu(myPlayer, GB);
            DungeonQuest.PlaySound(directory + "\\OtherSounds\\MenuOpen.wav");
        }
    }
    public class AttackAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            myPlayer.AttackAction(GB,"Normal");
        }
    }
    public class BlockAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            myPlayer.BlockAction(GB);
        }
    }
    public class Relic1Action extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            SelectRelic(0);

        }
    }
    public class Relic2Action extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            SelectRelic(1);
        }
    }
    public class Relic3Action extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            SelectRelic(2);
        }
    }
    public class Relic4Action extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            SelectRelic(3);
        }
    }
    public class Relic5Action extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            SelectRelic(4);

        }
    }
    public class Relic6Action extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            SelectRelic(5);
        }
    }
    public class AbilityAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            myPlayer.UseRelic(GB);
        }
    }
    public class TestAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }
    public void ChangeStateOfGame (boolean state)//true for resume game, false for pause it
    {
        GB.ChangeStateOfControls(state,myPlayer);
        if (state==true)
        {
            gameCycle.start();
            DungeonQuest.UnfreezeAllEntities();
        }
        else if (state ==false)
        {
            gameCycle.stop();
        }

    }

    static public void PlaySound(String fileName)
    {

        try
        {
            File audioFile = new File( fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        }
        catch(Exception exc)
        {
            System.out.println("Audio error");
            exc.printStackTrace();
        }
    }
    public void SelectRelic(int relicNumber)
    {
        if (myPlayer.GetOneRelic(relicNumber) == null)
        {
            DungeonQuest.PlaySound(DungeonQuest.directory + "\\Relic\\fail.wav");
        }
        else
        {
            myPlayer.SetCurrentRelic(relicNumber, GB);
        }
    }
    public static String[] AddStringToArray(String[] array, String newItem)
    {
        String[] newArray = new String[array.length + 1];
        for (int i =0;i < array.length;i++)
        {
            newArray[i] = array[i];
        }
        newArray[array.length] = newItem;
        return newArray;
    }
    public static Sword[] AddSwordToArray(Sword[] array, Sword newItem)
    {
        Sword[] newArray = new Sword[array.length + 1];
        for (int i =0;i < array.length;i++)
        {
            newArray[i] = array[i];
        }
        newArray[array.length] = newItem;
        return newArray;
    }
    public void MoveToNewLevel()
    {
        myPlayer.SavePlayer();
        GB.dispose();
        gameCycle.stop();
        gameCycle = null;
        cycleNumber = 0;
        CreateNewLevel(myPlayer);
    }
    public void CreateNewLevel(Player pValue)
    {
        GB = new GameBoard();
        myPlayer = new Player(GB,pValue.name,pValue.fileNumber);
        myPlayer.healthBar.UpdateStatBar(myPlayer.health);
        GB.playerPosition = myPlayer.icon.getLocation();
        GB.levelNumber = myPlayer.level;
        LoadInRewardDialog();
        GB.SetUpLevel();
        CreateControls();
        if (myPlayer.currentRelic != null)
        {
            GB.SetCurrentRelicIcon(myPlayer.currentRelic.toolID,myPlayer.kingsoul);
            GB.SetCurrentSwordIcon(myPlayer.currentSword);
        }
        myPlayer.icon.grabFocus();
        BeginGame();
    }
    public void LoadInRewardDialog()
    {
        numberOfRewardDialogs = 4;
        rewardDialog = new GameDialog[numberOfRewardDialogs];
        String rewardID = "";
        for (int i =0 ;i<numberOfRewardDialogs;i++)
        {
            if (i == 0)
            {
                rewardID = "Basic";
            }
            else if (i ==1)
            {
                rewardID = "Heart";
            }
            else if (i == 2)
            {
                rewardID = "Energy";
            }
            else if (i == 3)
            {
                rewardID = "Sword";
            }
            rewardDialog[i] = new GameDialog("Reward",rewardID);
        }
    }
    public void CreateControls()
    {
        //Creating all the Actions
        //--------------------------------------------------------------------------------------------------------------
        upAction = new UpAction();
        leftAction = new LeftAction();
        rightAction = new RightAction();
        downAction = new DownAction();
        menuAction = new MenuAction();
        attackAction = new AttackAction();
        abilityAction = new AbilityAction();
        interactAction = new InteractAction();
        blockAction = new BlockAction();

        relic1Action = new Relic1Action();
        relic2Action = new Relic2Action();
        relic3Action = new Relic3Action();
        relic4Action = new Relic4Action();
        relic5Action = new Relic5Action();
        relic6Action = new Relic6Action();


        testAction = new TestAction();
        //--------------------------------------------------------------------------------------------------------------
        //Moving
        //--------------------------------------------------------------------------------------------------------------
        //Arrow keys
        myPlayer.icon.getInputMap().put(KeyStroke.getKeyStroke("UP"), "upAction");
        myPlayer.icon.getActionMap().put("upAction", upAction);
        myPlayer.icon.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "leftAction");
        myPlayer.icon.getActionMap().put("leftAction", leftAction);
        myPlayer.icon.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "rightAction");
        myPlayer.icon.getActionMap().put("rightAction", rightAction);
        myPlayer.icon.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "downAction");
        myPlayer.icon.getActionMap().put("downAction", downAction);
        //--------------------------------------------------------------------------------------------------------------
        //letter keys
        myPlayer.icon.getInputMap().put(KeyStroke.getKeyStroke('w'), "upAction");
        myPlayer.icon.getActionMap().put("upAction", upAction);
        myPlayer.icon.getInputMap().put(KeyStroke.getKeyStroke('a'), "leftAction");
        myPlayer.icon.getActionMap().put("leftAction", leftAction);
        myPlayer.icon.getInputMap().put(KeyStroke.getKeyStroke('d'), "rightAction");
        myPlayer.icon.getActionMap().put("rightAction", rightAction);
        myPlayer.icon.getInputMap().put(KeyStroke.getKeyStroke('s'), "downAction");
        myPlayer.icon.getActionMap().put("downAction", downAction);
        //--------------------------------------------------------------------------------------------------------------
        //Interacting
        myPlayer.icon.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "attackAction");
        myPlayer.icon.getActionMap().put("attackAction", attackAction);
        myPlayer.icon.getInputMap().put(KeyStroke.getKeyStroke('r'), "interactAction");
        myPlayer.icon.getActionMap().put("interactAction", interactAction);
        myPlayer.icon.getInputMap().put(KeyStroke.getKeyStroke('e'), "openMenu");
        myPlayer.icon.getActionMap().put("openMenu", menuAction);
        myPlayer.icon.getInputMap().put(KeyStroke.getKeyStroke('f'), "abilityAction");
        myPlayer.icon.getActionMap().put("abilityAction", abilityAction);
        myPlayer.icon.getInputMap().put(KeyStroke.getKeyStroke('q'), "blockAction");
        myPlayer.icon.getActionMap().put("blockAction", blockAction);
        //--------------------------------------------------------------------------------------------------------------
        //Activating Relics
        myPlayer.icon.getInputMap().put(KeyStroke.getKeyStroke('1'), "relicAction1");
        myPlayer.icon.getActionMap().put("relicAction1", relic1Action);
        myPlayer.icon.getInputMap().put(KeyStroke.getKeyStroke('2'), "relicAction2");
        myPlayer.icon.getActionMap().put("relicAction2", relic2Action);
        myPlayer.icon.getInputMap().put(KeyStroke.getKeyStroke('3'), "relicAction3");
        myPlayer.icon.getActionMap().put("relicAction3", relic3Action);
        myPlayer.icon.getInputMap().put(KeyStroke.getKeyStroke('4'), "relicAction4");
        myPlayer.icon.getActionMap().put("relicAction4", relic4Action);
        myPlayer.icon.getInputMap().put(KeyStroke.getKeyStroke('5'), "relicAction5");
        myPlayer.icon.getActionMap().put("relicAction5", relic5Action);
        myPlayer.icon.getInputMap().put(KeyStroke.getKeyStroke('6'), "relicAction6");
        myPlayer.icon.getActionMap().put("relicAction6", relic6Action);
        //--------------------------------------------------------------------------------------------------------------
        myPlayer.icon.getInputMap().put(KeyStroke.getKeyStroke('t'), "testAction");
        myPlayer.icon.getActionMap().put("testAction", testAction);

        //--------------------------------------------------------------------------------------------------------------
        //Starting game when focused on
        //--------------------------------------------------------------------------------------------------------------
        GB.addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e)
            {
                myPlayer.icon.requestFocus();
                if (gameCycle != null)
                {
                    ChangeStateOfGame(true);
                }
            }
        });
    }
    public void CheckIfPlayerDead()
    {
        if (myPlayer.dead == true)
        {
            GB.dispose();
            gameCycle.stop();
        }
    }
    public static int[] RemoveIntFromArray(int[] oldArray, int index)
    {
        int newArray[] = new int[oldArray.length -1 ];
        for (int i=0;i<index;i++)
        {
            newArray[i] = oldArray[i];
        }
        for (int i =index + 1;i< oldArray.length;i++)
        {
            newArray[(i -1)] = oldArray[i];
        }
        return newArray;
    }
    public static Player[] AddPlayerToArray(Player[] array, Player newItem)
    {
        Player[] newArray = new Player[array.length + 1];
        for (int i =0;i < array.length;i++)
        {
            newArray[i] = array[i];
        }
        newArray[array.length] = newItem;
        return newArray;
    }
    public static Player[] RemovePlayerFromArray(Player[] array,Player itemToRemove)
    {
        int index = SearchArrayForPlayer(array,itemToRemove);
        Player newArray[] = new Player[array.length -1];
        if (index !=-1)
        {
            for (int i=0;i<index;i++)
            {
                newArray[i] = array[i];
            }
            for (int i =index + 1;i< array.length;i++)
            {
                newArray[(i -1)] = array[i];
            }
            return newArray;
        }
        return null;
    }
    public static Enemy[] RemoveEnemyFromArray(Enemy[] array,Enemy itemToRemove)
    {
        int index = SearchArrayForEnemy(array,itemToRemove);
        Enemy newArray[] = new Enemy[array.length -1];
        if (index !=-1)
        {
            for (int i=0;i<index;i++)
            {
                newArray[i] = array[i];
            }
            for (int i =index + 1;i< array.length;i++)
            {
                newArray[(i -1)] = array[i];
            }
            return newArray;
        }
        return null;
    }
    public static int SearchArrayForEnemy(Enemy[] array, Enemy pValue)
    {
        for (int i=0;i< array.length;i++)
        {
            if (array[i] == pValue)
            {
                return i;
            }
        }
        return -1;
    }
    public static Entity[] RemoveEntityFromArray(Entity[] array,Entity itemToRemove)
    {
        int index = SearchArrayForEntity(array,itemToRemove);
        Entity newArray[] = new Entity[array.length -1];
        if (index !=-1)
        {
            for (int i=0;i<index;i++)
            {
                newArray[i] = array[i];
            }
            for (int i =index + 1;i< array.length;i++)
            {
                newArray[(i -1)] = array[i];
            }
            return newArray;
        }
        return null;
    }
    public static int SearchArrayForEntity(Entity[] array, Entity pValue)
    {
        for (int i=0;i< array.length;i++)
        {
            if (array[i] == pValue)
            {
                return i;
            }
        }
        return -1;
    }
    public static int SearchArrayForPlayer(Player[] array, Player pValue)
    {
        for (int i=0;i< array.length;i++)
        {
            if (array[i] == pValue)
            {
                return i;
            }
        }
        return -1;
    }
    public static boolean CheckIfPlayerExists(String username)
    {
        int count = 3;
        for (int i =0;i<3;i++)
        {
            try
            {
                BufferedReader br = new BufferedReader(new FileReader(directory + "\\Players\\" + username + "-" + i + ".txt"));
            }
            catch(Exception exc)
            {
                count--;
            }
        }
        if (count > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public static Relic[] AddRelicToArray(Relic[] array, Relic newItem)
    {
        Relic[] newArray = new Relic[array.length + 1];
        for (int i =0;i < array.length;i++)
        {
            newArray[i] = array[i];
        }
        newArray[array.length] = newItem;
        return newArray;
    }
    public static int[] AddIntToArray(int[] array, int newItem)
    {
        int[] newArray = new int[array.length + 1];
        for (int i =0;i < array.length;i++)
        {
            newArray[i] = array[i];
        }
        newArray[array.length] = newItem;
        return newArray;
    }
    public static Enemy[] AddEnemyToArray(Enemy[] array, Enemy newItem)
    {
        Enemy[] newArray = new Enemy[array.length + 1];
        for (int i =0;i < array.length;i++)
        {
            newArray[i] = array[i];
        }
        newArray[array.length] = newItem;
        return newArray;
    }
    public static void ChangeJOptionPaneFontType(String type)
    {
        if (type.equals("Plain"))
        {
            UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Century", Font.PLAIN, 15)));

        }
        else if (type.equals("Italic"))
        {
            UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Monotype Corsiva", Font.ITALIC, 15)));

        }
    }
    public static TakenPoint[] AddTakenPointToArray(TakenPoint[] array, TakenPoint newItem)
    {
        TakenPoint[] newArray = new TakenPoint[array.length + 1];
        for (int i =0;i < array.length;i++)
        {
            newArray[i] = array[i];
        }
        newArray[array.length] = newItem;
        return newArray;
    }
    public static TakenPoint[] RemoveTakenPointFromArray(TakenPoint[] array,TakenPoint itemToRemove)
    {
        int index = SearchArrayForTakenPoint(array,itemToRemove);
        TakenPoint newArray[] = new TakenPoint[array.length -1];
        if (index !=-1)
        {
            for (int i=0;i<index;i++)
            {
                newArray[i] = array[i];
            }
            for (int i =index + 1;i< array.length;i++)
            {
                newArray[(i -1)] = array[i];
            }
            return newArray;
        }
        return null;
    }
    public static int SearchArrayForTakenPoint(TakenPoint[] array, TakenPoint pValue)
    {
        for (int i=0;i< array.length;i++)
        {
            if (array[i] == pValue)
            {
                return i;
            }
        }
        return -1;
    }
    public static GameDialog[] RemoveGameDialogFromArray(GameDialog[] array,GameDialog itemToRemove)
    {
        int index = SearchArrayForGameDialog(array,itemToRemove);
        GameDialog newArray[] = new GameDialog[array.length -1];
        if (index !=-1)
        {
            for (int i=0;i<index;i++)
            {
                newArray[i] = array[i];
            }
            for (int i =index + 1;i< array.length;i++)
            {
                newArray[(i -1)] = array[i];
            }
            return newArray;
        }
        return null;
    }
    public static int SearchArrayForGameDialog(GameDialog[] array, GameDialog pValue)
    {
        for (int i=0;i< array.length;i++)
        {
            if (array[i] == pValue)
            {
                return i;
            }
        }
        return -1;
    }
    public static Item[] RemoveItemFromArray(Item[] array,Item itemToRemove)
    {
        int index = SearchArrayForItem(array,itemToRemove);
        Item newArray[] = new Item[array.length -1];
        if (index !=-1)
        {
            for (int i=0;i<index;i++)
            {
                newArray[i] = array[i];
            }
            for (int i =index + 1;i< array.length;i++)
            {
                newArray[(i -1)] = array[i];
            }
            return newArray;
        }
        return null;
    }
    public static int SearchArrayForItem(Item[] array, Item pValue)
    {
        for (int i=0;i< array.length;i++)
        {
            if (array[i] == pValue)
            {
                return i;
            }
        }
        return -1;
    }


    }


