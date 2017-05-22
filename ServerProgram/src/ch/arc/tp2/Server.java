package ch.arc.tp2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Project Name : ServerProgram
 * author : jonathan.guerne
 * creation date : 18.05.2017
*/
public class Server
{


    private static final int port = 52017;


    public static void main(String[] args)
    {
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(port);
            System.out.println("Server is ready");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        ServerDispatcher serverDispatcher = new ServerDispatcher();
        serverDispatcher.start();


        while (true){
            try
            {
                Socket socket = serverSocket.accept();
                ClientInfo clientInfo = new ClientInfo();

                clientInfo.socket=socket;

                ClientSender clientSender = new ClientSender(serverDispatcher,clientInfo);
                ClientListener clientListener= new ClientListener(serverDispatcher,clientInfo);


                clientInfo.clientSender = clientSender;
                clientInfo.clientListener = clientListener;

                clientListener.start();
                clientSender.start();

                serverDispatcher.addClient(clientInfo);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }
}
