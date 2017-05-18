package ch.arc.tp2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Project Name : ServerProgram
 * author : jonathan.guerne
 * creation date : 18.05.2017
*/
public class main
{
    private static String clientName = "Client_";
    private static int clientNumber = 0;

    private static final int port = 52017;


    public static void main(String[] args)
    {
        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("My workers are ready");

            while (true){
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new ClienHandler(socket,clientName+clientNumber));
                thread.start();


                clientNumber++;


            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
