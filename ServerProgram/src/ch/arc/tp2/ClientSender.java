package ch.arc.tp2;

import ch.arc.tp2.Packets.Packet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


/**
 * Project Name : ServerProgram
 * @author anthony.fleury, guerne.jonathan
 * @date 18.05.2017
 * 
 * Thread use to send information to the client message are coming from the clientDispatcher
 */
public class ClientSender extends Thread
{
    ServerDispatcher serverDispatcher;
    ObjectOutputStream out;
    ClientInfo clientInfo;

    private ArrayList<Packet> messagesQueue; //list of message to send

    /**
     * Constructor
     * @param serverDispatcher
     * @param clientInfo
     * @throws IOException
     */
    public ClientSender(ServerDispatcher serverDispatcher, ClientInfo clientInfo) throws IOException
    {
        this.serverDispatcher = serverDispatcher;
        this.clientInfo = clientInfo;

        Socket socket = clientInfo.socket;

        out = new ObjectOutputStream(socket.getOutputStream());

        messagesQueue = new ArrayList<>();

        System.out.println("out server ok");
    }


    /**
     * call by the clientDispatcher
     * will add a message to the list of message to send
     * @param message packet to send
     */
    public synchronized void sendMessage(Packet message)
    {
        messagesQueue.add(message);
        notify();
    }

    /**
     * Sends the given message to clients
     * @param message
     */
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

    /**
     * Gets the next message from the message queue and return it
     * @return Message
     * @throws InterruptedException
     */
    public synchronized Packet getNextMessageFromQueue() throws InterruptedException
    {
        while (messagesQueue.size() == 0) //if there's no message the thread will wait
            wait();
        Packet message = messagesQueue.get(0);
        messagesQueue.remove(0);
        return message; //send and remove the first message of the list
    }


    @Override
    public void run()
    {
        try
        {
            while (!isInterrupted())
            {
                //get a new message in the list of message to send (will wait if empty)
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
