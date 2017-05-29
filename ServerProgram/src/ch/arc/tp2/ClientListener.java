package ch.arc.tp2;

import ch.arc.tp2.Packets.Packet;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/*
 * Project Name : ServerProgram
 * author : jonathan.guerne & anthony fleury
 * creation date : 18.05.2017
 * thread use to listen to a specific client message
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

    /**
     * listen to packet from client
     * if a new packet is received send it to the clientDispatcher
     */
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

        //delete client information
        clientInfo.clientSender.interrupt();

        serverDispatcher.deleteClient(clientInfo);

    }
}
