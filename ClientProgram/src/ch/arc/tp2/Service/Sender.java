package ch.arc.tp2.Service;

import ch.arc.tp2.Packets.Packet;
import ch.arc.tp2.Packets.TextMessage;

import java.io.*;
import java.util.ArrayList;

/**
 * Project Name : ClientProgram
 * @author anthony.fleury, guerne.jonathan
 * @date  18.05.2017
 */
public class Sender extends Thread
{

    ObjectOutputStream out;
    ArrayList<Packet> messages;


    public Sender(ObjectOutputStream out)
    {
        this.out = out;
        messages = new ArrayList<>();

    }


    public synchronized void addMessage(Packet message){
        messages.add(message);
        notify();
    }

    @Override
    public void run()
    {

        while (!isInterrupted())
        {
            while (messages.size()==0){
                try
                {
                    synchronized (this)
                    {
                        wait();
                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }


            try
            {
                out.writeObject(messages.get(0));
                out.flush();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            messages.remove(0);
        }
    }
}
