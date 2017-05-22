package ch.arc.tp2;

import ch.arc.tp2.Packets.TextMessage;

import java.util.ArrayList;

/*
 * Project Name : ServerProgram
 * author : jonathan.guerne
 * creation date : 18.05.2017
*/
public class ServerDispatcher extends Thread
{

    private ArrayList<TextMessage> messageQueue;
    private ArrayList<ClientInfo> clients;

    public ServerDispatcher()
    {
        messageQueue = new ArrayList<>();
        clients = new ArrayList<>();
    }

    public synchronized void addClient(ClientInfo clientInfo){
        clients.add(clientInfo);
    }

    @Override
    public void run()
    {
        try {

            while (true) {

                TextMessage message = getNextMessageFromQueue();

                sendMessageToAllClient(message);

            }

        } catch (InterruptedException ie) {

            // Thread interrupted. Stop its execution

        }

    }

    public synchronized void dispatchMessage(ClientInfo clientInfo, TextMessage message)
    {
        messageQueue.add(message);
        notify();
    }

    private synchronized TextMessage getNextMessageFromQueue()

            throws InterruptedException

    {

        while (messageQueue.size()==0)

            wait();

        TextMessage message = (TextMessage) messageQueue.get(0);

        messageQueue.remove(0);

        return message;

    }


    public synchronized void sendMessageToAllClient(TextMessage message){
        for(ClientInfo c:clients){
            c.clientSender.sendMessage(message);
        }
    }


    public synchronized void deleteClient(ClientInfo clientInfo)
    {
        int index = clients.indexOf(clientInfo);
        if(index != -1){
            messageQueue.remove(index);
        }
    }
}
