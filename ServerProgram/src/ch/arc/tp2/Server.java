package ch.arc.tp2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Project Name : ServerProgram
 * @author anthony.fleury, guerne.jonathan
 * @date 18.05.2017
 */
public class Server
{


    private static final int port = 52017; //port use by the server


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


        //starting the serverDispatcher
        ServerDispatcher serverDispatcher = new ServerDispatcher();
        serverDispatcher.start();


        //waiting for new client
        while (true){
            try
            {
                //create the socket and add it to a new client info
                Socket socket = serverSocket.accept();
                ClientInfo clientInfo = new ClientInfo();

                clientInfo.socket=socket;

                //create clientSender et clientListener for the new client
                ClientSender clientSender = new ClientSender(serverDispatcher,clientInfo);
                ClientListener clientListener= new ClientListener(serverDispatcher,clientInfo);

                //put clientListener et clientSender into the clientInfo object
                clientInfo.clientSender = clientSender;
                clientInfo.clientListener = clientListener;

                //start clientSender and clientListener
                clientListener.start();
                clientSender.start();

                //add a client to the clientDispatcher list of client
                serverDispatcher.addClient(clientInfo);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }
}
