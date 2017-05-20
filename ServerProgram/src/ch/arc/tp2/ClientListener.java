package ch.arc.tp2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    BufferedReader in;

    public ClientListener(ServerDispatcher serverDispatcher, ClientInfo clientInfo) throws IOException
    {
        this.serverDispatcher = serverDispatcher;
        this.clientInfo = clientInfo;
        this.in = in;
        Socket socket = clientInfo.socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run()
    {
        try
        {
            while (!isInterrupted())
            {
                String message = in.readLine();

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


        clientInfo.clientSender.interrupt();

        serverDispatcher.deleteClient(clientInfo);
    }
}
