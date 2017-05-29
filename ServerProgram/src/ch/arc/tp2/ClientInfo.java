package ch.arc.tp2;

import java.net.Socket;

/*
 * Project Name : ServerProgram
 * author : jonathan.guerne & anthony fleury
 * creation date : 18.05.2017
 * store all information about a client
 *  - socket
 *  - clientSender use by the server to send info to the client
 *  - clientListener use to listen to client message
*/
public class ClientInfo
{
    public Socket socket;
    public ClientSender clientSender;
    public ClientListener clientListener;

}
