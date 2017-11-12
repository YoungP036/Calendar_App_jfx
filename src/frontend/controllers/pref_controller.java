package frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

public class pref_controller extends universal_controller{
    @FXML private TextField workStart_TXT,workEnd_TXT;
    @FXML private Button confirm_BTN;

    @FXML
    private void confirm_pref(){
        System.out.println("TODO SAVE PREF");
        Stage stage = (Stage)confirm_BTN.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void validate_work_sTime(){
        String time = workStart_TXT.getText();
        if(validate_time(time)){
            System.out.println("Input VALID");
        }
        else
            invalid_input_dialogue();
    }
    @FXML
    private void validate_work_eTime(){
        String time = workEnd_TXT.getText();

        if(validate_time(time)){
            System.out.println("Input VALID");
        }
        else
            invalid_input_dialogue();
    }
}
