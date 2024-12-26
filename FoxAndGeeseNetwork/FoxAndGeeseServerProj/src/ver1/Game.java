package ver1;

/**
 *  Game.
 *  By noamabutbul | 19/10/2022 14:53
 */
public class Game
{
    private final Player player1; // שחקן ראשון
    private final Player player2; // שחקן שני
    private Player currentPlayer; // מצביע על השחקן הנוכחי
    private boolean isFirstGame; // משתנה שמיועד לבדיקה האם זה לא משחק ראשון
    private boolean stopGame; // משתנה שמיועד להפסקת המשחק בעת ניתוק
    
    private State currentState; // המצב הנוכחי
    
    /**
     * פעולה בונה למשחק
     * @param player1 שחקן ראשון
     * @param player2 שחקן שני
     */
    public Game(Player player1, Player player2)
    {
        this.player1 = player1;
        this.player2 = player2;
        
        this.isFirstGame = true;
        this.stopGame = false;
    }

  
    /**
     * פעולה המתאחלת את המשחק
     */
    public void initGame()
    {        
        this.currentPlayer = player1;
       
        if (isFirstGame)
        {
            currentPlayer.takeYourSign(Consts.FOX_SIGN);
            getOpponent().takeYourSign(Consts.GEESE_SIGN);
        }
        
        this.currentState = new State();
        
        currentPlayer.startNewGame(currentState.getLogicBoard());
        getOpponent().startNewGame(currentState.getLogicBoard());
        
        isFirstGame = false;
        
        startGame();

    }
            
    /**
     * פעולה המתחילה משחק
     */
    public void startGame()
    {
        Thread gameThread = new Thread(() ->
        {
            if (!foxSelectionProcess())
                stopGame = true;
            
            while (!stopGame)
            {
                if (!moveProcess())
                {
                    stopGame = true;
                    break;
                }
                
                if (checkGameStatus() || stopGame)
                    break;
                
                switchTurn();
                
            }
            if (!stopGame)
            {
                if (currentPlayer.listenToFinishCounddown() && getOpponent().listenToFinishCounddown())
                    initGame();
            }
        });
        gameThread.start();
        gameThread.setName("Game Thread");
       
    }
    
    
    /**
     * פעולה לתהליך המהלך
     */
    private boolean moveProcess()
    {
        getOpponent().waitForYourTurn();
        Move move = currentPlayer.makeMove(currentState.getAllPossibleLocations(currentPlayer.getSign()), currentState);
        
        if (move == null)
            return false;
        currentState.makeMove(move, currentPlayer.getSign());

        updateState();

        getOpponent().playerMoved(move, currentPlayer.getSign());
        
        return true;
    }
    
    
    /**
     * פעולה לתהליך בחירת מיקום השועל
     */
    private boolean foxSelectionProcess()
    {
        getOpponent().waitForYourTurn();
        currentPlayer.chooseFirstLocation();
        
        Location foxLoc =  currentPlayer.listenToFoxStartLocation();
        
        if (foxLoc == null)
            return false;
        
        currentState.foxSelectedStartLocation(foxLoc);

        updateState();

        getOpponent().foxStartedLocation(currentState.getFoxLocation());

        switchTurn();
        
        return true;
    }
   
    
    /**
     * פעולה הבודקת את מצב המשחק
     * @return אמת לניצחון ואחרת שקר
     */
    private boolean checkGameStatus()
    {
        char winPlyer = currentState.checkStatus();
        if (winPlyer == Consts.FOX_SIGN)
        {
            currentPlayer.gameOver(Consts.FOX_SIGN);
            getOpponent().gameOver(Consts.FOX_SIGN);
            return true;
        }
        else if (winPlyer == Consts.GEESE_SIGN)
        {
            currentPlayer.gameOver(Consts.GEESE_SIGN);
            getOpponent().gameOver(Consts.GEESE_SIGN);
            return true;
        }
        return false;
    }
            
 
    /**
     * פעולה לעדכון שדות מצב המשחק
     */
    private void updateState()
    {
        currentState.getAllPossibleMoves(Consts.GEESE_SIGN);
        currentState.getAllPossibleMoves(Consts.FOX_SIGN);
    }

   
   
    /**
     * פעולה המחליפה תור
     */
    private void switchTurn()
    {
        if (currentPlayer == player1)
            currentPlayer = player2;
        else
            currentPlayer = player1;
    }
        
      
    /**
     * פעולה המחזירה את היריב
     * @return היריב
     */
    private Player getOpponent()
    {
        if (currentPlayer == player1)
            return player2;
        return player1;
    }
    
    /**
     * פעולה המחזירה את הפרטנר עבור שחקן מסויים
     * @param playerId מספר המזהה של השחקן שעבורו נמצא פרטנר
     * @return פרטנר השחקן
     */
    public Player getPartnerPlayer(String playerId)
    {
        if (playerId.equals(player1.getId()))
            return player2;
        else if (playerId.equals(player2.getId()))
            return player1;
        return null;
    }
    
    /**
     * פעולה המפסיקה את המשחק
     */
    public void stopGame()
    {
        stopGame = true;
    }

}
