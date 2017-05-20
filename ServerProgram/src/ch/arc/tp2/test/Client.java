package ch.arc.tp2.test;

import java.io.*;
import java.net.Socket;

public class Client

{

    public static final String SERVER_HOSTNAME = "localhost";

    public static final int SERVER_PORT = 2002;



    public static void main(String[] args)

    {

        BufferedReader in = null;

        PrintWriter out = null;

        try {

            // Connect to Nakov Chat Server

            Socket socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);

            in = new BufferedReader(

                    new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(

                    new OutputStreamWriter(socket.getOutputStream()));

            System.out.println("Connected to server " +

                    SERVER_HOSTNAME + ":" + SERVER_PORT);

        } catch (IOException ioe) {

            System.err.println("Can not establish connection to " +

                    SERVER_HOSTNAME + ":" + SERVER_PORT);

            ioe.printStackTrace();

            System.exit(-1);

        }



        // Create and start Sender thread

        Sender sender = new Sender(out);

        sender.setDaemon(true);

        sender.start();



        try {

            // Read messages from the server and print them

            String message;

            while ((message=in.readLine()) != null) {

                System.out.println(message);

            }

        } catch (IOException ioe) {

            System.err.println("Connection to server broken.");

            ioe.printStackTrace();

        }



    }

}
