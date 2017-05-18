package ch.arc.tp2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * Project Name : ClientProgram
 * author : jonathan.guerne
 * creation date : 18.05.2017
*/
public class main
{

    private static final int port = 52017;

    public static void main(String[] args)
    {
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try
        {
            System.out.println("Client is starting");
            socket = new Socket("127.0.0.1", port);

            System.out.println("CLIENT reading from server");

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("CLIENT - from server : " + in.readLine());


            out = new PrintWriter(socket.getOutputStream());
            out.println("Je suis un client Heureux");

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                out.flush();

                in.close();
                socket.close();
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
