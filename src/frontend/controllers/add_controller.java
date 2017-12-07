package frontend.controllers;

import Backend.DataServer;
import Backend.Event;
import Backend.eventBuilder;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalTime;

import static Backend.DataServer.saveEvent;
import static frontend.controllers.main_controller.updateIndicators;

public class add_controller extends universal_controller{


//    @FXML private static TextField name_TXT, loc_TXT, sTime_TXT, eTime_TXT;
//    @FXML private static TextArea desc_TXT;
//    @FXML private static DatePicker start_DATE, end_DATE;
//    @FXML private Button confirm_BTN;
//    @FXML private static CheckBox type_CHECK;
    @FXML private  TextField name_TXT, loc_TXT, sTime_TXT, eTime_TXT;
    @FXML private  TextArea desc_TXT;
    @FXML private  DatePicker start_DATE, end_DATE;
    @FXML private Button confirm_BTN;
    @FXML private  CheckBox type_CHECK;
    @FXML ListView events_LST;
//    @FXML
//    public void initialize(){
////        setName_TXT("");
////        setLoc_TXT("");
////        setDesc_TXT("");
////        setsTime_TXT("");
//        name_TXT.setText("");
//        loc_TXT.setText("");
//        desc_TXT.setText("");
//        sTime_TXT.setText("");
//        eTime_TXT.setText("");
//        sTime_TXT.setPromptText("HH:MM");
//        eTime_TXT.setPromptText("HH:MM");
//        type_CHECK.selectedProperty().setValue(false);
//    }

    @FXML
    private void refresh(){
        name_TXT.setText("");
        loc_TXT.setText("");
        sTime_TXT.setText("");
        eTime_TXT.setText("");
        desc_TXT.setText("");
        type_CHECK.setSelected(false);
        start_DATE.getEditor().setText("");
        end_DATE.getEditor().setText("");

        events_LST.getItems().clear();
        Event[] events= DataServer.getAllEvent();
        for(int i = 0; i< Array.getLength(events); i++){
            events_LST.getItems().add(events[i].getName()+","+events[i].getsDay().toString()+","+events[i].getsTime().toString());
        }
    }

    @FXML
    private void confirm_edits(MouseEvent e){

    }

    @FXML
    private void select_event(MouseEvent e){
//        Node node = e.getSource();
        Event[] events=DataServer.getAllEvent();
        String sTime, sDay,selected;
        LocalTime time;
        LocalDate date;
        Event target_event;
        int selected_length;

        selected=events_LST.getSelectionModel().getSelectedItem().toString();
        selected_length=selected.length();
        time=LocalTime.parse(selected.substring(selected_length-5,selected_length));
        date=LocalDate.parse(selected.substring(selected_length-16,selected_length-6));

        for(int i=0;i<Array.getLength(events);i++){
            ;
        }
        System.out.println(events_LST.getSelectionModel().getSelectedItem().toString());
    }
    @FXML
    private void validate_sTime(){
        String time=sTime_TXT.getText();
        if(!validate_time(time))
            invalid_input_dialogue("Event times should be HH:MM on a 24 hour clock");
    }
    @FXML
    private void validate_eTime(){
        String time=eTime_TXT.getText();
        if(!validate_time(time))
            invalid_input_dialogue("Event times should be HH:MM on a 24 hour clock");
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

//    @FXML protected static void setName_TXT(String str) {
//        name_TXT.setText(str);
//    }
//    @FXML protected static void setLoc_TXT(String str) {loc_TXT.setText(str);}
//    @FXML protected static void setsTime_TXT(String str) {sTime_TXT.setText(str);}
//    @FXML protected static void seteTime_TXT(String str) {eTime_TXT.setText(str);}
//    @FXML protected static void setDesc_TXT(String str) {desc_TXT.setText(str);}
////    @FXML protected static void setStart_DATE(String str) {start_DATE.setText(str);}
////    @FXML protected static void setEnd_DATE(String str) {end_DATE.setText(str);}
//    @FXML protected static void setType_CHECK(boolean flag) {type_CHECK.setSelected(flag);}

}
