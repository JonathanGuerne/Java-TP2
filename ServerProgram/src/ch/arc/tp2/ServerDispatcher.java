package ch.arc.tp2;

import ch.arc.tp2.Packets.Packet;
import ch.arc.tp2.Packets.TextMessage;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Project Name : ServerProgram
 * @author anthony.fleury & guerne.jonathan
 * @date reation date : 18.05.2017
 * @description class use to manage all clients
 */
public class ServerDispatcher extends Thread
{

    private ArrayList<Packet> messageQueue; //list of message to send
    private ArrayList<ClientInfo> clients; //list of clients

    /**
     * Constructor
     */
    public ServerDispatcher()
    {
        messageQueue = new ArrayList<>();
        clients = new ArrayList<>();
    }

    /**
     * Adds a client to the server dispatcher
     * @param clientInfo
     */
    public synchronized void addClient(ClientInfo clientInfo)
    {
        clients.add(clientInfo);
    }

    @Override
    public void run()
    {
        try
        {

            while (true)
            {

                //get a new message to send (will wait if list is empty)
                Packet message = getNextMessageFromQueue();

                sendMessageToAllClient(message);

            }

        }
        catch (InterruptedException ie)
        {

        }

    }

    /**
     * Adds the given message to the message queue to be dispatched
     * @param clientInfo
     * @param message
     */
    public synchronized void dispatchMessage(ClientInfo clientInfo, Packet message)
    {
        messageQueue.add(message);
        notify();
    }


    /**
     * get the next message to send from the list of message
     * if there is not message in the list the thread will wait
     * @return Packet object to send
     * @throws InterruptedException
     */
    private synchronized Packet getNextMessageFromQueue() throws InterruptedException
    {

        while (messageQueue.size() == 0)

            wait();

        Packet message = (Packet) messageQueue.get(0);

        messageQueue.remove(0);

        return message;

    }


    /**
     * will send a message to all connected client
     * @param message packet to send
     */
    public synchronized void sendMessageToAllClient(Packet message)
    {
        for (ClientInfo c : clients)
        {
            c.clientSender.sendMessage(message);
        }
    }


    /**
     * remove a client from the list
     * @param clientInfo client to remove
     */
    public synchronized void deleteClient(ClientInfo clientInfo)
    {
        int index = clients.indexOf(clientInfo);
        if (index != -1)
        {
            try
            {
                clients.get(index).socket.close(); //close client specific socket
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            clients.remove(index);
        }
    }
}
