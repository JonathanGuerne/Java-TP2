package ch.arc.tp2;

import ch.arc.tp2.Packets.Packet;
import ch.arc.tp2.Packets.TextMessage;

import java.io.IOException;
import java.util.ArrayList;

/*
 * Project Name : ServerProgram
 * author : jonathan.guerne
 * creation date : 18.05.2017
*/
public class ServerDispatcher extends Thread
{

    private ArrayList<Packet> messageQueue;
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

                Packet message = getNextMessageFromQueue();

                sendMessageToAllClient(message);

            }

        } catch (InterruptedException ie) {

            // Thread interrupted. Stop its execution

        }

    }

    public synchronized void dispatchMessage(ClientInfo clientInfo, Packet message)
    {
        messageQueue.add(message);
        notify();
    }

    private synchronized Packet getNextMessageFromQueue()

            throws InterruptedException

    {

        while (messageQueue.size()==0)

            wait();

        Packet message = (Packet) messageQueue.get(0);

        messageQueue.remove(0);

        return message;

    }


    public synchronized void sendMessageToAllClient(Packet message){
        for(ClientInfo c:clients){
            c.clientSender.sendMessage(message);
        }
    }


    public synchronized void deleteClient(ClientInfo clientInfo)
    {
        int index = clients.indexOf(clientInfo);
        if(index != -1){
            try
            {
                clients.get(index).socket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            clients.remove(index);
        }
    }
}
