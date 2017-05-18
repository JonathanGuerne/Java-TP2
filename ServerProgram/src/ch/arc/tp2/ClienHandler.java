package ch.arc.tp2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * Project Name : reseau
 * author : jonathan.guerne
 * creation date : 04.05.2017
*/
public class ClienHandler implements Runnable
{
    Socket socket;
    String clientName;

    public ClienHandler(Socket socket, String clientName)
    {
        this.socket = socket;
        this.clientName = clientName;

    }


    @Override
    public void run()
    {

        PrintWriter out = null;

        try
        {
            System.out.println("I'm " + this.clientName);
            out = new PrintWriter(socket.getOutputStream());
            out.println("Hello " + this.clientName);
            out.flush();

            Thread.sleep(10000);
        }
        catch (IOException | InterruptedException e)
        {

            e.printStackTrace();
        }
        finally
        {
            try
            {
                out.close();
                socket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
