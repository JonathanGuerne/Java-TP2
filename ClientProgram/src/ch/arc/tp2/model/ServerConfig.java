package ch.arc.tp2.model;

import java.util.Random;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for server config information
 *
 * @author Anthony Fleury / Jonathan Guerne 
 */
public class ServerConfig
{


    private final StringProperty serverAddress;
    private final IntegerProperty serverPort;
    private final StringProperty pseudo;
    private String connectionStatus;



    /**
     * Default constructor.
     */
    public ServerConfig() {
        //TODO add random number to anonymus pseudo
        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(1000);
        String pseudo = "Anonymous"+String.valueOf(randomNumber);
        
        this.pseudo = new SimpleStringProperty(pseudo);
        this.serverAddress = new SimpleStringProperty("127.0.0.1");
        this.serverPort = new SimpleIntegerProperty(52017);
        this.connectionStatus = "Not verified";
    }

    
    
    public String getServerAddress() {
        return serverAddress.get();
    }

    public void setServerAddress(String address) {
        this.serverAddress.set(address);
    }

    public StringProperty serverAddressProperty() {
        return serverAddress;
    }
    
    public int getServerPort() {
        return serverPort.get();
    }

    public void setServerPort(int port) {
        this.serverPort.set(port);
    }

    public IntegerProperty serverPortProperty() {
        return serverPort;
    }  
    
    public String getPseudo() {
        return pseudo.get();
    }

    public void setPseudo(String pseudo) {
        this.pseudo.set(pseudo);
    }

    public StringProperty pseudoProperty() {
        return pseudo;
    }
    
    public String getConnectionStatus()
    {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus)
    {
        this.connectionStatus = connectionStatus;
    }
    
}
