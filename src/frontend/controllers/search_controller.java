package frontend.controllers;

import Backend.DataServer;
import Backend.Event;
import Backend.userPrefs;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.lang.reflect.Array;
import java.time.LocalTime;

public class search_controller extends universal_controller{
    @FXML private TextField length_TXT;
    @FXML private CheckBox work_CHK, personal_CHK, all_CHK;
//    private boolean[][] search_results = new boolean[7][24];
    @FXML
    private void validate(){
        String mins=length_TXT.getText();
        if(validate_minutes(mins)){
            System.out.println("Input VALID");
        }
        else
            invalid_input_dialogue();
    }

    //does not take into account sleep, sleep is for the weak
    @FXML
    private void search(){
        boolean[][] search_results= new boolean[7][24];
        int type=0;
        int length= Integer.parseInt(length_TXT.getText());

        if(work_CHK.isSelected())
            type=1;
        if(personal_CHK.isSelected())
            type=2;
        if(all_CHK.isSelected())
            type=3;
        search_results=eliminate_by_eType(search_results, type);
        System.out.println("TODO Search opening for " + length + " minute Event");
    }

    private boolean[][] eliminate_by_eType(boolean[][] results, int type){

        return results;
    }

}
