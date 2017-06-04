package ch.arc.tp2;/*
 * Project Name : javaFXMVC
 * author : jonathan.guerne
 * create date : 16.03.2017
*/

import java.io.File;
import java.io.IOException;
import java.util.Optional;


import ch.arc.tp2.view.ServerEditDialogController;
import ch.arc.tp2.model.ServerConfig;
import ch.arc.tp2.view.ChatController;
import ch.arc.tp2.view.RootLayoutController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;


public class MainApp extends Application
{


    private Stage primaryStage;
    private BorderPane rootLayout;
    private RootLayoutController mainController;
    private ChatController chatController;

    private ServerConfig serverConfig;


    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * Constructor
     */
    public MainApp()
    {
        serverConfig = new ServerConfig();
    }


    @Override
    public void start(Stage primaryStage)
    {

        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
        }

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("ChatBox");

        Platform.setImplicitExit(false);

        serverConfig = new ServerConfig();

        System.setProperty("prism.lcdtext", "false");

        initRootLayout();


    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout()
    {
        try
        {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();


            mainController = loader.getController();
            mainController.setDialogStage(primaryStage);
            mainController.setMainApp(this);


            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.getIcons().add(new Image("file:icon.png"));

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
            {
                @Override
                public void handle(WindowEvent event)
                {
                    chatController.stopServices();
                    String folderPath = System.getProperty("user.home") + File.separator + "chatDownloads";
                    File dir = new File(folderPath);
                    if (dir.exists() && dir.list().length > 0)
                    {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION
                                , "Delete files in download directory ("+dir.list().length+" file(s)) ?"
                                , ButtonType.YES, ButtonType.NO,ButtonType.CANCEL);
                        alert.showAndWait();
                        if (alert.getResult() == ButtonType.YES){
                            File[] entries = dir.listFiles();
                            for (File f : entries)
                            {
                                f.delete();
                            }
                        }
                        else if(alert.getResult() == ButtonType.CANCEL){
                            event.consume();
                        }
                    }
                }
            });

            primaryStage.show();

            showChat();

            //Set the server config into the main controller
            mainController.setServerConfig(serverConfig);            //Try to connect to the server

            boolean success = (mainController.connectToServer());
            //Update the displayed info about server config

            if (success)
            {
                chatController.startNetworkService();
            }

            mainController.updateServerInfo();


        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Shows the chat inside the root layout.
     */
    public void showChat()
    {
        try
        {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Chat.fxml"));
            AnchorPane chat = (AnchorPane) loader.load();

            // Set chat into the center of root layout.
            rootLayout.setCenter(chat);


            chatController = loader.getController();
            // Give the controller access to the main app.
            chatController.setMainApp(this);
            chatController.setServerConfig(serverConfig);

            mainController.setChatController(chatController);


        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit the server config. If the user
     * clicks OK, the changes are saved and true
     * is returned.
     *
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showServerEditDialog()
    {
        try
        {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ServerEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Server Configuration");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);


            ServerEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            // Set the serverconfig into the controller.
            controller.setServerConfig(serverConfig);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Returns the main stage.
     *
     * @return primaryStage
     */
    public Stage getPrimaryStage()
    {
        return primaryStage;
    }


    public BorderPane getRootLayout()
    {
        return rootLayout;
    }
}
