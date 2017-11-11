package frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import frontend.Main;

public class pref_controller extends universal_controller{
    @FXML private TextField workStart_TXT,workEnd_TXT;

    public void confirm_pref(){
        System.out.println("TODO SAVE PREF");
        Main.set_pane(0);
    }

    @FXML
    public void validate_work_sTime(){
        String time = workStart_TXT.getText();
        if(validate_time(time)){
            System.out.println("Input VALID");
        }
        else
            invalid_input_dialogue();
    }
    @FXML
    public void validate_work_eTime(){
        String time = workEnd_TXT.getText();

        if(validate_time(time)){
            System.out.println("Input VALID");
        }
        else
            invalid_input_dialogue();
    }
}
