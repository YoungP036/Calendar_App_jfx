package frontend.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class universal_controller {

    @FXML
    private Button cancel_BTN;
    public void complete_shutdown(){
        Platform.exit();
    }

    @FXML
    private void return_to_main(){
        Stage stage = (Stage)cancel_BTN.getScene().getWindow();
        stage.close();
    }

    public boolean validate_time(String time){
        System.out.println("TODO validate time " + time);
        //TODO CREATE SOME REGEX
        if(true){
            return true;
        }
        else
            return false;
    }
    public boolean validate_minutes(String minutes){
        System.out.println("TODO validate int>0 for " + minutes);
        //TODO CREATE SOME REGEX
        if(true)
                return true;
            else
                return false;
    }
    public void invalid_input_dialogue(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid Input");
        alert.setHeaderText("Input error");
        alert.setContentText("-Times should be HH:MM on a 24 hour clock\n-Lengths should be in minutes and greater then 0");
        alert.showAndWait();
    }

    public static void new_window(AnchorPane screen){
        Stage newStage = new Stage();

        if(screen.getScene()==null){
            Scene scene = new Scene(screen);
            newStage.setScene(scene);
        }
        else
            newStage.setScene(screen.getScene());

        newStage.show();
    }

}
