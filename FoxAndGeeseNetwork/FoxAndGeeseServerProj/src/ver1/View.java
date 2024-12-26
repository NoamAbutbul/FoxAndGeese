package ver1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *  View.
 *  By noamabutbul | 19/10/2022 15:17
 */
public class View
{
    private final Server server; //תכונה שמקבלת את השרת שלו שייך התצוגה 
    
    private static JTextArea output; // שדה הלוג של השרת
    private JLabel stattusServer; // סטטוס שיישמש לכמות החיבורים הנוכחית
    private JFrame win;

    
    /**
     * פעולה בונה ליצירת התצוגה
     * @param server שרת שלו שייך התצוגה
     */
    public View(Server server)
    {
        this.server = server;
        win = null;
    }

   
    /**
     * פעולה היוצרת את חלון התצוגה ומרכיביו
     */
    public void creatGUI()
    {
        win = new JFrame();
        win.setSize(400, 450);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setLayout(new BorderLayout());
        
        
        win.setTitle("Server running...");
                
        this.output = new JTextArea(5, 20);
        output.setForeground(Color.GREEN);
        output.setBackground(Color.BLACK);
        output.setOpaque(true);
        
        
        //output.setEnabled(false); //לסדר

        JScrollPane scroll = new JScrollPane (output, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        
        this.stattusServer = new JLabel("Server running - waiting for clients...");
        stattusServer.setBackground(Color.BLACK);
        stattusServer.setOpaque(true);
        stattusServer.setForeground(Color.CYAN);

        win.add(stattusServer, BorderLayout.NORTH);
        win.add(scroll,BorderLayout.CENTER);
        
        win.setVisible(true);
 
        win.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                server.closeTheServer();
                win.dispose();
            }
     
        });
    }
    
    /**
     * פעולה להדפסת הלוג בתצוגת השרת
     * @param str המחזורת להדפסה בלוג
     */
    public static void log(String str)
    {
        output.append("\n\n" + str);
    }
    
    /**
     * פעולה המעדכנת את סטטוס החיבורים
     */
    public void updateStatusServer()
    {
        stattusServer.setText("All Connected By Now: " + server.getCounterPlayers());
    }
    
    public JFrame getWindow()
    {
        return win;
    }
    
    public void dispose()
    {
        win.dispose();
    }
}
