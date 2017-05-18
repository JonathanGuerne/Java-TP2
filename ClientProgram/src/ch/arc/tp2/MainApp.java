package ch.arc.tp2;/*
 * Project Name : javaFXMVC
 * author : jonathan.guerne
 * create date : 16.03.2017
*/

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class MainApp extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("view/SimpleUI.fxml"));

        Scene scene = new Scene(root,400,400);
        scene.getStylesheets().add(getClass().getResource("view/simpleUI.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
