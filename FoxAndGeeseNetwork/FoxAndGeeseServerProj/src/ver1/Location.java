package ver1;

import java.io.Serializable;

/**
 *  Location.
 *  By noamabutbul | 31/01/2022 11:34
 */
public class Location implements Serializable
{
    public static final int DEFAULT_LOCATION = -1; //קבוע דיפולטיבי 

    // תכונות הם משתנים שמגדירים איפה ישמרו הנתונים של הטיפוס
    private int row;    // מספר השורה
    private int col;    // מספר העמודה
    
    
    // Constructor פעולה בונה
    public Location(int row, int col)
    {
        this.row = row;
        this.col = col;
    }
    
    
    /**
     * Default Constructor
     * שמה בערכים ערך דיפולטיבי
     */
    public Location()
    {
        this.row = DEFAULT_LOCATION;
        this.col = DEFAULT_LOCATION;
    }
    
    
    /**
     * Copy Constractor פעולה בונה מעתיקה
     * @param other מיקום להעתקה
     */
    public Location(Location other)
    {
        this.row = other.row;
        this.col = other.col;
    }
    
    
    /**
     * פעולה המחזירה את ערך השורה
     * @return ערך השורה
     */
    public int getRow()
    {
        return row;
    }
    
    
    /**
     * פעולה המחזירה את ערך העמודה של המיקום
     * @return ערך העמודה
     */
    public int getCol()
    {
        return col;
    }

    
    /**
     * פעולה מעדכנת את ערך העמודה במיקום
     * @param col ערך העמודה
     */
    public void setCol(int col)
    {
	this.col = col;
    }  

    
    /**
     * פעולה מעדכנת את ערך השורה במיקום
     * @param row ערך השורה
     */
    public void setRow(int row)
    {
	this.row = row;
    }  
    
    /**
     * פעולה סטטית המחזירה את ערך המרחק בין שני מיקומים על פי הצירים
     * @param loc1 מיקום ראשון
     * @param loc2 מיקום שני
     * @return ערך המרחק בין המיקומים
     */
    public static double distance(Location loc1, Location loc2)
    {
        // Axis X is cols
        // Axis Y is rows
        
        double distance = Math.sqrt((Math.pow(loc1.getCol() - loc2.getCol(), 2)) + Math.pow(loc1.getRow() - loc2.getRow(), 2));
        return distance;
    }
    
    /**
     * פעולה סטטית הבודקת ביחס ללוח אם המיקום הראשון מעל למיקום השני או באותה השורה שלו
     * @param loc1 מיקום ראשון
     * @param loc2 מיקום שני
     * @return אמת למיקום ראשון באותה השורה או גבוה מהמיקום השני ושקר להפך
     */
    public static boolean AboveEqual(Location loc1, Location loc2)
    {
        if (loc1.row <= loc2.row)
            return true;
        return false;
    }
    
//    public static boolean isAdjacent(Location location1, Location location2) {
//    int rowDiff = Math.abs(location1.getRow() - location2.getRow());
//    int colDiff = Math.abs(location1.getCol() - location2.getCol());
//
//    // Two locations are adjacent if the row and column differences are both <= 1
//    return (rowDiff <= 1 && colDiff <= 1 && rowDiff + colDiff > 0);
//}

    /**
     * פעולה המחזירה מחרוזת המתארת את המיקום
     * @return מחרוזת המתארת את המיקום
     */
    @Override
    public String toString()
    {
        return "("+row+","+col+")";
    }
}
