package ch.arc.tp2;

import java.util.ArrayList;

/*
 * Project Name : ServerProgram
 * author : jonathan.guerne
 * creation date : 18.05.2017
*/
public class ServerDispatcher extends Thread
{

    private ArrayList<String> messageQueue;
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

                String message = getNextMessageFromQueue();

                sendMessageToAllClient(message);

            }

        } catch (InterruptedException ie) {

            // Thread interrupted. Stop its execution

        }

    }

    public synchronized void dispatchMessage(ClientInfo clientInfo, String message)
    {
        messageQueue.add(message);
        notify();
    }

    private synchronized String getNextMessageFromQueue()

            throws InterruptedException

    {

        while (messageQueue.size()==0)

            wait();

        String message = (String) messageQueue.get(0);

        messageQueue.remove(0);

        return message;

    }


    public synchronized void sendMessageToAllClient(String message){
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
