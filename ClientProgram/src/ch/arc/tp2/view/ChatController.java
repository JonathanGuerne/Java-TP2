package ch.arc.tp2.view;

import ch.arc.tp2.MainApp;
import ch.arc.tp2.Packets.FileMessage;
import ch.arc.tp2.Packets.Packet;
import ch.arc.tp2.Packets.TextMessage;
import ch.arc.tp2.Service.ConnexionException;
import ch.arc.tp2.Service.Sender;
import ch.arc.tp2.model.ServerConfig;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Optional;

/**
 * Chatroom
 *
 * @author Anthony Fleury / Jonathan Guerne
 */
public class ChatController
{

    @FXML
    private TextArea ta_chatDisplay;

    @FXML
    private TextField tf_message;

    @FXML
    private Button bt_send;

    @FXML
    private Button bt_sendFile;

    private NetworkService networkService;


    // Reference to the main application.
    private MainApp mainApp;
    private ServerConfig serverConfig;

    private Desktop desktop = Desktop.getDesktop();

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ChatController()
    {

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize()
    {
        ta_chatDisplay.setText("- Welcome to ChatBox -\nSet your server config by edit menu.\n");
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
     * Called when the user clicks on the send button.
     */
    @FXML
    public void handleSendButton()
    {
        System.out.println("MESSAGE SENT");

        //TODO send message to server socket
        System.out.println("Message envoyé par " + serverConfig.getPseudo() + " : " + tf_message.getText());

        TextMessage message = new TextMessage();
        message.author = serverConfig.getPseudo();
        message.message = tf_message.getText();

        networkService.addMessage(message);

        tf_message.clear();

    }

    @FXML
    public void handleSendFileButton()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a file to send");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );

        File fileToSend = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (fileToSend != null)
        {

            FileMessage fm = new FileMessage();

            byte[] mybytearray = new byte[(int) fileToSend.length()];
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileToSend)))
            {
                bis.read(mybytearray, 0, mybytearray.length);

                fm.content = mybytearray;
                fm.filename = fileToSend.getName();

                fm.author = serverConfig.getPseudo();
                networkService.addMessage(fm);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    /**
     * create a new network service connecting the client and the server
     *
     * @param address address of the server
     * @param port    port to use
     * @return true if it goes well false otherwise
     */
    public boolean setNetworkService(String address, int port)
    {
        networkService = new NetworkService(address, port);

        boolean success;
        try
        {
            success = networkService.initSocket();
        }
        catch (ConnexionException e)
        {
            e.printStackTrace();
            return false;
        }

        return success;
    }


    public void setServerConfig(ServerConfig serverConfig)
    {
        this.serverConfig = serverConfig;
    }

    public void disableSending()
    {
        tf_message.setDisable(true);
        bt_send.setDisable(true);
    }

    public void enableSending()
    {
        tf_message.setDisable(false);
        bt_send.setDisable(false);
    }


    /**
     * start the network service use to listen to the server
     */
    public void startNetworkService()
    {
        networkService.start();
    }

    public synchronized void showDownloadFileDialog(FileMessage fileMessage)
    {

        String folderPath = System.getProperty("user.home") + File.separator + "chatDownloads";
        String newFilePath = folderPath + File.separator + fileMessage.filename;

        boolean success = (new File(folderPath)).mkdirs();
        if (success)
        {
            ta_chatDisplay.appendText("Directory created " + folderPath + "\n");
        }

        File newFile = new File(newFilePath);
        try
        {
            newFile.createNewFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFilePath)))
        {
            bos.write(fileMessage.content, 0, fileMessage.content.length);
            bos.flush();
            ta_chatDisplay.appendText("File " + newFilePath + " downloaded (" + fileMessage.content.length + " bytes)\n");
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if(newFile.exists()){
            try
            {
                desktop.open(newFile);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    /**
     * append text to the chat area
     *
     * @param message
     */
    public synchronized void appendText(String message)
    {
        this.ta_chatDisplay.appendText(message + "\n");
    }

    public void stopServices()
    {
        if (networkService != null)
        {
            networkService.stopService();
        }
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

        public synchronized void addMessage(Packet message)
        {
            System.out.println(Thread.currentThread().getName());
            sender.addMessage(message);
        }

        public boolean initSocket() throws ConnexionException
        {
            try
            {
                socket = new Socket(adresse, port);
                return socket.isConnected();
            }
            catch (IOException e)
            {
                //e.printStackTrace();
                throw new ConnexionException("unable to connect to the server");
            }
        }

        public synchronized void stopService()
        {
            try
            {
                if (socket != null && !socket.isClosed())
                {
                    socket.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            this.cancel();
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
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    System.out.println(Thread.currentThread().getName());

                    System.out.println("---------------");

                    sender = new Sender(out);
                    sender.setDaemon(true);
                    sender.start();

                    try
                    {
                        Packet message;
                        while (!socket.isClosed() && (message = (Packet) in.readObject()) != null)
                        {
                            appendText(message.toString());
                            if (message instanceof FileMessage)
                            {
                                showDownloadFileDialog((FileMessage) message);
                            }
                        }
                    }
                    catch (SocketException e){
                        System.out.println("socket closed");
                        in.close();
                        out.close();
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