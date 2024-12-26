package ver1;

import com.apple.eawt.Application;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
// לסדר את הבחירה הראשונית של השועל
/**
 * View.
 * By noamabutbul | 24/01/2022 13:30
 */
public class View
{
    public static final int DEFAULT_BOARD_SIZE = 8; //הגודל הדיפולטיבי של הלוח 
    public static final char FOX_SIGN = 'F'; // סימן השועל
    public static final char GEESE_SIGN = 'G'; // סימן האווז
    
    public static final String WIN_TITLE = "Fox & Geese Client"; // כותרת לחלון המשחק
    public static final Font FONT1 = new Font(null, Font.BOLD, 24);  // פונט לשחקנים
    public static final Font FONT2 = new Font(null, Font.BOLD, 16);  // פונט לכפתורים
    public static final int BUTTON_SIZE = 70; // גודל כפתורי הלוח
    public static final int ICON_SIZE = (int)(BUTTON_SIZE * 0.70); // גודל האייקון בהתאם לגודל הלוח
    public static final String FOX_START_STRING = "Please choose your first location"; // הגדרת מחרוזת לבדיקה אם הלחיצה הראשונה היא בחירת מיקום השועל
    
    private Client client; // הגדרת הלקוח
    
    //תכונות המחלקה 
    private JFrame win; // חלון התצוגה הראשי
    private JLabel lblMsg;       // תוית להצגת תור מי לשחק
    private JButton[][] btnMat;  // מטריצת הכפתורים
    
    private static int ROWS;  // מספר השורות במטריצה
    private static int COLS;  // מספר העמודות במטריצה
    private final ImageIcon icon_fox, icon_geese, icon_about, icon_rules, icon_Close; //שמירת האייקונים 


    /**
     * פעולה בונה של התצוגה
     * @param client לקוח
     */
    public View(Client client)
    {
        this.client = client;
        ROWS = DEFAULT_BOARD_SIZE;
        COLS = DEFAULT_BOARD_SIZE;
        
        
        // load assets
        icon_fox = loadImage("/assets/fox.png", ICON_SIZE, ICON_SIZE);
        icon_geese = loadImage("/assets/goose.png", ICON_SIZE, ICON_SIZE);
        icon_about = loadImage("/assets/About.png", ICON_SIZE, ICON_SIZE);
        icon_rules = loadImage("/assets/Rules.png", ICON_SIZE, ICON_SIZE);
        icon_Close = loadImage("/assets/Close.png", ICON_SIZE, ICON_SIZE);
    }

    /**
     * פעולה המקבלת את הלוח הלוגי של המשחק ומכינה בהתאם ללוח המתקבל את התצוגה
     * @param logicBoard הלוח הלוגי
     */
    public void setup(char logicBoard[][])
    {
        for (int row = 0; row < ROWS; row++)
        {
            // לולאה שעוברת על כל העמודות
            for (int col = 0; col < COLS; col++)
            {
                btnMat[row][col].setIcon(null);

                if (logicBoard[row][col] == GEESE_SIGN)
                {
                    btnMat[row][col].setIcon(icon_geese);
                    btnMat[row][col].setDisabledIcon(icon_geese);
                }
                
                btnMat[row][col].setEnabled(false);

                if (btnMat[row][col].getText().equals(GEESE_SIGN + ""))
                {
                    btnMat[row][col].setForeground(Color.orange);
                }
                if ((row == ROWS - 1) && (col % 2 == 0))
                {
                    btnMat[row][col].setEnabled(true);
                    btnMat[row][col].setBackground(Color.blue);
                    btnMat[row][col].setOpaque(true);
                }
            }
        }

       
        lblMsg.setText("Fox - please choose location for start");
        lblMsg.setBackground(null);

        win.setVisible(true);

    }

    /**
     * פעולה היוצרת ממשק גרפי למהלך המשחק
     */
    public void createGUI()
    {
        win = new JFrame(WIN_TITLE);
        win.setSize(550, 600);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setResizable(false);
        win.setIconImage(loadImage("/assets/fox.png", -1, -1).getImage());
        
        try
        {
            Application.getApplication().setDockIconImage(loadImage("/assets/fox.png", -1, -1).getImage());

        } catch (Exception e)
        {
            
        }
        
        win.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                Message cmd = new Message(Message.Status.EXIT_MESSAGE);
                client.getAppSocket().writeCmd(cmd);
            }
        });
        
        
        // יצירת תפריט ניהול המשחק
        System.setProperty("apple.laf.useScreenMenuBar", "true"); // התאמת התפריט למערכת ההפעלה של אפל
        JMenuBar MenuBar = new JMenuBar();
        
        JMenu menuAbout = menuAbout = new JMenu("About");
        MenuBar.add(menuAbout);
        
        JMenuItem credits = new JMenuItem("Credits");
        menuAbout.add(credits);
        
        
        credits.addActionListener((ActionEvent ae) ->
        {
            JOptionPane.showMessageDialog(win, "Created by Noam Abutbul© since 2022\nIcons - www.flaticon.com\nUI designer - Noam Abutbul", "Credists", JOptionPane.INFORMATION_MESSAGE, icon_about);
        });
        
        JMenuItem rules = new JMenuItem("Rules");
        menuAbout.add(rules);
        
        rules.addActionListener((ActionEvent e) ->
        {
            JOptionPane.showMessageDialog(win, "The Fox goes first.\n"
                    + "Geese can only move forward while the Fox can move forward and backward.\n"
                    + "Neither the Fox nor geese can jump squares.\n"
                    + "The game ends when the geese corner the Fox and he cannot move or if the Fox reaches the opponent's king row."
                    , "Rules", JOptionPane.INFORMATION_MESSAGE, icon_rules);
        });
 
        // הוספת התפריט לחלון
        win.setJMenuBar(MenuBar);

        // יצירת פאנל לכפתורים
        JPanel pnlButtons = new JPanel(new GridLayout(ROWS, COLS));

        // מערך הכפתורים של הלוח
        btnMat = new JButton[ROWS][COLS];

        //הגדרת התצוגה מותאמת למחשבי מק 
        try
        {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        // יצירת כל כפתורי הלוח קביעת הפונט שלהם והוספתם לחלון על פי גריד שנקבע
        // לולאה שעוברת על כל השורות במטריצה
        for (int row = 0; row < ROWS; row++)
        {
            // לולאה שעוברת על כל העמודות
            for (int col = 0; col < COLS; col++)
            {
                // יצירת כפתור בלוח המשחק
                btnMat[row][col] = new JButton();
                btnMat[row][col].setFont(FONT1);
                btnMat[row][col].setFocusable(false);

                //הגדרת הפונט לכפתורים 
                btnMat[row][col].setFont(FONT1);
                btnMat[row][col].setText("");

                //צביעת כל הכפתורים בצבע שחור 
                btnMat[row][col].setBackground(Color.black);
                btnMat[row][col].setOpaque(true);
                btnMat[row][col].setBorderPainted(false);

                //צביעת כל הכפתורים שנמצאים באלכסונים בצבע לבן 
                if ((col - row) % 2 == 0)
                {
                    btnMat[row][col].setBackground(Color.white);
                    btnMat[row][col].setOpaque(true);
                }

                btnMat[row][col].setActionCommand(row + "," + col); // save indexs (row,col)

                // הוספת מאזין לאירוע לחיצה על הכפתור
                btnMat[row][col].addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        Thread buttonPressedThread = new Thread(() ->
                        {
                            // שמירת המיקום הנלחץ ושליחתו לבקר
                            JButton btn = (JButton) e.getSource();
                            String indexs = btn.getActionCommand(); // get indexes (row,col)
                            int row1 = Integer.parseInt(indexs.substring(0, indexs.indexOf(',')));
                            int col1 = Integer.parseInt(indexs.substring(indexs.indexOf(',') + 1));

                            // בתחילת המשחק השועל בוחר מיקום התחלתי
                            if (lblMsg.getText().equals(FOX_START_STRING))
                            {
                                client.foxSelectedStartLocation(new Location(row1, col1));
                                unpainAfterFoxStarted();
                            } else
                            {
                                client.boardButtonPressed(new Location(row1, col1));
                            }
                        });
                        buttonPressedThread.start();
                        buttonPressedThread.setName("button Pressed Thread");
                    }
                });
                // הוספת הכפתור לגריד שבפנאל
                pnlButtons.add(btnMat[row][col]);
            }
        }

        lblMsg = new JLabel("Preparing to the game");
        lblMsg.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 2));
        lblMsg.setOpaque(true);
        lblMsg.setFont(FONT2);
        
       
        // הוספת הפאנל למרכז החלון
        win.add(pnlButtons, BorderLayout.CENTER);

        // הוספת התוית לדרום החלון
        win.add(lblMsg, BorderLayout.SOUTH);

        // מרכז החלון
        win.setLocationRelativeTo(null);
        
        
        //הראה את החלון 
        win.setVisible(true);
    }
    
    /**
     * פעולה המוודאת עם המשתמש האם הוא בטוח לצאת
     */
    private void closeAndExitDialog()
    {
        String title = "Close & Exit Game";
        String msg = "Do you really want to Exit?";

        // מקפיץ הודעה למשתמש
        int status = JOptionPane.showConfirmDialog(win, msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, icon_Close);

        switch (status)
        {
            case JOptionPane.DEFAULT_OPTION:
                break;

            case JOptionPane.YES_OPTION:
                win.dispose();
                break;
                
            case JOptionPane.NO_OPTION:
                break;
                
            case JOptionPane.CANCEL_OPTION:
                System.out.println("CANCEL");
                
                break;
        }
    }
    

    /**
     * פעולה המקבלת מיקום יעד ומקור ותור מי לשחק ומעדכנת את התצוגה בהתאם
     * @param source מיקום המקור
     * @param dest מיקום היעד
     * @param player תור מי לשחק
     */
    public void updateBoardButton(Location source, Location dest, char player)
    {
       
        btnMat[source.getRow()][source.getCol()].setIcon(null);
        
        
        if (player == FOX_SIGN)
        {
            lblMsg.setText("Geese Turn");
            btnMat[dest.getRow()][dest.getCol()].setIcon(icon_fox);
            btnMat[dest.getRow()][dest.getCol()].setDisabledIcon(icon_fox);


            btnMat[dest.getRow()][dest.getCol()].setForeground(Color.PINK);
            
        } 
        else
        {
            lblMsg.setText("Fox Turn");
            btnMat[dest.getRow()][dest.getCol()].setForeground(Color.orange);
            
            btnMat[dest.getRow()][dest.getCol()].setIcon(icon_geese);
            btnMat[dest.getRow()][dest.getCol()].setDisabledIcon(icon_geese);
        }
    }
    
    

    /**
     * פעולה המעדכנת את מיקום השועל ההתחלתי בלוח
     * @param loc מיקום השועל
     */
    public void foxSelectedStartLocation(Location loc)
    {
        btnMat[loc.getRow()][loc.getCol()].setIcon(icon_fox);
        btnMat[loc.getRow()][loc.getCol()].setDisabledIcon(icon_fox);

        btnMat[loc.getRow()][loc.getCol()].setForeground(Color.PINK);
    }

    /**
     * הפעולה מכבה את כל הכפתורים וכותבת מי המנצח במשחק
     * @param winPlayer סימן השחקן המנצח
     */
    String gameOver(char winPlayer)
    {
        for (int row = 0; row < ROWS; row++)
            for (int col = 0; col < COLS; col++)
                btnMat[row][col].setEnabled(false);
            
        lblMsg.setText("Game Over: ");
        if (winPlayer == FOX_SIGN)
            lblMsg.setText(lblMsg.getText() + " The Fox Won!!!");
        
        else
            lblMsg.setText(lblMsg.getText() + " The Geese Won!!!");

        setupColors();
        return lblMsg.getText();
    }

    /**
     * פעולה המקבלת רשימה של מהלכים ותור מי לשחק ועובדת לאחר הלחיצה השנייה בלוח המשחק
     * הפונקציה עבור האווזים פותחת להם את כפתורי המשחק לבחירת שחקן ועבור השועל
     * צובעת ופותחת את המקומות האפשריים בשבילו
     * @param possibleMoves רשימת מהלכים אפשריים לשחקן
     * @param currentPlyer תור מי לשחק
     */
     void afterSecondClick(ArrayList<Location> possibleLocations, char currentPlyer)
    {
        for (int row = 0; row < ROWS; row++)
        {
            for (int col = 0; col < COLS; col++)
            {
                btnMat[row][col].setEnabled(false);
            }
        }
        for (int i = 0; i < possibleLocations.size(); i++)
        {
            if (currentPlyer == GEESE_SIGN)
            {
                btnMat[possibleLocations.get(i).getRow()][possibleLocations.get(i).getCol()].setEnabled(true); 
            }
            if (currentPlyer == FOX_SIGN)
            {
                btnMat[possibleLocations.get(i).getRow()][possibleLocations.get(i).getCol()].setEnabled(true);
                btnMat[possibleLocations.get(i).getRow()][possibleLocations.get(i).getCol()].setBackground(Color.blue);
                btnMat[possibleLocations.get(i).getRow()][possibleLocations.get(i).getCol()].setOpaque(true);
            }
        }
    }
    
 
    
    /**
     * פונקציה המאפסת את כפתורי המשחק למצב ההתחלתי לאחר כל סיום תור
     */
    void setupColors()
    {
        for (int row = 0; row < ROWS; row++)
        {
            for (int col = 0; col < COLS; col++)
            {
                //צביעת כל הכפתורים בצבע שחור 
                btnMat[row][col].setBackground(Color.black);
                btnMat[row][col].setOpaque(true);
                btnMat[row][col].setBorderPainted(false);

                //צביעת כל הכפתורים שנמצאים באלכסונים בצבע לבן 
                if ((col - row) % 2 == 0)
                {
                    btnMat[row][col].setBackground(Color.white);
                    btnMat[row][col].setOpaque(true);
                }
            }
        }
    }
    
    /**
     * פונקציה מיוחדת לחתילת המשחק ועובדת פעם אחת
     * רק לאחר שהשועל בוחר את מיקומו ההתחתלתי
     * הפונקציה מאתחלת בחזרה את כפתורי המשחק
     */
    void unpainAfterFoxStarted()
    {
        for (int col = 0 ; col < COLS; col++)
        {
            if (col % 2 == 0)
                btnMat[ROWS-1][col].setBackground(Color.black);
                btnMat[ROWS-1][col].setOpaque(true);
        }
    }
    
    /**
     * פעולה הטוענת אייקון לממשק
     * @param fileName מחרוזת מיקום הקובץ
     * @param width רוחב התמונה
     * @param height גובה התמונה
     * @return אייקון לאחר שנטען
     */
    private ImageIcon loadImage(String fileName, int width, int height)
    {
        // טעינת התמונה מתוך הקובץ שנמצא בתקיית הנכסים
        ImageIcon imgIcon = new ImageIcon(getClass().getResource(fileName));
        
        if(width!=-1 || height!=-1)
            imgIcon = new ImageIcon(imgIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        
        return imgIcon;
    }

    /**
     * פעולה המחליפה את תור השחקן וכותבת בהודעה למטה תור מי לשחק בהתאם
     * @param player תור השחקן האחרון
     */
    public void turnSwiched(char player)
    {
        if (player == FOX_SIGN)
        {
            lblMsg.setText("Geese Turn");
        } else
        {
            lblMsg.setText("Fox Turn");
        }
    }
    
    /**
     * פעולה המעדכנת את מחרוזת הסטטוס בר
     * @param statusString מחרוזת הסטטוס בר החדשה
     */
    public void updateStatusLable(String statusString)
    {
        this.lblMsg.setText(statusString);
    }
    
    
    /**
     * פעולה הנועלת את כל הכפתורים בלוח המשחק
     */
    public void lockAllBoard()
    {
        for (int row = 0; row < ROWS; row++)
        {
            for (int col = 0; col < COLS; col++)
            {
                btnMat[row][col].setEnabled(false);
            }
        }
    }
    
   
    /**
     * פעולה המציבה את סימן השחקן בכותרת
     * @param signPlayer סימן השחקן
     */
    public void setTextTitlePlusSign(char signPlayer)
    {
        if (signPlayer == FOX_SIGN)
        {
            win.setTitle(win.getTitle() + " (Fox)");
        }
        else if (signPlayer == GEESE_SIGN)
        {
            win.setTitle(win.getTitle() + " (Geese)");
        }
    }  
    
    
    /**
     * פונקציה המקבלת את לוח המשחק ומיקום מקור ועובדת לאחר הלחיצה הראשונה
     * הפונקציה צובעת ונועלת כפתורים בהתאם לאפשרויות של האווזים
     * @param board מטריצה המצייגת את לוח המשחק
     * @param sourceLocation מיקום המקור של השחקן הנבחר
     */
    void afterFirstClick(char[][] board, ArrayList<Location> possibleLocations)
    {
        for (int i = 0; i < possibleLocations.size(); i++)
        {
            btnMat[possibleLocations.get(i).getRow()][possibleLocations.get(i).getCol()].setEnabled(true);
            btnMat[possibleLocations.get(i).getRow()][possibleLocations.get(i).getCol()].setBackground(Color.blue);
            btnMat[possibleLocations.get(i).getRow()][possibleLocations.get(i).getCol()].setOpaque(true);
        }
        for (int row = 0; row < ROWS; row++)
        {
            for (int col = 0; col < COLS; col++)
            {
                if (board[row][col] == GEESE_SIGN)
                    btnMat[row][col].setEnabled(false);
            }
        }        
    }
   
    /**
     * פעולה המחזירה את מחרוזת הסטטוס בר
     * @return מחרוזת הסטטוס בר
     */
    public String getStringStatusBar()
    {
        return lblMsg.getText();
    }
   
    /**
     * פעולה המציגה חלון לבחירת הלקוח מול מי לשחק
     * @return בחירת הלקוח מול מי לשחק
     * 0 לשחקן ממוחשב
     * 1 לשחקן רשת
     */
    public int chooseOpponentOptions()
    {
        Object[] options = {"Player AI", "Player NET"};
        int choose = JOptionPane.showOptionDialog(win,
                "Please choose player to play with.",
                "AI or NET",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                null);
        System.out.println("Choose from view = " + choose);
        return choose;
    }

    /**
     * פעולה הסוגרת את התצוגה
     */
    public void dispose()
    {
        win.dispose();
    }


    /**
     * פעולה המציגה הודעת ניצחון טכני
     */
    public void technicalVictory()
    {
        JOptionPane.showMessageDialog(null, "The opponent left the game\nYou are the winner!\n\nThe game will be close", "Technical Victory", JOptionPane.DEFAULT_OPTION);
    }

    void serverClosed()
    {
        JOptionPane.showMessageDialog(null, "The server closed\n\nThe game will be close", "Server Closed", JOptionPane.DEFAULT_OPTION);
    }

}
