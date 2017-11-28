package frontend.controllers;

import Backend.Event;
import Backend.eventBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import static Backend.DataServer.saveEvent;
import static frontend.controllers.main_controller.updateIndicators;

public class add_controller extends universal_controller{
    @FXML private TextField name_TXT, loc_TXT, sTime_TXT, eTime_TXT;
    @FXML private TextArea desc_TXT;
    @FXML private DatePicker start_DATE, end_DATE;
    @FXML private Button confirm_BTN;
    @FXML private CheckBox type_CHECK;

    @FXML
    public void initialize(){
        name_TXT.setText("");
        loc_TXT.setText("");
        desc_TXT.setText("");
        sTime_TXT.setText("");
        eTime_TXT.setText("");
        sTime_TXT.setPromptText("HH:MM");
        eTime_TXT.setPromptText("HH:MM");
        type_CHECK.selectedProperty().setValue(false);
    }
    @FXML
    private void validate_sTime(){
        String time=sTime_TXT.getText();
        if(!validate_time(time))
            invalid_input_dialogue();
    }
    @FXML
    private void validate_eTime(){
        String time=eTime_TXT.getText();
        if(!validate_time(time))
            invalid_input_dialogue();
    }
    @FXML
    private void confirm_changes(){
        try {
            eventBuilder eb = new eventBuilder();
            eb.setType(type_CHECK.isSelected());
            eb.setDesc(desc_TXT.getText());
            eb.setLoc(loc_TXT.getText());
            eb.setName(name_TXT.getText());
            eb.setsDay(LocalDate.parse(start_DATE.getValue().toString()));
            eb.seteDay(LocalDate.parse(end_DATE.getValue().toString()));
            eb.setsTime(LocalTime.parse(sTime_TXT.getText()));
            eb.seteTime(LocalTime.parse(eTime_TXT.getText()));
            Event e = eb.createEvent();
            saveEvent(e);
            updateIndicators();
        }
        catch(Exception e){
            System.out.println("Invalid input: " + e.getMessage());
        }
        finally {
            Stage stage = (Stage) confirm_BTN.getScene().getWindow();
            stage.close();
        }
    }


}
