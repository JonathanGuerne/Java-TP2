package ch.arc.tp2;

import ch.arc.tp2.Packets.Packet;
import ch.arc.tp2.Packets.TextMessage;

import java.io.*;
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
        System.out.println("in server ok");
    }

    @Override
    public void run()
    {
        try
        {
            while (!isInterrupted())
            {
                Packet message = (Packet) in.readObject();

                if( message == null){
                    break;
                }

                serverDispatcher.dispatchMessage(clientInfo,message);
            }
        }
        catch (EOFException e){
            this.interrupt();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }



        try
        {
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        clientInfo.clientSender.interrupt();

        serverDispatcher.deleteClient(clientInfo);

    }
}
