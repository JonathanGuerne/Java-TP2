package ch.arc.tp2;

import ch.arc.tp2.Packets.Packet;
import ch.arc.tp2.Packets.TextMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/*
 * Project Name : ServerProgram
 * author : jonathan.guerne
 * creation date : 18.05.2017
*/
public class ClientSender extends Thread
{
    ServerDispatcher serverDispatcher;
    ObjectOutputStream out;
    ClientInfo clientInfo;

    private ArrayList<Packet> messagesQueue;


    public ClientSender(ServerDispatcher serverDispatcher, ClientInfo clientInfo) throws IOException
    {
        this.serverDispatcher = serverDispatcher;
        this.clientInfo = clientInfo;

        Socket socket = clientInfo.socket;

        out = new ObjectOutputStream(socket.getOutputStream());

        messagesQueue = new ArrayList<>();

        System.out.println("out server ok");
    }


    public synchronized void sendMessage(Packet message)
    {
        messagesQueue.add(message);
        notify();
    }

    public void sendMessageToClient(Packet message)
    {
        try
        {
            out.writeObject(message);
            out.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public synchronized Packet getNextMessageFromQueue() throws InterruptedException
    {
        while (messagesQueue.size() == 0)
            wait();
        Packet message = messagesQueue.get(0);
        messagesQueue.remove(0);
        return message;
    }


    @Override
    public void run()
    {
        try
        {
            while (!isInterrupted())
            {
                Packet s = getNextMessageFromQueue();
                sendMessageToClient(s);
            }
        }
        catch (InterruptedException e)
        {

        }

        try
        {
            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        clientInfo.clientListener.interrupt();
        serverDispatcher.deleteClient(clientInfo);

    }
}
