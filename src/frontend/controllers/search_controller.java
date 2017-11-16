package frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class search_controller extends universal_controller{
    @FXML private TextField length_TXT;

    @FXML
    private void validate(){
        String mins=length_TXT.getText();
        if(validate_minutes(mins)){
            System.out.println("Input VALID");
        }
        else
            invalid_input_dialogue();
    }
    @FXML
    private void search(){
        String length= length_TXT.getText();
        System.out.println("TODO Search opening for " + length + " minute Event");
    }
}
