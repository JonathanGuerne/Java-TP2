package ch.arc.tp2.view;

import ch.arc.tp2.MainApp;
import ch.arc.tp2.Packets.TextMessage;
import ch.arc.tp2.Service.ConnexionException;
import ch.arc.tp2.Service.Sender;
import ch.arc.tp2.model.ServerConfig;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;

/**
 * Chatroom
 *
 * @author Anthony Fleury / Jonathan Guerne 
 */
public class ChatController {

    @FXML
    private TextArea ta_chatDisplay;
    
    @FXML
    private TextField tf_message;
    
    @FXML
    private Button bt_send;

    private NetworkService networkService;
    
    
    

    // Reference to the main application.
    private MainApp mainApp;
    private ServerConfig serverConfig;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ChatController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        ta_chatDisplay.setText("- Welcome to ChatBox -\nSet your server config by edit menu.\n");
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }



    
    /**
     * Called when the user clicks on the send button.
     */
    @FXML
    public void handleSendButton() {
        System.out.println("MESSAGE SENT");
        
        //TODO send message to server socket
        System.out.println("Message envoyé par " + serverConfig.getPseudo() + " : " + tf_message.getText());

        TextMessage message = new TextMessage();
        message.author = serverConfig.getPseudo();
        message.message = tf_message.getText();

        networkService.addMessage(message);

        tf_message.clear();

    }

    /**
     * create a new network service connecting the client and the server
     * @param address address of the server
     * @param port port to use
     * @return true if it goes well false otherwise
     */
    public boolean setNetworkService(String address,int port){
        networkService = new NetworkService(address,port);
        try{
            networkService.initSocket();
        }
        catch(ConnexionException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public void setServerConfig(ServerConfig serverConfig)
    {
        this.serverConfig = serverConfig;
    }
    
    public void disableSending(){
        tf_message.setDisable(true);
        bt_send.setDisable(true);
    }
    
    public void enableSending(){
        tf_message.setDisable(true);
        bt_send.setDisable(true);
    }


    /**
     * start the network service use to listen to the server
     */
    public void startNetworkService()
    {
        networkService.start();
    }


    /**
     * append text to the chat area
     * @param message
     */
    public void appendText(String message){
        this.ta_chatDisplay.appendText(message+"\n");
    }

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

        public synchronized void addMessage(TextMessage message){
            System.out.println(Thread.currentThread().getName());
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
                    ObjectInputStream in = null;
                    ObjectOutputStream out = null;

                    try
                    {
                        System.out.println("Client is starting");
                        out = new ObjectOutputStream(socket.getOutputStream());
                        in = new ObjectInputStream(socket.getInputStream());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    System.out.println(Thread.currentThread().getName());

                    System.out.println("---------------");

                    sender = new Sender(out);
                    sender.setDaemon(true);
                    sender.start();

                    try{
                        TextMessage message;
                        while ((message = (TextMessage) in.readObject())!= null)
                        {
                            appendText(message.toString());
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
}