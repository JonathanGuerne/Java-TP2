package ch.arc.tp2.Service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * Project Name : ClientProgram
 * author : jonathan.guerne
 * creation date : 20.05.2017
*/
public class NetworkService extends Service<Integer>
{
    private int port;
    private String adresse;
    private Socket socket;
    private Sender sender;

    public NetworkService(String adresse, int port)
    {
        this.port = port;
        this.adresse = adresse;
    }

    public synchronized void addMessage(String message){
        sender.addMessage(message);
    }

    public void initSocket() throws ConnexionException{
        try
        {
            socket = new Socket(adresse, port);
        }
        catch (IOException e)
        {
            //e.printStackTrace();
            throw new ConnexionException("unable to connect to the server");
        }
    }


    @Override
    protected Task<Integer> createTask()
    {
        return new Task<Integer>()
        {
            @Override
            protected Integer call() throws Exception
            {
                BufferedReader in = null;
                PrintWriter out = null;

                try
                {
                    System.out.println("Client is starting");
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream());

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    throw new ConnexionException("Unable to connect to the server");
                }


                sender = new Sender(out);
                sender.setDaemon(true);
                sender.start();

                try{
                    String message;

                    while ((message = in.readLine())!= null){
                        System.out.println("RECIVED FROM SERVER : "+message);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    throw new ConnexionException("problem during data reading");
                }

                return 0;
            }
        };
    }
}
