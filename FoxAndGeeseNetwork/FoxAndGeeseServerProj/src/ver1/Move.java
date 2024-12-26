package ver1;

import java.io.Serializable;

/**
 *  Move.
 *  By noamabutbul | 11/04/2022 19:46
 */
public class Move implements Serializable
{
    //תכונות 
    private Location sourceLocation;
    private Location destLocation;

    /**
     * פעולה בונה המקבלת שני מיקומים
     * @param sourceLocation מיקום מקור
     * @param destLocation מיקום יעד
     */
    public Move(Location sourceLocation, Location destLocation)
    {
        this.sourceLocation = new Location(sourceLocation);
        this.destLocation = new Location(destLocation);    
    }

    /**
     * Default Constructor
     * שמה בערכים ערך דיפולטיבי
     */
    public Move()
    {
        this.sourceLocation = new Location();
        this.destLocation = new Location();
    }
    
    /**
     * Copy Constractor
     * פעולה בונה מעתיקה
     * @param other 
     */
    public Move(Move other)
    {
        this.sourceLocation = new Location(other.sourceLocation);
        this.destLocation = new Location(other.destLocation);
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
     * @return 
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
     * פעולה המחזירה מחרוזת המתארת את המהלך
     * @return מחרוזת המתארת את המהלך
     */
    @Override
    public String toString()
    {
        return "Move{" + "sourceLocation=" + sourceLocation + ", destLocation=" + destLocation + '}';
    }
    
}
