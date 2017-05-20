package ch.arc.tp2.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**

 * Nakov Chat Client

 * (c) Svetlin Nakov, 2002

 * http://www.nakov.com

 *

 * Client connects to Nakov Chat Server and prints all the messages

 * received from the server. It also allows the user to send messages to the

 * server. Client thread reads messages and print them to the standard

 * output. Sender thread reads messages from the standard input and sends them

 * to the server.

 */





class Sender extends Thread

{

    private PrintWriter mOut;



    public Sender(PrintWriter aOut)

    {

        mOut = aOut;

    }



    /**

     * Until interrupted reads messages from the standard input (keyboard)

     * and sends them to the chat server through the socket.

     */

    public void run()

    {

        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            while (!isInterrupted()) {

                String message = in.readLine();

                mOut.println(message);

                mOut.flush();

            }

        } catch (IOException ioe) {

            // Communication is broken

        }

    }

}
