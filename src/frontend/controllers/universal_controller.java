package frontend.controllers;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import static frontend.Main.set_pane;

public class universal_controller {

    public void complete_shutdown(){
        Platform.exit();
    }
    public void return_to_main(){
        set_pane(0);
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
}
