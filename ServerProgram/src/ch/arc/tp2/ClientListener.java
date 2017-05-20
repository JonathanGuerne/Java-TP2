package ch.arc.tp2;

import ch.arc.tp2.Packets.TextMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;

/*
 * Project Name : ServerProgram
 * author : jonathan.guerne
 * creation date : 18.05.2017
*/
public class ClientListener extends Thread
{
    ServerDispatcher serverDispatcher;
    ClientInfo clientInfo;
    ObjectInputStream in;

    public ClientListener(ServerDispatcher serverDispatcher, ClientInfo clientInfo) throws IOException
    {
        this.serverDispatcher = serverDispatcher;
        this.clientInfo = clientInfo;
        this.in = in;
        Socket socket = clientInfo.socket;
        in = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run()
    {
        try
        {
            while (!isInterrupted())
            {
                TextMessage message = (TextMessage) in.readObject();

                if( message == null){
                    break;
                }

                serverDispatcher.dispatchMessage(clientInfo,message);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }


        clientInfo.clientSender.interrupt();

        serverDispatcher.deleteClient(clientInfo);
    }
}
