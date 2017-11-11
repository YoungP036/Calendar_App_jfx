package frontend.controllers;

import frontend.Main;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class edit_controller extends universal_controller{
    @FXML private TextField sTime_TXT, eTime_TXT;
    public void validate_sTime(){
        String time=sTime_TXT.getText();
        if(validate_time(time)) {
            System.out.println("Input VALID");
            //TODO DO STUFF
        }
        else
            invalid_input_dialogue();
    }
    public void validate_eTime(){
        String time=eTime_TXT.getText();
        if(validate_time(time)){
            System.out.println("Input VALID");
            //TODO DO STUFF
        }
        else
            invalid_input_dialogue();
    }
    public void confirm_changes(){
        System.out.println("TODO SAVE CHANGES");
        Main.set_pane(0);
    }
}
