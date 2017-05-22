package ch.arc.tp2;

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

    private ArrayList<TextMessage> messagesQueue;


    public ClientSender(ServerDispatcher serverDispatcher, ClientInfo clientInfo) throws IOException
    {
        this.serverDispatcher = serverDispatcher;
        this.clientInfo = clientInfo;

        Socket socket = clientInfo.socket;

        out = new ObjectOutputStream(socket.getOutputStream());

        messagesQueue = new ArrayList<>();

        System.out.println("out server ok");
    }


    public synchronized void sendMessage(TextMessage message){
        messagesQueue.add(message);
        notify();
    }

    public void sendMessageToClient(TextMessage message){
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

    public synchronized TextMessage getNextMessageFromQueue() throws InterruptedException
    {
        while (messagesQueue.size() == 0)
            wait();

        TextMessage message = messagesQueue.get(0);
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
                TextMessage s = getNextMessageFromQueue();
                sendMessageToClient(s);
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        clientInfo.clientListener.interrupt();
        serverDispatcher.deleteClient(clientInfo);
    }
}
