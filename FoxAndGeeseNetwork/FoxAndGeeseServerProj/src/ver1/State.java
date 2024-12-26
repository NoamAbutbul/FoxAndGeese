package ver1;

import java.util.ArrayList;


/**
 *  State.
 *  By noamabutbul | 06/04/2022 10:28
 */
public class State
{
    //תכונות המחלקה 
    private static int ROWS;  // מספר השורות במטריצה
    private static int COLS;  // מספר העמודות במטריצה
    
    private char[][] logicBoard; // הלוח הלוגי
    private char currentPlayer; // אות המסלמלת את תור השחקן הנוכחי
    private Location foxLocation; // מיקום השועל
    private int freeLocationsForFox; //מספר שלם המייצג את מספר המיקומים האפשריים לשועל 
    private int freeLocationsForGeese; //מספר שלם המייצג את מספר המיקומים האפשריים לאווזים 
    private Location[] geesesLocations; //מערך ששומר את ממיקומי האווזים 

    /**
     * פעולה בונה למחלקה
     * @param logicBoard הלוח הלוגי
     * @param currentPlayer תור השחקן הנוכחי
     * @param foxLocation מיקום השועל בלוח
     * @param freeLocationsForFox מספר שלם המייצג את מספר המיקומים האפשריים לשועל
     * @param freeLocationsForGeese מספר שלם המייצג את מספר המיקומים האפשריים לאווזים
     * @param geesesLocations מערך מיקום האווזים
     */
    public State(char[][] logicBoard, char currentPlayer, Location foxLocation, int freeLocationsForFox, int freeLocationsForGeese, Location[] geesesLocations)
    {
        this.logicBoard = new char[ROWS][COLS];
        for (int row = 0; row < ROWS; row++)
        {
            for (int col = 0; col < COLS; col++)
            {
                this.logicBoard[row][col] = logicBoard[row][col];
            }
        }
        this.currentPlayer = currentPlayer;
        this.foxLocation = new Location(foxLocation);
        this.freeLocationsForFox = freeLocationsForFox;
        this.freeLocationsForGeese = freeLocationsForGeese;
        this.geesesLocations = new Location[Consts.NUM_OF_GEESES];
        for (int i = 0; i < this.geesesLocations.length; i++)
        {
            this.geesesLocations[i] = geesesLocations[i]; 
        }
        //System.out.println("i created. fox = " + freeLocationsForFox + " , geese =  " + freeLocationsForGeese);
        //System.out.println(this);
        View.log(this.toString());
    }

    /**
     * פעולה בונה דיפולטיבית היוצרת לוח במצב של משחק חדש
     */
    public State()
    {
        ROWS = Consts.DEFAULT_BOARD_SIZE;
        COLS = Consts.DEFAULT_BOARD_SIZE;
        
        freeLocationsForFox = 0;
        freeLocationsForGeese = 0;
        
        this.logicBoard = new char[ROWS][COLS];
        this.foxLocation = new Location();
        this.currentPlayer = Consts.GEESE_SIGN;
        this.geesesLocations = new Location[Consts.NUM_OF_GEESES];
        
        for (int row = 0; row < ROWS; row++)        
        {
            for (int col= 0; col < COLS; col++)            
            {
                logicBoard[row][col] = Consts.EMPTY_SIGN;
            }
        }
        
        for (int col = 1; col < COLS; col += 2)
        {
            logicBoard[0][col] = Consts.GEESE_SIGN;
        }
        
        for (int i = 0, j = 1; i < this.geesesLocations.length; i++, j += 2)
        {
            this.geesesLocations[i] = new Location(0, j);
        }
        
        System.out.println(this);

    }
    
    /**
     * פעולה בונה מעתיקה
     * @param other מצב להעתקה
     */
    public State(State other)
    {
        ROWS = Consts.DEFAULT_BOARD_SIZE;
        COLS = Consts.DEFAULT_BOARD_SIZE;
        
        this.logicBoard = new char[ROWS][COLS];
        
        for (int row = 0; row < ROWS; row++)
        {
            for (int col = 0; col < COLS; col++)
            {
                this.logicBoard[row][col] = other.getLogicBoard()[row][col];
            }
        }
        
        this.foxLocation = other.getFoxLocation();
        this.currentPlayer = other.getPlayer();
        
        this.freeLocationsForFox = other.getFreeLocationsForFox();
        this.freeLocationsForGeese = other.getFreeLocationsForGeese();
        
        for (int i = 0; i < this.geesesLocations.length; i++)
        {
            this.geesesLocations[i] = other.geesesLocations[i];
        }
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
        this.logicBoard = logicBoard;
    }

    /**
     * פעולה המחזירה תו המייצג תור מי לשחק
     * @return תו המייצג תור מי לשחק
     */
    public char getPlayer()
    {
        return currentPlayer;
    }

    /**
     * פעולה המעדכנת תור מי לשחק
     * @param currentPlayer תו המייצג תור מי לשחק
     */
    public void setPlayer(char currentPlayer)
    {
        this.currentPlayer = currentPlayer;
    }

    /**
     * פעולה המחזירה את מיקום השועל בלוח
     * @return מיקום השועל בלוח
     */
    public Location getFoxLocation()
    {
        return new Location(foxLocation);
    }

    /**
     * פעולה המעדכנת את מיקום השועל בלוח
     * @param foxLocation מיקום השועל בלוח
     */
    public void setFoxLocation(Location foxLocation)
    {
        this.foxLocation = foxLocation;
    }
    
    /**
     * פעולה המחזירה את מספר המיקומים האפשריים לתזוזת השועל
     * @return מספר המיקומים האפשריים לתזוזת השועל
     */
    public int getFreeLocationsForFox()
    {
        return freeLocationsForFox;
    }


    /**
     * פעולה המעדכנת את מספר המיקומים האפשריים לתזוזת השועל
     * @param freeLocationsForFox מספר המיקומים האפשריים לתזוזת השועל
     */
    public void setFreeLocationsForFox(int freeLocationsForFox)
    {
        this.freeLocationsForFox = freeLocationsForFox;
    }

    /**
     * פעולה המחזירה את מספר המיקומים האפשריים לתזוזת האווזים
     * @return מספר המיקומים האפשריים לתזוזת האווזים
     */
    public int getFreeLocationsForGeese()
    {
        return freeLocationsForGeese;
    }

    /**
     * פעולה המעדכנת את מספר המיקומים האפשריים לתזוזת האווזים
     * @param freeLocationsForGeese מספר המיקומים האפשריים לתזוזת האווזים
     */
    public void setFreeLocationsForGeese(int freeLocationsForGeese)
    {
        this.freeLocationsForGeese = freeLocationsForGeese;
    }
    
    
    /**
     * פעולה המחזירה את מערך מיקום האווזים
     * @return מערך מיקום האווזים
     */
    public Location[] getGeesesLocations()
    {
        return geesesLocations;
    }
    
    
    public void setGeesesLocations(Location[] geesesLocations)
    {
        for (int i = 0; i < this.geesesLocations.length; i++)
        {
            this.geesesLocations[i] = geesesLocations[i];
        }
    }
    
    
    /**
    * פעולה המקבלת מיקום שבחר השועל להתחיל את המשחק ומעדכנת את הלוח הלוגי בהתאם
    * @param loc מיקום השועל הנבחר
    */
    public void foxSelectedStartLocation(Location loc)
    {
        logicBoard[loc.getRow()][loc.getCol()] = Consts.FOX_SIGN;
        foxLocation = new Location(loc);
        //System.out.println("i created. fox = " + freeLocationsForFox + " , geese =  " + freeLocationsForGeese);
    }
    
    
    
    
    /** 
    * פעולה המקבלת מיקום יעד ומקור ותור השחקן ומעדכנת את הלוח הלוגי בהתאם
    * @param move המהלך שיש לבצע
    * @param playerSign סימן המורה על תור השחקן
    */
    public void makeMove(Move move, char playerSign)
    {
        if(playerSign == Consts.FOX_SIGN)
        {
            logicBoard[getFoxLocation().getRow()][getFoxLocation().getCol()] = ' ';
            logicBoard[move.getDestLocation().getRow()][move.getDestLocation().getCol()] = Consts.FOX_SIGN;
            setFoxLocation(move.getDestLocation());
        }
        else
        {
            for (int i = 0; i < geesesLocations.length; i++)
            {
                if ((geesesLocations[i].getRow() == move.getSourceLocation().getRow()) &&
                    (geesesLocations[i].getCol() == move.getSourceLocation().getCol()))
                    {
                        geesesLocations[i].setRow(move.getDestLocation().getRow());
                        geesesLocations[i].setCol(move.getDestLocation().getCol());
                    }
            }
            logicBoard[move.getSourceLocation().getRow()][move.getSourceLocation().getCol()] = ' ';
            logicBoard[move.getDestLocation().getRow()][move.getDestLocation().getCol()] = Consts.GEESE_SIGN;
        }
        
        //System.out.println(this);
        View.log(this.toString());

    }
    
    
    /**
     * פעולה המבצעת מהלך על הלוח הלוגי
     * @param move המהלך שיש לבצע
     * @param playerSign תו המייצג תור מי השחקן שמבצע את המהלך
     */
    public void applyMove(Move move, char playerSign) 
    {
        makeMove(new Move(move), playerSign);
    }
    
    /**
     * פעולה המבטלת מהלך מהלוח הלוגי
     * @param move המהלך שיש לבטל
     * @param playerSign תו המייצג תור מי שיחק את המהלך שיש לבטל
     */
    public void undoMove(Move move, char playerSign)
    {
        makeMove(new Move(move.getDestLocation(), move.getSourceLocation()), playerSign);
    }
    
    /**
     * פעולה המחזירה את תו היריב
     * @param playerSign תו השחקן הנוכחי
     * @return תו היריב
     */
    public char getOponent(char playerSign)
    {
        if(playerSign == Consts.FOX_SIGN)
            return Consts.GEESE_SIGN;
        return Consts.FOX_SIGN;
    }
  
    
    
    
    /**
    * פעולה המקבלת מיקום ומחזירה אם הוא חוקי כרגע
    * @param loc מיקום המתקבל
    * @return אמת למיקום חוקי ושקר למיקום לא חוקי
    */
    public boolean isLegal(Location loc)
    {
        if ((loc.getCol() - loc.getRow()) % 2 == 0)
            return false;
        
         if ((loc.getRow() >= ROWS) || (loc.getRow() < 0) || (loc.getCol() >= COLS) || (loc.getCol() < 0))
                return false;
         
        if (this.logicBoard[loc.getRow()][loc.getCol()] != Consts.EMPTY_SIGN)
            return false;
       
        return true;
    }
    
    public boolean isTrapped(Location location) {
    int row = location.getRow();
    int col = location.getCol();

    // Check if all adjacent locations are occupied by opponent pieces
    boolean upOccupied = isLegal(new Location(row - 1, col));
    boolean downOccupied = isLegal(new Location(row + 1, col));
    boolean leftOccupied = isLegal(new Location(row, col - 1));
    boolean rightOccupied = isLegal(new Location(row, col + 1));

    // Check if all adjacent locations are occupied
    boolean allAdjacentOccupied = upOccupied && downOccupied && leftOccupied && rightOccupied;

    return allAdjacentOccupied;
}
    
    
    
    
    /**
     * פונקציה המקבלת מיקום מקור ומחזירה רשימה של מהלכים אפשריים
     * @param sourceLocation מיקום המקור
     * @return רשימת מהלכים אפשריים לאותו המיקום
     */
    public  ArrayList<Move> getPossibelMoves(Location sourceLocation)
    {
        ArrayList<Move> possibleMoves = new ArrayList<Move>();
        Location tmpDestLoc = new Location();
        
        if (this.getLogicBoard()[sourceLocation.getRow()][sourceLocation.getCol()] == Consts.FOX_SIGN)
        {
            tmpDestLoc.setRow(this.foxLocation.getRow() + 1);
            tmpDestLoc.setCol(this.foxLocation.getCol() + 1);
            if (isLegal(tmpDestLoc))
                possibleMoves.add(new Move(sourceLocation, tmpDestLoc));
            
            tmpDestLoc.setRow(this.foxLocation.getRow() + 1);
            tmpDestLoc.setCol(this.foxLocation.getCol() - 1);
            if (isLegal(tmpDestLoc))
                possibleMoves.add(new Move(sourceLocation, tmpDestLoc));
            
            tmpDestLoc.setRow(this.foxLocation.getRow() - 1);
            tmpDestLoc.setCol(this.foxLocation.getCol() - 1);
            if (isLegal(tmpDestLoc))
                possibleMoves.add(new Move(sourceLocation, tmpDestLoc));
            
            tmpDestLoc.setRow(this.foxLocation.getRow() - 1);
            tmpDestLoc.setCol(this.foxLocation.getCol() + 1);
            if (isLegal(tmpDestLoc))
                possibleMoves.add(new Move(sourceLocation, tmpDestLoc));
        }
        
        else
        {
            tmpDestLoc.setRow(sourceLocation.getRow() + 1);
            tmpDestLoc.setCol(sourceLocation.getCol() - 1);
            if (isLegal(tmpDestLoc))
                possibleMoves.add(new Move(sourceLocation, tmpDestLoc));

            tmpDestLoc.setRow(sourceLocation.getRow() + 1);
            tmpDestLoc.setCol(sourceLocation.getCol() + 1);
            if (isLegal(tmpDestLoc))
                possibleMoves.add(new Move(sourceLocation, tmpDestLoc));
        }
        return possibleMoves;
    }
    
    
    /**
     * פעולה המחזירת את רשימת המיקומים האפשריים לשחקן מסויים
     * @param sourceLocation מיקום המקור
     * @return רשימת המיקומים האפשריים
     */
    public ArrayList<Location> getPossibleLocations(Location sourceLocation) 
    {
        ArrayList<Location> possibleLocations = new ArrayList<>();
        Location tmpDestLoc = new Location();

         if (this.getLogicBoard()[sourceLocation.getRow()][sourceLocation.getCol()] == Consts.FOX_SIGN)
        {
            tmpDestLoc.setRow(this.foxLocation.getRow() + 1);
            tmpDestLoc.setCol(this.foxLocation.getCol() + 1);
            if (isLegal(tmpDestLoc))
                possibleLocations.add(new Location(tmpDestLoc));
            
            tmpDestLoc.setRow(this.foxLocation.getRow() + 1);
            tmpDestLoc.setCol(this.foxLocation.getCol() - 1);
            if (isLegal(tmpDestLoc))
                possibleLocations.add(new Location(tmpDestLoc));
            
            tmpDestLoc.setRow(this.foxLocation.getRow() - 1);
            tmpDestLoc.setCol(this.foxLocation.getCol() - 1);
            if (isLegal(tmpDestLoc))
                possibleLocations.add(new Location(tmpDestLoc));
            
            tmpDestLoc.setRow(this.foxLocation.getRow() - 1);
            tmpDestLoc.setCol(this.foxLocation.getCol() + 1);
            if (isLegal(tmpDestLoc))
                possibleLocations.add(new Location(tmpDestLoc));
        }
         
        else
        {
            tmpDestLoc.setRow(sourceLocation.getRow() + 1);
            tmpDestLoc.setCol(sourceLocation.getCol() - 1);
            if (isLegal(tmpDestLoc))
                possibleLocations.add(new Location(tmpDestLoc));

            tmpDestLoc.setRow(sourceLocation.getRow() + 1);
            tmpDestLoc.setCol(sourceLocation.getCol() + 1);
            if (isLegal(tmpDestLoc))
                possibleLocations.add(new Location(tmpDestLoc));
        }
        return possibleLocations;
    }
    
    
     /**
    * פעולה המקבלת שחקן ומחזירה רשימה של מיקומים אפשריים חוקיים לזוז אליהם
    * @param currentPlayer תו המסמל שחקן
    * @return רשימת מיקומים חוקיים
    */
    public ArrayList<Move> getAllPossibleMoves(char currentPlayer)
    {
        ArrayList<Move> listPossibleMoves = new ArrayList<Move>();

        if (currentPlayer == Consts.FOX_SIGN)
        {
            listPossibleMoves = getPossibelMoves(this.getFoxLocation());
            this.setFreeLocationsForFox(listPossibleMoves.size());
        }
        else
        {
            for (int i = 0; i < this.getGeesesLocations().length; i++)
            {
                 listPossibleMoves.addAll(getPossibelMoves(this.getGeesesLocations()[i]));
            }
             
            this.setFreeLocationsForGeese(listPossibleMoves.size());
        }
  
        //System.out.println(listPossibleMoves);
        return listPossibleMoves;
    }
    
    
    /**
     * פעולה המחזירה את רשימת כל המיקומים האפשריים עבור שחקן מסויים
     * @param signPlayer סימן השחקן
     * @return רשימת כל המיקומים האפשריים עבור שחקן מסויים
     */
    public ArrayList<Location> getAllPossibleLocations(char signPlayer)
    {
        ArrayList<Location> allPossibleLocations = new ArrayList<>();
        if (signPlayer == Consts.FOX_SIGN)
        {
            allPossibleLocations = getPossibleLocations(foxLocation);
        }
        else if (signPlayer == Consts.GEESE_SIGN)
        {
            for (int i = 0; i < this.getGeesesLocations().length; i++)
            {
                 allPossibleLocations.addAll(getPossibleLocations(this.getGeesesLocations()[i]));
            }
        }
        
        return allPossibleLocations;
    }
    
    
     /**
    * פעולה הבודקת ניצחון
    * @return השחקן המנצח במידה וניצח
    */
    char checkStatus()
    {
        //בדיקת ניצחון השועל 
        
        for (int i = 1; i < COLS; i+=2)
        {
            if (this.logicBoard[0][i] == Consts.FOX_SIGN)
                return Consts.FOX_SIGN; 
            getAllPossibleMoves(Consts.GEESE_SIGN);
            if (this.freeLocationsForGeese == 0) //אם לאווזים אין מהלכים אפשריים
            return Consts.FOX_SIGN;
        }
      
        //בדיקת ניצחון האווזים 
       
        getAllPossibleMoves(Consts.FOX_SIGN);
        if(this.freeLocationsForFox == 0) //אם לשועל אין מהלכים אפשריים
            return Consts.GEESE_SIGN;
    
        return Consts.EMPTY_SIGN;
    }
    
    
    /**
     * פעולה הממירה את מערך מיקומי האווזים לרשימה
     * @return רשימת מיקומי האווזים
     */
    public ArrayList<Location> convertGeeseLocationsToArrayList()
    {
        ArrayList<Location> locations = new ArrayList<>();
        
        //getAllPossibleMoves(Consts.GEESE_SIGN);
        
        for (int i = 0; i < this.geesesLocations.length; i++)
        {
            locations.add(this.geesesLocations[i]);
        }
        
        return locations;
    }
    
    
    /**
     * פונקציה הבודקת מצב משחק סופי
     * @return אמת אם ערך טרמינלי ושקר אם לא
     */
    public boolean isTerminal()
    {
        return (checkStatus() == Consts.FOX_SIGN) || (checkStatus() == Consts.GEESE_SIGN);
    }
    
    
    
    
    /**
     * פעולה המחזירה מחרוזת שמתארת את המצב בלוח המשחק
     * @return מחרוזת המתארת את המצב בלוח המשחק
     */
    @Override
    public String toString()
    {
        
        String str="";
        
        for (int row= 0; row < ROWS; row++)        
        {
            for (int col= 0; col < COLS; col++)            
            {
                if (logicBoard[row][col] == Consts.EMPTY_SIGN)
                    str += " - ";
                else
                    str += logicBoard[row][col]+" ";
            }
            str += "\n";
        }
        
        str += "player: " + currentPlayer;
        
        str += "\nfox free locs = " + freeLocationsForFox + "\n geese free locs = " + freeLocationsForGeese;

        
        str += "\n\nGeeses Locations:\n";
        
        for (int i = 0; i < this.geesesLocations.length; i++)
        {
            str += "Geese num" + i + ":" + geesesLocations[i].toString() + "\n";
        }
                
        
        return str;
    }
}
