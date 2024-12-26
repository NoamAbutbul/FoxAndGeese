package ver1;

import java.net.Inet4Address;
import javax.swing.JOptionPane;

/**
 *  Client.
 *  By noamabutbul | 22/10/2022 21:22
 */
public class Client
{
    public final static int SERVER_PORT_NUM = 2000; // הפורט הדיפולטיבי של השרת
    public static final int TIME_COUNTDOWN = 5; // זמן ספירה לאחור בשניות לאחר סיום המשחק


    // Menegment client
    private AppSocket appSocket; // אפסוקט לחיבור לשרת
    private String serverIP; // כתובת האייפי של השרת
    private int serverPort; // פורט שעליו מאזין השרת
    private String id; // מזהה הלקוח
    private char signPlayer; // סימן השחקן
         
    private boolean isConnect;
    
    // GUI
    private View view; // תצוגת הלקוח

 
    /**
     * פעולה בונה ללקוח
     */
    public Client()
    {
        this.view = new View(this);
        this.view.createGUI();
        if (!chooseIpAndPort())
            return;

        
        this.appSocket = new AppSocket(serverIP, serverPort);
        if (this.appSocket != null)
            this.isConnect = true;
        else
            isConnect = false;
    }

    /**
     * פעולה המחזירה את האפסוקט של הלקוח
     * @return האפסוקט של הלקוח
     */
    public AppSocket getAppSocket()
    {
        return appSocket;
    }

    /**
     * פעולה המחזירה את כתובת האייפי של השרת
     * @return כתובת האייפי של השרת
     */
    public String getServerIP()
    {
        return serverIP;
    }

    /**
     * פעולה המעדכנת את האייפי של השרת
     * @param serverIP האייפי של השרת
     */
    public void setServerIP(String serverIP)
    {
        this.serverIP = serverIP;
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
     * פעולה המעדכנת את פורט השרת
     * @param serverPort פורט השרת
     */
    public void setServerPort(int serverPort)
    {
        this.serverPort = serverPort;
    }

    
    /**
     * פעולה המחזירה את מזהה הלקוח
     * @return מזהה הלקוח
     */
    public String getId()
    {
        return id;
    }
    
    /**
     * פעולה המחזירה את סימן הלקוח
     * @return סימן הלקוח
     */
    public char getSignPlayer()
    {
        return signPlayer;
    }
    
    
    /**
     * פעולה המאזינה להודעות מהשרת
     */
    public void getMessageFromServer()
    {
        if (isConnect)
        {
            Thread getMessageFromServerThread = new Thread(() ->
            {
                while (true)
                {
                    Message msg = appSocket.readMessage();

                    if (msg == null)
                        break;

                    treatmentMessage(msg);
                }
            });
            getMessageFromServerThread.start();
            getMessageFromServerThread.setName("Get Message From Server Thread");
        }
    }
    
    /**
     * פעולה המקבלת הודעה מהשרת ומטפלת בה בהתאם
     * @param msg הודה שהתקבלה מהשרת
     */
    public void treatmentMessage(Message msg)
    {
        if (msg != null)
        {
            System.out.println("\n\nMessage: " + msg);
      
            switch (msg.getSubject())
            {
                case CHOOSE_OPPONNENT:
                    int choose  = view.chooseOpponentOptions();
                    Message msgChosseOpponent = new Message(Message.Status.CHOOSE_OPPONNENT);
                    msgChosseOpponent.setTypeOfPlayer(choose);
                    sendMessage(msgChosseOpponent);
                    break;

                case WAIT_FOR_PARTNER:
                    view.updateStatusLable("Wait for partner");
                    view.lockAllBoard();
                    break;

                 case SIGN_PLAYER:
                    this.signPlayer = msg.getSignPlayer();
                    view.setTextTitlePlusSign(signPlayer);
                    break;

                case START_NEW_MESSAGE:
                    view.setup(msg.getLogicBoard());
                    break;

                case WAIT_TURN:
                    view.updateStatusLable("Wait Turn");
                    view.lockAllBoard();
                    view.unpainAfterFoxStarted();
                    break;

                case CHOOSE_FIRST_LOCATION:
                    view.updateStatusLable("Please choose your first location");
                    break;

                case FOX_START_LOCATION:
                    view.foxSelectedStartLocation(msg.getFoxStartLocation());
                    break;

                case PLAY_TURN:
                    view.afterSecondClick(msg.getAllPossibleLocations(), this.signPlayer);
                    view.updateStatusLable("Play Turn");
                    break;          

                case PAIN_AFTER_FIRST_CLICK:
                    view.afterFirstClick(msg.getLogicBoard(), msg.getAllPossibleLocations());
                    break;

                case PLAYER_MOVED:
                    Location source = msg.getSourceLocation();
                    Location dest = msg.getDestLocation();
                    char sign = msg.getSignPlayer();
                    view.updateBoardButton(source, dest, sign);
                    view.setupColors();
                    break;

                case GAME_OVER:
                    String winnerStr = view.gameOver(msg.getSignPlayer());
                    finalCountdown(winnerStr);
                    break;
                default:
            }
        }
    }
    
    
    
    /**
     * פעולה המעדכנת את השרת בבחירת מיקום השועל
     * @param loc מיקום בחירת השועל
     */
    public void foxSelectedStartLocation(Location loc)
    {
        Message msg = new Message(Message.Status.FOX_START_LOCATION);
        msg.setFoxStartLocation(loc);
        sendMessage(msg);
        view.foxSelectedStartLocation(loc);
    }

    
    /**
     * פעולה המעדכנת את השרת בלחיצה מהלקוח על לוח המשחק
     * @param loc מיקום הלחיצה בלוח המשחק
     */
    void boardButtonPressed(Location loc)
    {
        Message msg = new Message(Message.Status.CLICK);
        msg.setClickLocation(loc);
        appSocket.writeMessage(msg);
     }
    
    
    
    /**
     * פעולה המעדכנת את התצוגה מי המנצח
     * ומתחילה לספור לאחור חמש שניות ולהתחיל משחק חדש
     * @param winPlayerStr מחרוזת המאתרת את המנצח
     */
    private void finalCountdown(String winPlayerStr)
    {
        Thread countDownThread = new Thread(() ->
        {
            for (int i = TIME_COUNTDOWN; i > 0; i--)
            {
                try
                {
                    view.updateStatusLable(winPlayerStr + "\t\tNew game start in: " + i + "...");
                    Thread.sleep(1000);
                } catch (Exception e)
                {
                    System.out.println("Exception from finalCountdown: " + e);
                }
            }
            Message msg = new Message(Message.Status.COUNTDOWN_FINISHED);
            appSocket.writeMessage(msg);
        });
        countDownThread.start();
        countDownThread.setName("countDown Thread");
    }
    
    /**
     * פעולה שמעלה חלון ללקוח לבחור כתובת אייפי ופורט
     * של השרת אליו אנו רוצים להתחבר
     * @return אמת במידה והצליח אחרת שקר
     */
    public boolean chooseIpAndPort()
    {
        String computerIP = "";
        try
        {
            computerIP = Inet4Address.getLocalHost().getHostAddress();
        } catch (Exception e)
        {
            System.out.println("Exception from chooseIpAndPort " + e);
            return false;
        }
        
        String serverIpAndPort = (String) JOptionPane.showInputDialog("Enter Server Address [IP:PORT]:", computerIP + ":" + SERVER_PORT_NUM);
        
        if (serverIpAndPort == null)
        {
            return false;
        }
        
        setServerIP(serverIpAndPort.substring(0, serverIpAndPort.indexOf(':')));
        setServerPort(Integer.parseInt(serverIpAndPort.substring(serverIpAndPort.indexOf(':') + 1)));
        
        return true;
    }
    
    /**
     * פעולה השולחת הודעה לצד השני
     * @param msg הודעה לשליחה
     */
    public void sendMessage(Message msg)
    {
        appSocket.writeMessage(msg);
    }  
  
        
    /**
     * פעולה המאזינה לפקודות מהשרת
     */
    public void getCmdFromServer()
    {
        if (isConnect)
        {
            Thread getCmdFromServerThread = new Thread(() ->
            {
                Message cmd = appSocket.readCmd();

                if (appSocket != null)
                    appSocket.Close();

                if (cmd != null && cmd.getSubject() == Message.Status.TECHNICAL_VICTORY)
                    view.technicalVictory();
                
                if (cmd != null && cmd.getSubject() == Message.Status.SERVER_CLOSED)
                    view.serverClosed();

                view.dispose();

            });
            getCmdFromServerThread.setName("Get Cmd From Server Thread");
            getCmdFromServerThread.start();
        }
    }
    
    
    
    //--------------------Main-----------//
    public static void main(String[] args) 
    {

        Client client = new Client();
        client.getMessageFromServer();
        client.getCmdFromServer();
    }
}
