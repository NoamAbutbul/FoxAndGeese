package ver1;

import java.util.ArrayList;

/**
 *  Player.
 *  By noamabutbul | 19/10/2022 14:53
 */
public abstract class Player
{
    protected char sign; // סימן השחקן
    protected String id; // מזהה השחקן

    
    /**
     * פעוה בונה למחלקה שחקן
     * @param signPlayer סימן החשקן
     */
    public Player(char signPlayer)
    {
        this.sign = signPlayer;
    }
    
    /**
     * פעולה המחזירה את סימן השחקן
     * @return סימן השחקן
     */
    public char getSign()
    {
        return sign;
    }
    
    /**
     * פעולה המעדכנת את סימן השחקן
     * @param sign סימן השחקן
     */
    public void setSign(char sign)
    {
        this.sign = sign;
    }
    
    /**
     * פעולה המחזירה את מזהה השחקן
     * @return מזהה השחקן
     */
    public String getId()
    {
        return id;
    }
           

    // פעולות אבסטרקטיות לניהול מהלך המשחק
    public abstract void takeYourSign(char signPlayer);
    public abstract void startNewGame(char[][] logicBoard);
    public abstract void chooseFirstLocation();
    public abstract void waitForYourTurn();
    public abstract Location listenToFoxStartLocation();
    public abstract void foxStartedLocation(Location loc);
    public abstract void playTurn(ArrayList<Location> allPossibleLocations);
    public abstract Move makeMove(ArrayList<Location> allPossibleLocations, State currentState);

    public abstract Location listenToClick();
    public abstract void painBoard(char[][] logicBoard, ArrayList<Location> possibleLocations);

    public abstract void playerMoved(Move move, char signPlayer);
    public abstract void gameOver(char signPlayer);
            
    public abstract boolean listenToFinishCounddown();
    
    public abstract void closeYourself();
    
    public abstract void technicalVictory();
    public abstract void serverClosed();


    
    /**
     * פעולה המדפיסה את מזהה השחקן
     * @return 
     */
    @Override
    public String toString()
    {
        return id;
    }    
}
