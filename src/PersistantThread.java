/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author zaidr
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
public class PersistantThread extends Thread{
    //this is the consumer thread
    private int ID;
    private Queue<Socket> q;
    public PersistantThread(int id, Queue<Socket> t)
    {
        this.q=t;
        this.ID=id;
    }
    
    @Override
    public void run()
    {
        Socket socket_to_work_on=q.dequeue();
        
        while(socket_to_work_on!=null)
        {
            BufferedReader r=null;
            
            try {
                r=new BufferedReader(new InputStreamReader(socket_to_work_on.getInputStream()));
                try (PrintWriter w = new PrintWriter(new OutputStreamWriter(socket_to_work_on.getOutputStream()),true) //autoflush=true?
                ) {
                    String s=r.readLine();
                    w.println("WELCOME TO THE SERVER! Type to begin ,Type BYE To END----->");
                    while(!s.equals("BYE"))
                    {
                        System.err.println(s);
                        w.println(s);
                        s=r.readLine();
                        
                        
                        //MD5 Function returning
                        MessageDigest md=MessageDigest.getInstance("MD5");
                        md.update(s.getBytes());
                        byte[]digest=md.digest();
                        String hash_to_return=DatatypeConverter.printHexBinary(digest).toUpperCase();
                        
                        w.println("THE MD5 Hash is ");
                        w.println(hash_to_return);
                        
                        
                    }
                    r.close();
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(PersistantThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                socket_to_work_on.close();
                socket_to_work_on=q.dequeue();
            } catch (IOException ex) {
                Logger.getLogger(PersistantThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
