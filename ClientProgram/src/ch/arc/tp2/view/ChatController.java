package ch.arc.tp2.view;

import ch.arc.tp2.MainApp;
import ch.arc.tp2.Service.ConnexionException;
import ch.arc.tp2.Service.NetworkService;
import ch.arc.tp2.model.ServerConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
        ta_chatDisplay.setText("- Welcome to ChatBox -\nSet your server config by edit menu.");
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


    public void startNetworkService()
    {
        networkService.start();
    }
}