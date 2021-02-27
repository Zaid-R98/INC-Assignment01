/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author zaidr
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
        
public class ConcurrentServer extends Thread{
    
    //This is the producer thread
    //This is the concurrent server

    /**
     */
    private Queue<Socket> q=null;
    private PersistantThread[]pool=null;
    private int N;
    private int Port;
    private boolean continueWorking=true;
    public ConcurrentServer(int numThreads, int p)
    {
        
        System.out.println("Conrurrent Server Deatails 1:   ");
        System.out.println("NThreads: "+numThreads+"   "+"Port Number "+p);
        
        q=new Queue<>(numThreads);
        pool=new PersistantThread[numThreads];
        
        for(int i=0;i<numThreads;i++)
        {
            pool[i]=new PersistantThread(i,q);// passing the referance as it has to be commonly shared between pthreads.
            pool[i].start();
        }
        N=numThreads;
        Port=p;
        
        System.out.println("Conrurrent Server Deatails 2:   ");
        System.out.println("NThreads: "+N+"   "+"Port Number "+Port);
    }

    public boolean isContinueWorking() {
        return continueWorking;
    }

    public void setContinueWorking(boolean continueWorking) {
        this.continueWorking = continueWorking;
    }
    
    
    @Override
    public void run() {
        try {
            ServerSocket ss=new ServerSocket(Port);
            Socket inc=null;
            while(continueWorking)
            {
                inc=ss.accept();
                
                if(continueWorking)
                q.enqueue(inc);
            }
        } catch (IOException ex) {
            Logger.getLogger(ConcurrentServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int i=0;i<N;i++)
        {
            q.enqueue(null);// adding nulls as a signal to stop 
        }
        for(int i=0;i<N;i++)
        {
            try {
                pool[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(ConcurrentServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
 