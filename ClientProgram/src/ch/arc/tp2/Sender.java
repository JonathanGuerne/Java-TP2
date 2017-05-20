package ch.arc.tp2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/*
 * Project Name : ClientProgram
 * author : jonathan.guerne
 * creation date : 18.05.2017
*/
public class Sender extends Thread
{

    PrintWriter out;

    public Sender(PrintWriter out)
    {
        this.out = out;

    }


    @Override
    public void run()
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try
        {
            while (!isInterrupted())
            {
                String message = in.readLine();

                out.println(message);
                out.flush();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
