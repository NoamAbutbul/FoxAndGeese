package ver1;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *  AppSocket.
 *  By noamabutbul | 19/10/2022 11:11
 */
public class AppSocket
{
    private Socket messageSocket; // סוקט להעברת הודעות ברשת
    private Socket cmdSocket; // סוקט להעברת פקודות ברשת

    private ObjectInputStream messageIs; // צינור לקבלת הודעות
    private ObjectOutputStream messageOs; //צינור לשליחת הודעות 
    
    private ObjectInputStream cmdIs; // צינור לקבלת פקודות
    private ObjectOutputStream cmdOs; // צינור לשליחת פקודות
    
    /**
     * פעולה בונה שמשמשת את השרת לחיבור לקוחות
     * @param messageSocket צינור להעברת הודעות
     * @param cmdSocket צינור להעברת פקודות
     */
    public AppSocket(Socket messageSocket, Socket cmdSocket)
    {
        this.messageSocket = messageSocket;
        this.cmdSocket = cmdSocket;
        try
        {
            // שליחה וקבלה נתונים מהצד השני
            this.messageOs = new ObjectOutputStream(messageSocket.getOutputStream()); 
            this.messageIs = new ObjectInputStream(messageSocket.getInputStream());
            
            // שליחה וקבלת פקודות מהצד השני
            this.cmdOs =  new ObjectOutputStream(cmdSocket.getOutputStream());
            this.cmdIs = new ObjectInputStream(cmdSocket.getInputStream());
            
        } 
        catch (Exception e)
        {
            System.out.println("Error: from AppSocket(Socket socket, String id) ");
        }
  
    }
    
    /**
     * פעולה בונה שמשמשת את הלקוח לחיבור לשרת
     * @param ipAddress IP כתובת
     * @param portNumber מספר הפורט שהשרת יושב עליו
     */
    public AppSocket(String ipAddress, int portNumber)
    {
        try
        {
            this.messageSocket = new Socket(ipAddress, portNumber);
            this.cmdSocket = new Socket(ipAddress, portNumber);
            
            // שליחה וקבלה נתונים מהצד השני
            this.messageOs = new ObjectOutputStream(messageSocket.getOutputStream()); 
            this.messageIs = new ObjectInputStream(messageSocket.getInputStream());
            
            // שליחה וקבלת פקודות מהצד השני
            this.cmdOs =  new ObjectOutputStream(cmdSocket.getOutputStream());
            this.cmdIs = new ObjectInputStream(cmdSocket.getInputStream());
           
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "There is no server", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
   
    
    /**
     * פעולה לשליחת הודעה לצד השני
     * @param msg הודעה להעברה
     */
    public void writeMessage(Message msg)
    {
        System.out.println("The Message to send: " + msg);

        try
        {
            this.messageOs.writeObject(msg);
            this.messageOs.flush(); // שלח עכשיו
        } 
        catch (Exception e)
        {
            System.out.println("Error: from writeMessage(Message msg) ");
        }
    }
    
    /**
     * פעולה לשליחת פקודה לצד השני
     * @param cmd פקודה להעברה
     */
    public void writeCmd(Message cmd)
    {
        System.out.println("The CMD to send: " + cmd);
        
        try
        {
            this.cmdOs.writeObject(cmd);
            this.cmdOs.flush(); // שלח עכשיו
        } 
        catch (Exception e)
        {
            System.out.println("Error: from writeCmd(Message cmd) " + e);
        }
    }
    
    /**
     * פעולה לקריאת הודעה מהצד השני
     * @return הודעה שהתקבלה
     */
    public Message readMessage()
    {
        Message msg = null;
        
        try
        {
            msg = (Message) messageIs.readObject();
        } 
        catch (Exception e)
        {
            System.out.println("Error: from readMessage() " + e);
        }
        
        return msg;
    }
    
    /**
     * פעולה לקריאת פקודה מהצד השני
     * @return פקודה שהתקבלה
     */
    public Message readCmd()
    {
        Message cmd = null;
        
        try
        {
            cmd = (Message) cmdIs.readObject();
        } 
        catch (Exception e)
        {
            System.out.println("Error: from readMessage() ");
        }
        
        return cmd;
    }
    
    
    /**
     * פעולה המחזירה את כתובת האייפי והפורט 
     * @return כתובת האייפי והפורט
     */
    public String getRemoteAddress()
    {
        return this.messageSocket.getRemoteSocketAddress().toString();
    }

    /**
     * פעולה לסגירת כל הסוקטים וכל הצינורות 
     */
    public void Close()
    {
        try
        {
            this.messageIs.close();
            this.messageOs.close();
            this.cmdIs.close();
            this.cmdOs.close();
            this.messageSocket.close();
            this.cmdSocket.close();
        } 
        catch (Exception e)
        {
            System.out.println("Close dosnet succssed");
        }
        
    }

    
    /**
     * פעולה המחזירה את תיאור האובייקט
     * @return תיאור האובייקט
     */
    @Override
    public String toString()
    {
        return "AppSocket{" + "messageSocket=" + messageSocket + ", cmdSocket=" + cmdSocket + '}';
    }
}
