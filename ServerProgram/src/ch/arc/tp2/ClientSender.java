package ch.arc.tp2;

import java.io.IOException;
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
    PrintWriter out;
    ClientInfo clientInfo;

    private ArrayList<String> messagesQueue;


    public ClientSender(ServerDispatcher serverDispatcher, ClientInfo clientInfo) throws IOException
    {
        this.serverDispatcher = serverDispatcher;
        this.clientInfo = clientInfo;

        Socket socket = clientInfo.socket;

        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

        messagesQueue = new ArrayList<>();
    }


    public synchronized void sendMessage(String message){
        messagesQueue.add(message);
        notify();
    }

    public void sendMessageToClient(String message){
        out.println(message);
        out.flush();
    }

    public synchronized String getNextMessageFromQueue() throws InterruptedException
    {
        while (messagesQueue.size() == 0)
            wait();

        String message = messagesQueue.get(0);
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
                String s = getNextMessageFromQueue();
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
