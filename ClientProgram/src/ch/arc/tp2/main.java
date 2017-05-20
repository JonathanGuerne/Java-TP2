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

    private static int port = 2002;
    private static String address = "127.0.0.1";

    public static void main(String[] args)
    {
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try
        {
            System.out.println("Client is starting");
            socket = new Socket("127.0.0.1", port);

            System.out.println("CLIENT connecting to the server");

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        Sender sender = new Sender(out);
        sender.setDaemon(true);
        sender.start();

        try{
            String message;

            while ((message = in.readLine())!= null){
                System.out.println(message);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
