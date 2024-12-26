package ver1;

import java.util.ArrayList;

/**
 *  PlayerNet.
 *  By noamabutbul | 19/10/2022 15:03
 */
public class PlayerNet extends Player
{
    private AppSocket appSocket; // משתנה לחיבור השחקן לשרת
    
    /**
     * פעולה בונה לשחקן רשת
     * @param signPlayer סימן השחקן
     * @param appSocket משתנה לחיבור השחקן לשרת
     */
    public PlayerNet(char signPlayer, AppSocket appSocket)
    {
        super(signPlayer);
        this.appSocket = appSocket;
        
    }
    
    /**
     * פעולה המחזירה את האפסוקט
     * @return אפסוקט
     */
    public AppSocket getAppSocket()
    {
        return appSocket;
    }

    /**
     * פעולה המחזירה את מזהה השחקן
     * @return מזהה השחקן
     */
    public String getId()
    {
        return id;
    }
    
    /**
     * פעולה המעדכנת את מזהה השחקן
     * @param id מזהה השחקן
     */
    public void setId(String id)
    {
        this.id = id;
    }
    
    
    /**
     * פעולה לשליחת הודעה ללקוח
     * @param msg 
     */
    private void sendMessage(Message msg)
    {
        appSocket.writeMessage(msg);
    }
    
    /**
     * פעולה השולחת הודעה ללקוח להתחיל משחק
     * @param logicBoard הלוח הלוגי
     */
    @Override
    public void startNewGame(char[][] logicBoard)
    {
        Message msg = new Message(Message.Status.START_NEW_MESSAGE);
        msg.setLogicBoard(logicBoard);
        sendMessage(msg);
    }
    
    /**
     * פעולה השולחת ללקוח את הסימן שלו
     * @param signPlayer סימן השחקן
     */
    @Override
    public void takeYourSign(char signPlayer)
    {
        Message msg = new Message(Message.Status.SIGN_PLAYER);
        msg.setSignPlayer(signPlayer);
        sendMessage(msg);
    }
    
    /**
     * פעולה השולחת ללקוח להמתין לתורו
     */
    @Override
    public void waitForYourTurn()
    {
        Message msg = new Message(Message.Status.WAIT_TURN);
        sendMessage(msg);
    }
    
    /**
     * פעולה השולחת ללקוח הודעה לבחירת מיקום השועל
     */
    @Override
    public void chooseFirstLocation()
    {
        Message msg = new Message(Message.Status.CHOOSE_FIRST_LOCATION);
        sendMessage(msg);
    }

    /**
     * פעולה המאזינה לבחירת המיקום ההתחלתי של השועל
     * @return בחירת מיקום ההתחלתי של השועל
     */
    @Override
    public Location listenToFoxStartLocation()
    {
        Message msg = appSocket.readMessage();
        if (msg == null)
            return null;
        switch (msg.getSubject())
        {
            case FOX_START_LOCATION:
                Location foxStartedLocation = msg.getFoxStartLocation();
                return foxStartedLocation;
                
            default:
                return null;
        }
    }
    
    /**
     * פעולה השולחת ללקוח הודעה עם מיקום בחירת השועל
     * @param loc מיקום בחירת השועל
     */
    @Override
    public void foxStartedLocation(Location loc)
    {
        Message msg = new Message(Message.Status.FOX_START_LOCATION);
        msg.setFoxStartLocation(loc);
        
        System.out.println("\n\n\n\n\n\nI have got message " + loc + " = fox loc\n\n\n\n\n\n");
        sendMessage(msg);
    }
    
    
    /**
     * פעולה השולחת ללקוח הודעה לבצע תור
     * @param allPossibleLocations רשימת כל המהלכים האפשריים לעדכון התצוגה
     */
    @Override
    public void playTurn(ArrayList<Location> allPossibleLocations)
    {
        Message msg = new Message(Message.Status.PLAY_TURN);
        msg.setAllPossibleLocations(allPossibleLocations);
        sendMessage(msg);
    }

    
    /**
     * פעולה המבצעת תמרון ביצוע מהלך מול הלקוח
     * @param allPossibleLocations רשימת כל המיקומים האפשריים
     * @param currentState המצב הנוכחי בלוח
     * @return מהלך שביצע הלקוח
     */
    @Override
    public Move makeMove(ArrayList<Location> allPossibleLocations, State currentState)
    {
        Move move = null;
        
        Message msg = new Message(Message.Status.PLAY_TURN);
        msg.setAllPossibleLocations(allPossibleLocations);
        sendMessage(msg);
        
        if (sign == Consts.FOX_SIGN)
        {
            Location sourceLocation = currentState.getFoxLocation();
            Location destLocation = listenToClick();
            
            if (destLocation == null)
                return null;
            
            move = new Move(sourceLocation, destLocation);
            currentState.makeMove(move, sign);
            
            playerMoved(move, sign);
        }
        else if (sign == Consts.GEESE_SIGN)
        {
            playTurn(currentState.convertGeeseLocationsToArrayList());
            Location sourceLocation = listenToClick();
            
            if (sourceLocation == null)
                return null;
                        
            painBoard(currentState.getLogicBoard(), currentState.getPossibleLocations((sourceLocation)));
            playTurn(currentState.getPossibleLocations(sourceLocation));
            
            Location destLocation = listenToClick();
            
            if (destLocation == null)
                return null;
            
            move = new Move(sourceLocation, destLocation);
            currentState.makeMove(move, sign);
            
            playerMoved(move, sign);
        }
        
        return move;
    }

   
    /**
     * פעולה המאזינה ללחיצה מהלקוח
     * @return מיקום הלחיצה מהלקוח
     */
    @Override
    public Location listenToClick()
    {
        Message msg = appSocket.readMessage();
        
        if(msg == null)
            return null;
        
        switch (msg.getSubject())
        {
            case CLICK:
                Location loc = msg.getClickLocation();
                return loc;
                
            default:
                return null;
        }
    }

  
    
    /**
     * פעולה השולחת ללקוח הודעה לצביעת הלוח במיקומים מסויימים
     * @param logicBoard הלוח הלוגי
     * @param possibleLocations רשימת המיקומים שאותם יש לצבוע
     */
    @Override
    public void painBoard(char[][] logicBoard, ArrayList<Location> possibleLocations)
    {
        Message msg = new Message(Message.Status.PAIN_AFTER_FIRST_CLICK);
        msg.setLogicBoard(logicBoard);
        msg.setAllPossibleLocations(possibleLocations);
        sendMessage(msg);
    }

    /**
     * פעולה השולחת ללקוח הודעה עם המהלך שהיריב ביצע
     * @param move מהלך היריב
     * @param signPlayer סימן השחקן המבצע
     */
    @Override
    public void playerMoved(Move move,  char signPlayer)
    {
        Message msg = new Message(Message.Status.PLAYER_MOVED);
        msg.setSignPlayer(signPlayer);
        msg.setSourceLocation(move.getSourceLocation());
        msg.setDestLocation(move.getDestLocation());
        sendMessage(msg);
    }
    
    /**
     * פעולה ששולחת הודעה ללקוח שהמשחק נגמר
     * @param signPlayer סימן השחקן המנצח
     */
    @Override
    public void gameOver(char signPlayer)
    {
        Message msg = new Message(Message.Status.GAME_OVER);
        msg.setSignPlayer(signPlayer);
        sendMessage(msg);
    }
        
   /**
    * פעולה השולחת הודעת ללקוח לחכות לפרטנר לתחילת המשחק
    */
    public void waitForPartner()
    {
         Message msg = new Message(Message.Status.WAIT_FOR_PARTNER);
         sendMessage(msg);
    }

    /**
     * פעולה המאזינה לסיום ספירת הלקוח לאחור כשמשחק נגמר
     * @return אמת במידה וההודעה התקבלה אחרת שקר
     */
    @Override
    public boolean listenToFinishCounddown()
    {
        Message msg = appSocket.readMessage();
        
        if (msg == null)
            return false;
        
        switch (msg.getSubject())
        {
            case COUNTDOWN_FINISHED:
                return true;
            default:
                
        }
        return false;
    }
  
    /**
     * פעולה לסגירת האפסוקט של הלקוח
     */
    @Override
    public void closeYourself()
    {
        if (appSocket != null)
            appSocket.Close();
    }

    /**
     * פעולה השולחת הודעה ללקוח לבחור מול מי הוא רוצה לשחק
     * ומאזינה לבחירת הלקוח מול מי לשחק ומחזירה את בחירתו
     * @return בחירת הלקוח 
     * 0 לשחקן ממוחשב
     * 1 לשחקן רשת
     */
    public int chooseOpponentProcess()
    {
        Message writeMsg = new Message(Message.Status.CHOOSE_OPPONNENT);
        sendMessage(writeMsg);
        
        Message readMsg = appSocket.readMessage();
        
        if (readMsg == null)
            return Server.BREAKE;
        
        return readMsg.getTypeOfPlayer();
    }
    


    /**
     * פעולה המתארת שחקן רשת
     * @return תיאור שחקן הרשת
     */
    @Override
    public String toString()
    {
        return "I am playerNet with id = " + this.id;
    }

    /**
     * פעולה השולחת הודעת ניצחון טכני ללקוח
     */
    @Override
    public void technicalVictory()
    {
        Message cmd = new Message(Message.Status.TECHNICAL_VICTORY);
        appSocket.writeCmd(cmd);
    }

    @Override
    public void serverClosed()
    {
        Message cmd = new Message(Message.Status.SERVER_CLOSED);
        appSocket.writeCmd(cmd);
    }
}
