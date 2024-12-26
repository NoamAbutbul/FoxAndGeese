package ver1;

/**
 *  MoveMinimax.
 *  By noamabutbul | 09/05/2022 13:03
 */
public class MoveMinimax extends Move //מחלקה היורשת מהמחלקה מהלך לצורך אלגוריתם המינימקס
{
    //תכונות המחלקה 
    private double score; // ניקוד המהלך
    private int depth; // עומק המהלך בעץ

    /**
     * פעולה בונה למחלקה
     * @param score ציון המהלך
     * @param depth עומק המהלך בעץ המשחק
     * @param sourceLocation מיקום מקור
     * @param destLocation מיקום יעד
     */
    public MoveMinimax(double score, int depth, Location sourceLocation, Location destLocation)
    {
        super(sourceLocation, destLocation);
        this.score = score;
        this.depth = depth;
    }
    
    /**
     * פעולה בונה מעתיקה
     * @param other מהלך להעתקה
     */
    public MoveMinimax(MoveMinimax other)
    {
        this.score = other.score;
        this.depth = other.depth;
    }
    
    /**
     * פעולה בונה למהלך מינימקס
     * @param score ניקוד המהלך
     */
    public MoveMinimax(double score)
    {
        this.score = score;
    }
    
    /**
     * פעולה בונה עם ניקוד למהלך ועומק
     * @param score ניקוד המהלך
     * @param depth עומק בעץ המשחק
     */
    public MoveMinimax(double score, int depth)
    {
        this.score = score;
        this.depth = depth;
    }
    
    /**
     * פעולה בונה עם ניקוד עומק ומהלך
     * @param score ניקוד המהלך
     * @param depth עומק בעץ המשחק
     * @param move מהלך
     */
    public MoveMinimax(double score, int depth, Move move)
    {
        super(move);
        this.score = score;
        this.depth = depth;
    }

    /**
     * פעולה המחזירה את ציון המהלך
     * @return ציון המהלך
     */
    public double getScore()
    {
        return score;
    }

    /**
     * פעולה המעדכנת את ציון המהלך
     * @param score ציון המהלך
     */
    public void setScore(double score)
    {
        this.score = score;
    }

    /**
     * פעולה המחזירה את עומק המהלך בעץ המשחק
     * @return עומק המהלך בעץ המשחק
     */
    public int getDepth()
    {
        return depth;
    }

    /**
     * פעולה המעדכנת את עומק המהלך בעץ המשחק
     * @param depth עומק המהלך בעץ המשחק
     */
    public void setDepth(int depth)
    {
        this.depth = depth;
    }
    
    /**
     * פעולה המחזירה את המהלך
     * @return המהלך
     */
    public Move getMove()
    {
        return new Move(this.getSourceLocation(), this.getDestLocation());
    }
    
    /**
     * פעולה המעדכנת את המהלך
     * @param move המהלך
     */
    public void setMove(Move move)
    {
        setSourceLocation(move.getSourceLocation());
        setDestLocation(move.getDestLocation());
    }

    /**
     * פעולה המחזירה מחרוזת לתיאור מהלך המינימקס
     * @return מחרוזת המתארת את מהלך המינימקס
     */
    @Override
    public String toString()
    {
        return "MoveMinimax{" + "Move = " + super.toString() + "score = " + score + ", depth = " + depth + '}';
    }
}
