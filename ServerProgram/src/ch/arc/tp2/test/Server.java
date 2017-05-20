package ch.arc.tp2.test; /**

 * Nakov Chat Server

 * (c) Svetlin Nakov, 2002

 * http://www.nakov.com

 *

 * Nakov Chat Server is multithreaded chat server. It accepts multiple clients

 * simultaneously and serves them. Clients can send messages to the server.

 * When some client send a message to the server, this message is dispatched

 * to all the clients connected to the server.

 *

 * The server consists of two components - "server core" and "client handlers".

 *

 * The "server core" consists of two threads:

 *   - Server - accepts client connections, creates client threads to

 * handle them and starts these threads

 *   - ch.arc.tp2.test.ServerDispatcher - waits for a messages and sends arrived messages to

 * all the clients connected to the server

 *

 * The "client handlers" consist of two threads:

 *   - ch.arc.tp2.test.ClientListener - listens for message arrivals from the socket and

 * forwards them to the ch.arc.tp2.test.ServerDispatcher thread

 *   - ch.arc.tp2.test.ClientSender - sends messages to the client

 *

 * For each accepted client, a ch.arc.tp2.test.ClientListener and ch.arc.tp2.test.ClientSender threads are

 * created and started. A ch.arc.tp2.test.ClientInfo object is also created to contain the

 * information about the client. Also the ch.arc.tp2.test.ClientInfo object is added to the

 * ch.arc.tp2.test.ServerDispatcher's clients list. When some client is disconnected, is it

 * removed from the clients list and both its ch.arc.tp2.test.ClientListener and ch.arc.tp2.test.ClientSender

 * threads are interrupted.

 *

 *

 * Server class is entry point for the program. It opens a server

 * socket, starts the dispatcher thread and infinitely accepts client connections,

 * creates threads for handling them and starts these threads.

 */


import java.net.*;
import java.io.*;



public class Server

{

    public static final int LISTENING_PORT = 2002;



    public static void main(String[] args)

    {

        // Open server socket for listening

        ServerSocket serverSocket = null;

        try {

            serverSocket = new ServerSocket(LISTENING_PORT);

            System.out.println("Server started on port " + LISTENING_PORT);

        } catch (IOException se) {

            System.err.println("Can not start listening on port " + LISTENING_PORT);

            se.printStackTrace();

            System.exit(-1);

        }



        // Start ch.arc.tp2.test.ServerDispatcher thread

        ServerDispatcher serverDispatcher = new ServerDispatcher();

        serverDispatcher.start();



        // Accept and handle client connections

        while (true) {

            try {

                Socket socket = serverSocket.accept();

                ClientInfo clientInfo = new ClientInfo();

                clientInfo.mSocket = socket;

                ClientListener clientListener = new ClientListener(clientInfo, serverDispatcher);

                ClientSender clientSender = new ClientSender(clientInfo, serverDispatcher);

                clientInfo.clientListener = clientListener;

                clientInfo.clientSender = clientSender;

                clientListener.start();

                clientSender.start();

                serverDispatcher.addClient(clientInfo);

            } catch (IOException ioe) {

                ioe.printStackTrace();

            }

        }

    }



}





