package ver1;

import java.util.ArrayList;

/**
 *  PlayerAI.
 *  By noamabutbul | 19/10/2022 15:14
 */
public class PlayerAI extends Player // לשנות אותו ללא אבסטרקטי כשעובדים עליו
{
    private static final long MAX_TIME_FOR_MINIMAX = 7 * 1000; // זמן להרצת מינימקס
    
    public static final double EVAL_WIN_SCORE = 100;  // הערכת ניצחון
    public static final double EVAL_LOSS_SCORE = -100;// הערכת הפסד
    public static final long DEFAULT_MAX_TIME_FOR_MINIMAX_IN_MILS = 7 * 1000; // זמן להרצת מינימקס במילישניות
    public static final int DEFAULT_MAX_TIME_FOR_MINIMAX_IN_SECOND = 7; // זמן להרצת מינימקס בשניות

    /**
     * פעולה בונה לשחקן ממוחשב
     * @param signPlayer סימן השחקן שהמחשב ישמש
     */
    public PlayerAI(char signPlayer)
    {
        super(signPlayer);
    }

    
    /**
     * פעולה איטרטיבית לזימון אלגוריתם מינימקס
     * @param allPossibleLocations כל המהלכים האפשריים לשחקן
     * @param currentState הלוח הנוכחי
     * @return המהלך הטוב ביותר
     */
    @Override
    public Move makeMove(ArrayList<Location> allPossibleLocations, State currentState)
    {        
        MoveMinimax bestMove = null;
        int depth = 1;
        long timeLeft = MAX_TIME_FOR_MINIMAX;
        long totalTime = 0;
        
        while (timeLeft > 2 * totalTime)
        {
            long t1 = System.currentTimeMillis();
            bestMove = minimax(currentState, true, 0, depth, currentState.getPlayer(), Integer.MIN_VALUE, Integer.MAX_VALUE);
            long t2 = System.currentTimeMillis();
            totalTime = t2 - t1;
            timeLeft -= totalTime;
            depth++;
        }

        currentState.makeMove(bestMove, currentState.getPlayer());
        
        System.out.println("move depth = " + bestMove.getDepth());

                
        return bestMove;
    }
    

    /**
     * אלגוריתם מינימקס להפעלת משחק ממוחשב
     * @param currentState המצב הנוכחי שממנו מתחיל האלגוריתם
     * @param isMax משתנה בוליאני שאומר אם השחקן הוא שחקן מקסימום או מינימום
     * @param depth עומק המהלך שנמשר במהלך האלגוריתם
     * @param maxDepth העומק המקסימלי להרצה
     * @param player תו שמסמל את השחקן
     * @param alpha ערך אלפא לגיזום
     * @param beta ערך בטא לגיזום
     * @return מחזיר את המהלך הטוב ביותר לשחקן מקסימום
     */
    private MoveMinimax minimax(State currentState, boolean isMax, int depth, int maxDepth, char player, double alpha, double beta)
    {
        MoveMinimax bestMove = new MoveMinimax(0);

        if (currentState.isTerminal() || depth == maxDepth)
            return new MoveMinimax(eval(currentState), depth);
        
 
        ArrayList<Move> possibleMoves = currentState.getAllPossibleMoves(player);
        
        if (isMax)
        { 
            bestMove.setScore(Integer.MIN_VALUE);
            
            for (int i = 0; i < possibleMoves.size(); i++)
            {
                currentState.applyMove(possibleMoves.get(i), player);
                MoveMinimax checkMove = minimax(currentState, false, depth + 1, maxDepth, currentState.getOponent(player), alpha, beta);
                alpha = Math.max(alpha, checkMove.getScore());
                
               
                if (depth == 0)
                {
                    if (checkMove.getScore() > bestMove.getScore())
                    {                        
                        bestMove.setMove(possibleMoves.get(i));
                        bestMove.setScore(checkMove.getScore());
                        bestMove.setDepth(checkMove.getDepth());
                    }
                    if (checkMove.getScore() == bestMove.getScore())
                        if (checkMove.getDepth() < bestMove.getDepth())
                        {
                            bestMove.setMove(possibleMoves.get(i));
                            bestMove.setScore(checkMove.getScore());
                            bestMove.setDepth(checkMove.getDepth());
                        }
                    
                }
                else if (checkMove.getScore() > bestMove.getScore())
                    bestMove = checkMove;
                else
                    if (checkMove.getScore() == bestMove.getScore())
                        if (checkMove.getDepth() < bestMove.getDepth())
                            bestMove = checkMove;  
                         
                currentState.undoMove(possibleMoves.get(i), player);
                
                if (alpha > beta)
                    break;

            }
        }
        else
        {   
            bestMove.setScore(Integer.MAX_VALUE);
            
            for (int i = 0; i < possibleMoves.size(); i++)
            {
                currentState.applyMove(possibleMoves.get(i), player);
                MoveMinimax checkMove = minimax(currentState, true, depth + 1, maxDepth, currentState.getOponent(player), alpha, beta);
                beta = Math.min(beta, checkMove.getScore());
                
                 if(checkMove.getScore() < bestMove.getScore())
                    bestMove = checkMove;
                 else
                    if(checkMove.getScore() == bestMove.getScore())
                        if(checkMove.getDepth() < bestMove.getDepth())
                        {
                            bestMove = checkMove;
                        }
                 
                 currentState.undoMove(possibleMoves.get(i), player);

            }
        }

        return bestMove;
    }
    
    /**
     * פעולת עזר למינימקס אשר מעריכה לוח שהוא לא מצב טרמינאלי
     * @param currentState
     * @return הערכת הלוח בין מינוס מאה למאה
     */
    public double eval(State currentState)
    {
        final int EVAL_FACTOR_FOR_FOX = 20;
        final double EVAL_FACTOR_BOARD = EVAL_WIN_SCORE / Consts.DEFAULT_BOARD_SIZE;                   
        
        double evalScore = 0;
        
        if (currentState.checkStatus() == currentState.getPlayer())
            return EVAL_WIN_SCORE;     // Game Over - Win for playerSign

        if (currentState.checkStatus() == currentState.getOponent(currentState.getPlayer()))
            return EVAL_LOSS_SCORE;    // Game Over - Loss for playerSign  
        
        if (currentState.getPlayer() == Consts.FOX_SIGN)
        {
            double rowScore = 0;
            double freeLocScore = 0;
            
            boolean checkIfFoxAboveGeese = false;
            
            for (int i = 0; i < currentState.getGeesesLocations().length; i++)
            {
                checkIfFoxAboveGeese = Location.AboveEqual(currentState.getFoxLocation(), currentState.getGeesesLocations()[i]);
            }
          
            
            if (!checkIfFoxAboveGeese)
            {
                rowScore = (EVAL_WIN_SCORE - ((currentState.getFoxLocation().getRow() + 1) * EVAL_FACTOR_BOARD)) * 0.9;
                freeLocScore = (currentState.getFreeLocationsForFox() * EVAL_FACTOR_FOR_FOX) * 0.1;
                evalScore = rowScore + freeLocScore;
            }
            else
                 evalScore = (EVAL_WIN_SCORE - ((currentState.getFoxLocation().getRow() + 1) * EVAL_FACTOR_BOARD));
        
        }
        
        else
        {
            double dictanceAvg = 0;
            double distanceScore = 0;
            
            double freeLocScore = (EVAL_WIN_SCORE / currentState.getFreeLocationsForGeese()) * 0.5; 
       
            
            for (int i = 0; i < currentState.getGeesesLocations().length; i++)
            {
               dictanceAvg += Location.distance(currentState.getGeesesLocations()[i], currentState.getFoxLocation());
            }
          
            
            dictanceAvg /= Consts.NUM_OF_GEESES;
            distanceScore = (EVAL_WIN_SCORE - dictanceAvg) * 0.5;
            
            evalScore = distanceScore + freeLocScore;
            
        }
       
        if (evalScore > EVAL_WIN_SCORE || evalScore < EVAL_LOSS_SCORE)
        {
            System.out.println("Eval Error!");
            System.out.println("evalValue = " + evalScore);
            System.exit(0);
        }
        
        return evalScore; 
    }
    
    
    
//    public double eval(State currentState) {
//    final double WIN_SCORE = 100.0;
//    final double LOSS_SCORE = -100.0;
//    final double BOARD_SIZE_FACTOR = WIN_SCORE / Consts.DEFAULT_BOARD_SIZE;
//    final double DISTANCE_FACTOR = WIN_SCORE / (Consts.NUM_OF_GEESES * Consts.DEFAULT_BOARD_SIZE);
//
//    if (currentState.checkStatus() == currentState.getPlayer())
//        return WIN_SCORE; // Game Over - Win for playerSign
//
//    if (currentState.checkStatus() == currentState.getOponent(currentState.getPlayer()))
//        return LOSS_SCORE; // Game Over - Loss for playerSign
//
//    if (currentState.getPlayer() == Consts.FOX_SIGN) {
//        int foxRow = currentState.getFoxLocation().getRow();
//        int geeseCount = currentState.getGeesesLocations().length;
//        int freeLocCount = currentState.getFreeLocationsForFox();
//        boolean geeseAboveFox = false;
//
//        for (Location geeseLocation : currentState.getGeesesLocations()) {
//            if (Location.AboveEqual(currentState.getFoxLocation(), geeseLocation)) {
//                geeseAboveFox = true;
//                break;
//            }
//        }
//
//        if (geeseAboveFox) {
//            return WIN_SCORE - (foxRow + 1) * BOARD_SIZE_FACTOR;
//        } else {
//            double rowScore = (WIN_SCORE - (foxRow + 1) * BOARD_SIZE_FACTOR) * 0.9;
//            double freeLocScore = freeLocCount * 10.0;
//            return rowScore + freeLocScore;
//        }
//    } else {
//        double distanceAvg = 0.0;
//        int freeLocCount = currentState.getFreeLocationsForGeese();
//
//        for (Location geeseLocation : currentState.getGeesesLocations()) {
//            distanceAvg += Location.distance(geeseLocation, currentState.getFoxLocation());
//        }
//
//        distanceAvg /= Consts.NUM_OF_GEESES;
//        double distanceScore = (WIN_SCORE - distanceAvg * DISTANCE_FACTOR) * 0.8;
//        double freeLocScore = freeLocCount * 10.0;
//        return distanceScore + freeLocScore;
//    }
//}
    
    
//    public double eval(State currentState) {
//    final int EVAL_FACTOR_FOR_FOX = 20;
//    final double EVAL_FACTOR_BOARD = EVAL_WIN_SCORE / (double) Consts.DEFAULT_BOARD_SIZE;
//    final double EVAL_FACTOR_GEESE_COUNT = EVAL_WIN_SCORE / (double) Consts.NUM_OF_GEESES;
//
//    double evalScore = 0;
//
//    if (currentState.checkStatus() == currentState.getPlayer())
//        return EVAL_WIN_SCORE;     // Game Over - Win for playerSign
//
//    if (currentState.checkStatus() == currentState.getOponent(currentState.getPlayer()))
//        return EVAL_LOSS_SCORE;    // Game Over - Loss for playerSign
//
//    if (currentState.getPlayer() == Consts.FOX_SIGN) {
//        double rowScore = 0;
//        double freeLocScore = 0;
//
//        boolean checkIfFoxAboveGeese = false;
//
//        for (int i = 0; i < currentState.getGeesesLocations().length; i++) {
//            checkIfFoxAboveGeese = Location.AboveEqual(currentState.getFoxLocation(), currentState.getGeesesLocations()[i]);
//        }
//
//        if (!checkIfFoxAboveGeese) {
//            rowScore = (EVAL_WIN_SCORE - ((currentState.getFoxLocation().getRow() + 1) * EVAL_FACTOR_BOARD)) * 0.9;
//            freeLocScore = (currentState.getFreeLocationsForFox() * EVAL_FACTOR_FOR_FOX) * 0.1;
//            evalScore = rowScore + freeLocScore;
//        } else {
//            evalScore = (EVAL_WIN_SCORE - ((currentState.getFoxLocation().getRow() + 1) * EVAL_FACTOR_BOARD));
//        }
//    } else {
//        double distanceAvg = 0;
//        double distanceScore = 0;
//        double geeseCountScore = 0;
//
//        double freeLocScore = (EVAL_FACTOR_GEESE_COUNT / currentState.getFreeLocationsForGeese()) * 0.5;
//
//        for (int i = 0; i < currentState.getGeesesLocations().length; i++) {
//            distanceAvg += Location.distance(currentState.getGeesesLocations()[i], currentState.getFoxLocation());
//        }
//
//        distanceAvg /= Consts.NUM_OF_GEESES;
//        distanceScore = (EVAL_WIN_SCORE - distanceAvg) * 0.4;
//
//        geeseCountScore = (currentState.getGeesesLocations().length * EVAL_FACTOR_GEESE_COUNT) * 0.1;
//
//        evalScore = distanceScore + freeLocScore + geeseCountScore;
//    }
//
//    if (evalScore > EVAL_WIN_SCORE || evalScore < EVAL_LOSS_SCORE) {
//        System.out.println("Eval Error!");
//        System.out.println("evalValue = " + evalScore);
//        System.exit(0);
//    }
//
//    return evalScore;
//}
    
    
//    public double eval(State currentState) {
//    final double WIN_SCORE = 100;
//    final double LOSS_SCORE = -100;
//    final double CENTER_CONTROL_SCORE = 5;
//    final double MOBILITY_SCORE = 1;
//    final double POSITION_SCORE = 2;
//    final double THREAT_SCORE = 10;
//
//    double evalScore = 0;
//
//    if (currentState.checkStatus() == currentState.getPlayer()) {
//        return WIN_SCORE; // Game Over - Win for player
//    }
//    if (currentState.checkStatus() == currentState.getOponent(currentState.getPlayer())) {
//        return LOSS_SCORE; // Game Over - Loss for player
//    }
//
//    // Calculate piece mobility
//    int playerMobility = currentState.getAllPossibleMoves(currentState.getPlayer()).size();
//    int opponentMobility = currentState.getAllPossibleMoves(currentState.getOponent(currentState.getPlayer())).size();
//    evalScore += MOBILITY_SCORE * (playerMobility - opponentMobility);
//
//    // Evaluate piece positioning
//    for (Location gooseLocation : currentState.getGeesesLocations()) { 
//        // Assign higher score for geese closer to the opponent's side
//        evalScore += POSITION_SCORE * (Consts.DEFAULT_BOARD_SIZE - gooseLocation.getRow());
//
//        // Assign higher score for geese occupying central positions
//        evalScore += CENTER_CONTROL_SCORE * Math.abs(gooseLocation.getCol() - Consts.DEFAULT_BOARD_SIZE / 2);
//    }
//
//    // Evaluate threat assessment
//    Location foxLocation = currentState.getFoxLocation();
//    for (Location gooseLocation : currentState.getGeesesLocations()) {
//        // Check if the goose is vulnerable to the fox
//        if (Location.isAdjacent(foxLocation, gooseLocation) && currentState.isTrapped(gooseLocation)) {
//            evalScore -= THREAT_SCORE;
//        }
//    }
//
//    return evalScore;
//}
    
//    public boolean isTrapped(Location location, State currentState) {
//    int row = location.getRow();
//    int col = location.getCol();
//
//    // Check if all adjacent locations are occupied by opponent pieces
//    boolean upOccupied = currentState.isLegal(new Location(row - 1, col));
//    boolean downOccupied = currentState.isLegal(new Location(row + 1, col));
//    boolean leftOccupied = currentState.isLegal(new Location(row, col - 1));
//    boolean rightOccupied = currentState.isLegal(new Location(row, col + 1));
//
//    // Check if all adjacent locations are occupied
//    boolean allAdjacentOccupied = upOccupied && downOccupied && leftOccupied && rightOccupied;
//
//    return allAdjacentOccupied;
//}



    
    
    @Override
    public void startNewGame(char[][] logicBoard) {}
    
    @Override
    public void closeYourself() {}

    @Override
    public void takeYourSign(char signPlayer) {}

    @Override
    public void waitForYourTurn() {}

    @Override
    public void chooseFirstLocation() {}
    
    @Override
    public  Location listenToFoxStartLocation() { return null; }

    @Override
    public void foxStartedLocation(Location loc) {}

    @Override
    public void playTurn(ArrayList<Location> allPossibleLocations) {}

    @Override
    public Location listenToClick() { return null; }

    @Override
    public void painBoard(char[][] logicBoard, ArrayList<Location> possibleLocations) {}

    @Override
    public void playerMoved(Move move, char signPlayer) {}

    @Override
    public void gameOver(char signPlayer) {}

    @Override
    public boolean listenToFinishCounddown() { return true; }
    
    @Override
    public void technicalVictory() {}
    
    /**
     * פעולה המתארת שחקן ממוחשב
     * @return תיאור השחקן הממוחשב
     */
    @Override
    public String toString()
    {
        return "Player AI";
    } 

    @Override
    public void serverClosed() {}
    


}
