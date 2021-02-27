/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author zaidr
 */

import java.util.logging.Level;
import java.util.logging.Logger;


public class Queue<T> {
    private T []store=null;
    private int in=0, out=0, inStore=0, N;
    public Queue(int n)
    {
        N=n;
        store = (T[]) new Object[N];
    }

    Queue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public synchronized void enqueue(T x)
    {
        while(inStore==N)
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Queue.class.getName()).log(Level.SEVERE, null, ex);
            }
        store[in]=x;
        in = (in+1) % N;
        inStore++;
        notifyAll();        
    }

    public synchronized T dequeue()
    {
        while(inStore==0)
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Queue.class.getName()).log(Level.SEVERE, null, ex);
            }
        T temp = store[out];
        out = (out+1) % N;
        inStore--;
        notifyAll();                
        return temp;
    }
}
