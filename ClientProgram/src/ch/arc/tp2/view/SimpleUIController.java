package ch.arc.tp2.view;/*
 * Project Name : javaFXMVC
 * author : jonathan.guerne
 * creation date : 16.03.2017
*/

import ch.arc.tp2.model.SimpleUIModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SimpleUIController
{
    SimpleUIModel model;

    @FXML
    Button myButton;

    @FXML
    Label myLabel;

    @FXML
    void handleButtonClick(ActionEvent evt){
        System.out.println("click !");
        model.setLabel("hello");
    }

    @FXML
    private void initialize(){
        model = new SimpleUIModel("Hello from the controller");

        myLabel.textProperty().bindBidirectional(model.labelProperty());
    }
}
