package ch.arc.tp2.view;

import ch.arc.tp2.MainApp;
import ch.arc.tp2.model.ServerConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Main frame with menu, containing the chatroom stage
 *
 * @author Anthony Fleury / Jonathan Guerne 
 */
public class RootLayoutController
{
    private Stage stage;
    private ChatController chatController;
    private ServerConfig serverConfig;

    private MainApp mainApp;
    
    @FXML
    private Label lb_status_text;
    @FXML
    private Label lb_serverAddress_text;
    @FXML
    private Label lb_serverPort_text;
    
    @FXML
    private Label lb_status;
    @FXML
    private Label lb_serverAddress;
    @FXML
    private Label lb_serverPort;
    
    
    
    
    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public RootLayoutController() {
        
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
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }
    
    /**
     * Sets the stage of the main window.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage stage) {
        this.stage = stage;
    }
    
    /**
     * Give the chat controller to the main controller
     * 
     * @param chatController
     */
    public void setChatController(ChatController chatController)
    {
        this.chatController = chatController;
    }
    
    /**
     * Updates the different information about the server configuration (bottom)
     * 
     * @param address
     * @param port
     */
    public void updateServerInfo(){
        lb_serverAddress.setText(serverConfig.getServerAddress());
        lb_serverPort.setText(String.valueOf(serverConfig.getServerPort()));
        lb_status.setText(serverConfig.getConnectionStatus());
    }
    
    
    /**
     * Called when the user clicks Close menu.
     */
    @FXML
    private void handleCloseMenu() {
        stage.close();
    }
    
    /**
     * Called when the user clicks Server Config menu.
     */
    @FXML
    private void handleServConfigMenu() {


        boolean okClicked = mainApp.showServerEditDialog();
        if (okClicked) {
            this.connectToServer();
            this.updateServerInfo();

        }


    }
    
    /**
     * Called when the user clicks About menu.
     */
    @FXML
    private void handleAboutMenu() {
        // Show the info message.
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(stage);
        alert.setTitle("Information");
        alert.setHeaderText("Information about authors");
        alert.setContentText("by Jonathan Guerne & Anthony Fleury");

        alert.showAndWait();
    }
    
    /**
     * Give the serverconfig object to the main controller
     * 
     * @param serverConfig
     */
    public void setServerConfig(ServerConfig serverConfig)
    {
        this.serverConfig = serverConfig;
    }
    
    /**
     * Try to connect to the server and update the status into the server config
     */
    public void connectToServer(){
        //TODO connection to the server
        System.out.println("Try to connect to the server : " + serverConfig.getServerAddress()+":"+serverConfig.getServerPort()+ " as " + serverConfig.getPseudo());
        
        if(serverConfig.getServerAddress().equals("127.0.0.1") && serverConfig.getServerPort() == 52017){
            System.out.println("CONNECTED");
            serverConfig.setConnectionStatus("OK");
        }
        else{
            System.out.println("CONNECTION ERROR");
            serverConfig.setConnectionStatus("NO");
        }
    
    }
    
    
    
}
