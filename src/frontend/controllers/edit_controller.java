package frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class edit_controller extends universal_controller{
    @FXML private TextField sTime_TXT, eTime_TXT;
    @FXML private Button confirm_BTN,cancel_BTN;

    @FXML
    private void validate_sTime(){
        String time=sTime_TXT.getText();
        if(validate_time(time)) {
            System.out.println("Input VALID");
            //TODO DO STUFF
        }
        else
            invalid_input_dialogue();
    }
    @FXML
    private void validate_eTime(){
        String time=eTime_TXT.getText();
        if(validate_time(time)){
            System.out.println("Input VALID");
            //TODO DO STUFF
        }
        else
            invalid_input_dialogue();
    }
    @FXML
    private void confirm_changes(){
        System.out.println("TODO SAVE EVENT");
        Stage stage=(Stage) confirm_BTN.getScene().getWindow();
        stage.close();
    }
}
