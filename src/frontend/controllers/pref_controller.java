package frontend.controllers;

import Backend.DataServer;
import Backend.prefsBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

import java.time.LocalTime;

public class pref_controller extends universal_controller{
    @FXML private TextField workStart_TXT,workEnd_TXT;
    @FXML private Button confirm_BTN;
    @FXML CheckBox sun_CHK, mon_CHK, tues_CHK, wed_CHK, thurs_CHK, fri_CHK, sat_CHK;

    @FXML
    private void confirm_pref(){
        prefsBuilder pb = new prefsBuilder();
        boolean[] wdays = new boolean[7];

        if(!validate_time(workStart_TXT.getText())||!validate_time(workEnd_TXT.getText()))
            invalid_input_dialogue("Event times should be HH:MM on a 24 hour clock");
        else {

            pb.setStart(LocalTime.parse(workStart_TXT.getText()));
            pb.setEnd(LocalTime.parse(workEnd_TXT.getText()));
            wdays[0] = sun_CHK.isSelected();
            wdays[1] = mon_CHK.isSelected();
            wdays[2] = tues_CHK.isSelected();
            wdays[3] = wed_CHK.isSelected();
            wdays[4] = thurs_CHK.isSelected();
            wdays[5] = fri_CHK.isSelected();
            wdays[6] = sat_CHK.isSelected();
            pb.setDays(wdays);
            DataServer.savePrefs(pb.createPrefs());

            Stage stage = (Stage) confirm_BTN.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void validate_work_sTime(){
        String time = workStart_TXT.getText();
        if(validate_time(time)){
            System.out.println("Input VALID");
        }
        else
            invalid_input_dialogue("Event times should be HH:MM on a 24 hour clock");
    }
    @FXML
    private void validate_work_eTime(){
        String time = workEnd_TXT.getText();

        if(validate_time(time)){
            System.out.println("Input VALID");
        }
        else
            invalid_input_dialogue("Event times should be HH:MM on a 24 hour clock");
    }
}
