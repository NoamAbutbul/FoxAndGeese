package ver1;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Server.
 * By noamabutbul | 19/10/2022 15:22
 */
public class Server
{
    // Consts
    public final static int DEFAULT_SERVER_PORT_NUM = 2000; // קבוע לפורט דיפולטיבי
    public final static String SERVER_STRING = "[Server]"; // קבוע לתיאור השרת במחרוזת
    public final static int AI_CHOOSE = 0;
    public final static int NET_CHOOSE = 1;
    public final static int BREAKE = 2; //קבוע לנפילת חשמל אצל הלקוח 
    public final static int EXIT = -1;

    // Menegment server
    private ServerSocket serverSocket; // סוקט לשרת
    private String serverIp; // IP כתובת
    private int serverPort; // מספר פורט
    
    private boolean isReadyToRun;
    
    // Data
    private ArrayList<Game> games; // רשימת המשחקים
    private ArrayList<Player> players; // רשימת השחקנים
    private int counterGames; // ספירת המשחקים
    private int counterPlayers; // ספירת השחקנים
    
    // Players to match
    private PlayerNet playerNet1; // שחקן רשת ראשון
    private PlayerNet playerNet2; //שחקן רשת שני 
    
    
    // GUI:
    private View view; // תצוגה לשרת

    /**
     * פעולה בונה לשרת
     */
    public Server()
    {
        isReadyToRun = false;
        
        try
        {
            this.serverIp = InetAddress.getByName(InetAddress.getLocalHost().getHostName()).getHostAddress();
            
            this.serverSocket = choosePortNum();
            if (this.serverSocket == null)
            {
                System.out.println("Null");
                JOptionPane.showMessageDialog(this.view.getWindow(), "Cannot open server", "Server error", JOptionPane.ERROR_MESSAGE);
                this.view.dispose();
                return;
            }
            
            isReadyToRun = true;
            
            this.games = new ArrayList<>();
            this.players = new ArrayList<>();
            this.counterGames = 0;
            this.counterPlayers = 0;
            
            this.playerNet1 = null;
            this.playerNet2 = null;
          
            this.view = new View(this);
            this.view.creatGUI();
        } 
        
        catch (Exception e)
        {
            System.out.println("Error: The server disconnected" + e);
        }    
    }
    
    
    /**
     * פעולה המחזירה את כתובת האייפי של השרת
     * @return אייפי של השרת
     */
    public String getServerIp()
    {
        return serverIp;
    }

    /**
     * פעולה המחזירה את סוקט השרת
     * @return סוקט השרת
     */
    public ServerSocket getServerSocket()
    {
        return serverSocket;
    }
    
    /**
     * פעולה המחזירה את פורט השרת
     * @return פורט השרת
     */
    public int getServerPort()
    {
        return serverPort;
    }
    
    /**
     * פעולה המחזירה את רשימת המשחקים
     * @return רשימת המשחקים
     */
    public ArrayList<Game> getGames()
    {
        return games;
    }

    /**
     * פעולה המחזירה את רשימת השחקנים
     * @return רשימת השחקנים
     */
    public ArrayList<Player> getPlayers()
    {
        return players;
    }
    
    /**
     * פעולה המחזירה את מספר המשחקים
     * @return מספר המשחקים
     */
    public int getCounterGames()
    {
        return counterGames;
    }

    /**
     * פעולה המחזירה את מספר השחקנים
     * @return מספר השחקנים
     */
    public int getCounterPlayers()
    {
        return counterPlayers;
    }

   
    /**
     * פעולה המתחילה את ריצת השרת והמתנה ללקוחות
     */
    public void runServer()
    {
        if (isReadyToRun)
            view.log("Server runnig on " + this.serverIp + ":" + this.serverPort);
        
        try
        {
            while (true)
            {
                AppSocket appSocketToClinet = new AppSocket(serverSocket.accept(), serverSocket.accept());
                handleCient(appSocketToClinet);  
            }
            
        } catch (Exception e)
        {
            System.out.println("Error Server: ");
        }
        
    }
    
    /**
     * פעולה המטפלת בלקוח
     * @param appSocketToClient 
     */
    public void handleCient(AppSocket appSocketToClient)
    {
        Thread handleThread = new Thread(() ->
        {
            PlayerNet playerNet = new PlayerNet(Consts.FOX_SIGN, appSocketToClient);
            
            listenToCmd(playerNet);
            
            playerNet.setId(++counterPlayers + "");
            
            view.log("client " + (playerNet.getId()) + " connected");
            view.updateStatusServer();
            
            synchronized (players)
            {
                players.add(playerNet);
            }
            
            int choice = playerNet.chooseOpponentProcess();
            
            switch (choice)
            {
                case AI_CHOOSE:
                    handleAIChoose(playerNet);
                    break;
                case NET_CHOOSE:
                    handleNetChoose(playerNet);
                    break;
                case BREAKE:
                    return;
                    
                case EXIT:
                    playerNet.closeYourself();
                    return;
                    
                default:
                    handleAIChoose(playerNet);
            }
        });
        handleThread.setName("handleThread");
        handleThread.start();
    }
    
    /**
     * פעולה המטפלת בבחירת משחק מול שחקן ממוחשב
     * @param playerNet שחקן הרשת שיוזם את המשחק
     */
    private void handleAIChoose(PlayerNet playerNet)
    {
        PlayerAI playerAI = new PlayerAI(Consts.GEESE_SIGN);
        Game game = new Game(playerNet, playerAI);
        
        view.log("Started a new game with:\n"
                + "\tFirst Player: " + playerNet + "\n"
                + "\tSecond Player: " + playerAI);
                
        synchronized (games)
        {
            games.add(game);
        }
        
        game.initGame();
    }
    
    /**
     * פעולה המטפלת בבחירת משחק מול שחקן רשת
     * @param playerNet שחקן רשת היוזם את המשחק
     */
    private void handleNetChoose(PlayerNet playerNet)
    {
        if (playerNet1 == null)
        {
            playerNet1 = playerNet;
            playerNet1.waitForPartner();
        }
        
        else
        {
            playerNet2 = playerNet;
            playerNet2.setSign(Consts.GEESE_SIGN);
            
            Game game = new Game(playerNet1, playerNet2);
            
            view.log("Started a new game with:\n"
                + "\tFirst Player: " + playerNet1 + "\n"
                + "\tSecond Player: " + playerNet2);
            
            synchronized (games)
            {
                games.add(game);
            }
            
            game.initGame();
            
            playerNet1 = playerNet2 = null;
        }
    }
    

    
    /**
     * פעולה לסגירת השרת
     */
    public void closeTheServer()
    {
        for (int i = 0; i < players.size(); i++)
        {
            if(players.get(i) != null)
            {
                players.get(i).serverClosed();
                players.get(i).closeYourself();
            }
                
        }
    }
    
    
    /**
     * פעולה המאזינה לפקודות מהלקוח
     * @param playerNet שחקן הרשת שאליו מאזינים
     */
    public void listenToCmd(PlayerNet playerNet)
    {
        Thread listenToCmdThread = new Thread(() ->
        {
            Message cmd = playerNet.getAppSocket().readCmd();

            synchronized (players)
            {
                players.remove(playerNet);
            }
            playerNet.closeYourself();

            for (int i = 0; i < games.size(); i++)
            {
                Player playerToRemove = games.get(i).getPartnerPlayer(playerNet.getId());
                if (playerToRemove != null)
                {
                    playerToRemove.technicalVictory();
                    synchronized (players)
                    {
                        players.remove(playerToRemove);
                    }
                    playerToRemove.closeYourself();
                    synchronized (games)
                    {
                        games.get(i).stopGame();
                        games.remove(games.get(i));
                    }
                }
            }
        });
        
        listenToCmdThread.start();
        listenToCmdThread.setName("listenToCmdThread");
    }
    
    /**
     * פעולה המעלה הודעה למשתמש בשרת לבחור פורט שהשרת יישב אליו
     * ויוצרת סוקט לשרת עם אותו הפורט
     * במקרה של כישלון בפורט השרת יפעל עם הפורט הדיפולטיבי
     * @return סוקט לשרת
     */
    private ServerSocket choosePortNum() 
    {
        String portStr = (String) JOptionPane.showInputDialog(null, 
                "Enter Server Port Number", 
                "Input", 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                null, 
                DEFAULT_SERVER_PORT_NUM);
        
        
        try
        {
            this.serverPort = Integer.parseInt(portStr);
            return new ServerSocket(Integer.parseInt(portStr));
        }

        catch(Exception ex)
        {
            //                this.serverPort = DEFAULT_SERVER_PORT_NUM;
//                return new ServerSocket(DEFAULT_SERVER_PORT_NUM);
            
            JOptionPane.showMessageDialog(null, "Cannot open server", "Server error", JOptionPane.ERROR_MESSAGE);
            //this.view.dispose();
            return null;
        }
    
    }
    
    
    //--------------------Main-----------//
    public static void main(String[] args)
    {
        Server server = new Server();
        server.runServer();
    }
}
