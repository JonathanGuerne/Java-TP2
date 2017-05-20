package ch.arc.tp2.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/*
 * Project Name : ClientProgram
 * author : jonathan.guerne
 * creation date : 18.05.2017
*/
public class Sender extends Thread
{

    PrintWriter out;
    ArrayList<String> messages;


    public Sender(PrintWriter out)
    {
        this.out = out;
        messages = new ArrayList<>();

    }


    public synchronized void addMessage(String message){
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


            out.println(messages.get(0));
            out.flush();

            messages.remove(0);
        }
    }
}
