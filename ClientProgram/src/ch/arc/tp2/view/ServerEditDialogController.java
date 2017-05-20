package ch.arc.tp2.view;



import ch.arc.tp2.model.ServerConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;


/**
 * Dialog to edit the server configuration.
 *
 * @author Anthony Fleury / Jonathan Guerne 
 */
public class ServerEditDialogController {
    
    @FXML
    private TextField tf_pseudo;
    @FXML
    private TextField tf_serverAddress;
    @FXML
    private TextField tf_serverPort;
    
    @FXML
    private Label lb_pseudo;
    @FXML
    private Label lb_serverAddress;
    @FXML
    private Label lb_serverPort;
    
    private Stage dialogStage;
    private boolean okClicked = false;
    private ServerConfig serverConfig;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the ServerConfig to be edited in the dialog.
     *
     * @param serverConfig
     */
    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        
        tf_pseudo.setText(serverConfig.getPseudo());
        tf_serverAddress.setText(serverConfig.getServerAddress());
        tf_serverPort.setText(String.valueOf(serverConfig.getServerPort()));
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            serverConfig.setPseudo(tf_pseudo.getText());
            serverConfig.setServerAddress(tf_serverAddress.getText());
            serverConfig.setServerPort(Integer.parseInt(tf_serverPort.getText()));

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";
        
        if (tf_pseudo.getText() == null || tf_pseudo.getText().length() == 0) {
            errorMessage += "No valid pseudo!\n";
        }
        if(!validIP(tf_serverAddress.getText())){
            errorMessage += "No valid IP Address!\n";
        }
        if (tf_serverPort.getText() == null || tf_serverPort.getText().length() == 0) {
            errorMessage += "No valid port!\n";
        } else {
            // try to parse the port code into an int.
            try {
                Integer.parseInt(tf_serverPort.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid port (must be an integer)!\n";
            }
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
    
    //Method to validate a string containing an ip address formated like 127.0.0.1
    private static boolean validIP (String ip) {
        try {
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    
}