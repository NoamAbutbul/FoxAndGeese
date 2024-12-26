package ver1;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *  Message.
 *  By noamabutbul | 19/10/2022 11:09
 */


public class Message implements Serializable
{
    enum Status // הגדרת מצבים להודעות ולפקודות
    {
        CHOOSE_OPPONNENT,
        WAIT_FOR_PARTNER,
        SIGN_PLAYER,
        START_NEW_MESSAGE,
        WAIT_TURN,
        CHOOSE_FIRST_LOCATION,
        FOX_START_LOCATION,
        PLAY_TURN,
        MAKE_MOVE,
        FIRST_CLICK,
        SECOND_CLICK,
        CLICK,
        PAIN_AFTER_FIRST_CLICK,
        PLAYER_MOVED,
        GAME_OVER,
        COUNTDOWN_FINISHED,
        EXIT_MESSAGE,
        TECHNICAL_VICTORY,
        SERVER_CLOSED
    }
    
    // Subject
    private Status subject; // סטטוס להגדרת כותרת ההודעה
    
    
    // Data
    private int typeOfPlayer; // שחקן ממוחש או שחקן רשת
    
    private char signPlayer; // סימן השחקן
    private char[][] logicBoard; // לוח לוגי
    private Location foxStartLocation; // מיקום התחלת השועל
    private ArrayList<Location> allPossibleLocations; // כל המהלכים האפשריים

    private Location clickLocation; // מיקום לחיצה
    private Location sourceLocation; // מיקום מקור
    private Location destLocation; // מיקום יעד
    
    /**
     * פעולה בונה להודעה
     * @param subject כותרת ההודעה
     */
    public Message(Status subject)
    {
        this.subject = subject;        
    }

    /**
     * פעולה המחזירה את סוג בחירת השחקן
     * @return 
     */
    public int getTypeOfPlayer()
    {
        return typeOfPlayer;
    }

    /**
     * פעולה המעדכנת את סוג בחירת השחקן
     * @param typeOfPlayer סוג בחירת השחקן
     */
    public void setTypeOfPlayer(int typeOfPlayer)
    {
        System.out.println("type param: " + typeOfPlayer);
        this.typeOfPlayer = typeOfPlayer;
        System.out.println("type setted: " + this.typeOfPlayer);
    }

    
    /**
     * פעולה המחזירה את כותרת ההודעה
     * @return כותרת ההודעה
     */
    public Status getSubject()
    {
        return subject;
    }
    
    /**
     * פעולה המחזירה את סימן השחקן
     * @return סימן השחקן
     */
    public char getSignPlayer()
    {
        return signPlayer;
    }
    
    /**
     * פעולה המעדכנת את סימן השחקן
     * @param signPlayer סימן השחקן
     */
    public void setSignPlayer(char signPlayer)
    {
        this.signPlayer = signPlayer;
    }

    
    /**
     * פעולה המחזירה את הלוח הלוגי
     * @return הלוח הלוגי
     */
    public char[][] getLogicBoard()
    {
        return logicBoard;
    }

    /**
     * פעולה המעדכנת את הלוח הלוגי
     * @param logicBoard הלוח הלוגי
     */
    public void setLogicBoard(char[][] logicBoard)
    {
        this.logicBoard = new char[logicBoard.length][logicBoard.length];
        
        for (int row = 0; row < logicBoard.length; row++)
        {
            for (int col = 0; col < logicBoard.length; col++)
            {
                this.logicBoard[row][col] = logicBoard[row][col];
            }
        }
    }

    /**
     * פעולה המחזירה את מיקום בחירת השועל
     * @return מיקום בחירת השועל
     */
    public Location getFoxStartLocation()
    {
        return foxStartLocation;
    }

    /**
     * פעולה המעדכנת את מיקום בחירת השועל
     * @param foxStartLocation מיקום בחירת השועל
     */
    public void setFoxStartLocation(Location foxStartLocation)
    {
        this.foxStartLocation = new Location(foxStartLocation);
    }

    /**
     * פעולה המחזירה את מיקום הלחיצה בלוח
     * @return מיקום הלחיצה בלוח
     */
    public Location getClickLocation()
    {
        return clickLocation;
    }

    /**
     * פעולה המעדכנת את מיקום הלחיצה בלוח
     * @param clickLocation מיקום הלחיצה בלוח
     */
    public void setClickLocation(Location clickLocation)
    {
        this.clickLocation = new Location(clickLocation);
    }
    
    /**
     * פעולה המחזירה את מיקום המקור
     * @return מיקום המקור
     */
    public Location getSourceLocation()
    {
        return sourceLocation;
    }

    /**
     * פעולה המעדכנת את מיקום המקור
     * @param sourceLocation מיקום המקור
     */
    public void setSourceLocation(Location sourceLocation)
    {
        this.sourceLocation = new Location(sourceLocation);
    }

    /**
     * פעולה המחזירה את מיקום היעד
     * @return מיקום היעד
     */
    public Location getDestLocation()
    {
        return destLocation;
    }

    /**
     * פעולה המעדכנת את מיקום היעד
     * @param destLocation מיקום היעד
     */
    public void setDestLocation(Location destLocation)
    {
        this.destLocation = new Location(destLocation);
    }

    /**
     * פעולה המחזירה את רשימת המהלכים האפשריים
     * @return רשימת המהלכים האפשריים
     */
    public ArrayList<Location> getAllPossibleLocations()
    {
        return allPossibleLocations;
    }

    /**
     * פעולה המעדכנת את רשימת המהלכים האפשריים
     * @param allPossibleLocations רשימת המהלכים האפשריים
     */
    public void setAllPossibleLocations(ArrayList<Location> allPossibleLocations)
    {
        this.allPossibleLocations = new ArrayList<>();
        for (int i = 0; i < allPossibleLocations.size(); i++)
        {
            this.allPossibleLocations.add(new Location(allPossibleLocations.get(i)));
        }
    }
    
    
    
    /**
     * פעולה המתארת את ההודעה
     * @return תיאור ההודעה
     */
    @Override
    public String toString()
    {
        String str = "Message{" + "subject=" + subject.name() + '}';
        str += "\n";
        
        return str;
    }
}
