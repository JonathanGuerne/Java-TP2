package ch.arc.tp2.view;

import ch.arc.tp2.MainApp;
import ch.arc.tp2.Service.ConnexionException;
import ch.arc.tp2.Service.Sender;
import ch.arc.tp2.model.ServerConfig;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

        networkService.addMessage(tf_message.getText());
        
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
                    }


                    sender = new Sender(out);
                    sender.setDaemon(true);
                    sender.start();

                    try{
                        String message;

                        while ((message = in.readLine())!= null){
                            appendText(message);
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