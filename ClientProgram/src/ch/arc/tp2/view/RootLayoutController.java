package ch.arc.tp2.view;

import ch.arc.tp2.MainApp;
import ch.arc.tp2.model.ServerConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;


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

    @FXML
    private ImageView img_refresh;


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public RootLayoutController()
    {

    }


    /**
     * Try to stop and reconnect to the server
     */
    @FXML
    public void handleRefresh()
    {
        if (serverConfig.getConnectionStatus() != "OK")
        {
            chatController.stopServices();
            boolean success = this.connectToServer();
            if (success)
            {
                chatController.startNetworkService();
            }
            updateServerInfo();
        }
    }


    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp)
    {
        this.mainApp = mainApp;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize()
    {
    }

    /**
     * Sets the stage of the main window.
     *
     * @param stage
     */
    public void setDialogStage(Stage stage)
    {
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
     */
    public void updateServerInfo()
    {
        lb_serverAddress.setText(serverConfig.getServerAddress());
        lb_serverPort.setText(String.valueOf(serverConfig.getServerPort()));
        lb_status.setText(serverConfig.getConnectionStatus());
    }


    /**
     * Called when the user clicks Close menu.
     */
    @FXML
    private void handleCloseMenu()
    {
        stage.fireEvent(
                new WindowEvent(
                        stage,
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        );
    }

    /**
     * Called when the user clicks Server Config menu.
     */
    @FXML
    private void handleServConfigMenu()
    {

        int temp_port = serverConfig.getServerPort();
        String temp_adresse = serverConfig.getServerAddress();
        String temp_pseudo = serverConfig.getPseudo();

        boolean okClicked = mainApp.showServerEditDialog();
        if (okClicked)
        {
            if (serverConfig.getServerPort() != temp_port || serverConfig.getServerAddress() != temp_adresse)
            {
                chatController.stopServices();
                boolean success = this.connectToServer();
                if (success)
                {
                    chatController.startNetworkService();
                }
            }
            updateServerInfo();
        }
    }


    /**
     * Delete the transfered files folder (asks by dialog)
     */
    @FXML
    public void handleDeleteDirContent(){
        String folderPath = System.getProperty("user.home") + File.separator + "chatDownloads";
        File dir = new File(folderPath);
        if (dir.exists() && dir.list().length > 0)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION
                    , "Delete files in download directory ("+dir.list().length+" file(s)) ?"
                    , ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES){
                File[] entries = dir.listFiles();
                for (File f : entries)
                {
                    f.delete();
                }
            }
        }
    }

    /**
     * Called when the user clicks About menu.
     */
    @FXML
    private void handleAboutMenu()
    {
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
     * 
     * @return success of connection (boolean)
     */
    public boolean connectToServer()
    {
        System.out.println("Try to connect to the server : " + serverConfig.getServerAddress() + ":" + serverConfig.getServerPort() + " as " + serverConfig.getPseudo());

        boolean connectSucces = chatController.setNetworkService(serverConfig.getServerAddress(), serverConfig.getServerPort());

        if (!connectSucces)
        {
            System.out.println("CONNECTION ERROR");
            serverConfig.setConnectionStatus("NO");
            chatController.disableSending();
        }
        else
        {
            System.out.println("CONNECTED");
            serverConfig.setConnectionStatus("OK");
            chatController.enableSending();
        }

        return connectSucces;
    }


}
