package frontend.controllers;

import Backend.DataServer;
import Backend.Event;
import Backend.eventBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalTime;

import static Backend.DataServer.saveEvent;
import static frontend.controllers.main_controller.updateIndicators;

public class event_manager_controller extends universal_controller{


    @FXML private  TextField name_TXT, loc_TXT, sTime_TXT, eTime_TXT;
    @FXML private  TextArea desc_TXT;
    @FXML private  DatePicker start_DATE, end_DATE;
    @FXML private Button confirm_BTN;
    @FXML private  CheckBox type_CHECK;
    @FXML ListView events_LST;
    private Event selected_event;

    @FXML
    private void initialize(){
        refresh_list();
        refresh_fields();
    }

    @FXML
    private void set_alarm(){
        if(selected_event!=null) {
            LocalTime time = selected_event.getsTime();
            LocalDate date = selected_event.getsDay();
            create_alarm(date, time);
        }
    }

    @FXML
    private void create_alarm(LocalDate date, LocalTime time){
        //TODO spin thread to play alarm @date&&time
        ;
    }
    @FXML
    private void refresh_fields(){
        name_TXT.setText("");
        loc_TXT.setText("");
        sTime_TXT.setText("");
        eTime_TXT.setText("");
        desc_TXT.setText("");
        type_CHECK.setSelected(false);
        start_DATE.getEditor().setText("");
        end_DATE.getEditor().setText("");


    }

    @FXML
    private void refresh_list(){
        events_LST.getItems().clear();
        Event[] events= DataServer.getAllEvent();
        for(int i = 0; i< Array.getLength(events); i++){
            events_LST.getItems().add(events[i].getName()+","+events[i].getsDay().toString()+","+events[i].getsTime().toString());
        }
    }

    @FXML
    private void confirm_delete(){
        if(selected_event!=null){
            DataServer.deleteEvent(selected_event.getsTime().toString(), selected_event.geteTime().toString(),
                    selected_event.getsDay().toString(), selected_event.geteDay().toString());
            selected_event=null;
            refresh_fields();
            refresh_list();
        }
    }
    @FXML
    private void confirm_edits(){
        if(selected_event!=null) {
            //delete old event
            DataServer.deleteEvent(selected_event.getsTime().toString(), selected_event.geteTime().toString(),
                    selected_event.getsDay().toString(), selected_event.geteDay().toString());

            //create new event
            eventBuilder eb = new eventBuilder();
            eb.setName(name_TXT.getText());
            eb.setLoc(loc_TXT.getText());
            eb.setDesc(desc_TXT.getText());
            eb.setType(type_CHECK.isSelected());
            eb.setsTime(LocalTime.parse(sTime_TXT.getText()));
            eb.seteTime(LocalTime.parse(eTime_TXT.getText()));
            eb.setsDay(start_DATE.getValue());
            eb.seteDay(end_DATE.getValue());
            DataServer.saveEvent(eb.createEvent());
            refresh_list();
            updateIndicators();
        }
    }

    @FXML
    private void select_event(){
        Event[] events=DataServer.getAllEvent();
        String name;
        LocalTime time;
        LocalDate date;

        String[] eSplit=events_LST.getSelectionModel().getSelectedItem().toString().split(",");
        name=eSplit[0];
        date=LocalDate.parse(eSplit[1]);
        time=LocalTime.parse(eSplit[2]);

        for(int i=0;i<Array.getLength(events);i++)
            if(events[i].getsDay().compareTo(date)==0)
                if(events[i].getsTime().compareTo(time)==0)
                    if(events[i].getName().compareTo(name)==0)
                        selected_event = events[i];

        name_TXT.setText(selected_event.getName());
        loc_TXT.setText(selected_event.getLoc());
        sTime_TXT.setText(selected_event.getsTime().toString());
        eTime_TXT.setText(selected_event.geteTime().toString());
        desc_TXT.setText(selected_event.getDesc());
        start_DATE.setValue(selected_event.getsDay());
        end_DATE.setValue(selected_event.geteDay());
        if(selected_event.isWorkType())
            type_CHECK.setSelected(true);
        else
            type_CHECK.setSelected(false);


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
//    private void confirm_changes(MouseEvent e){
    private void confirm_new(ActionEvent e){
        System.out.println(e.getSource().toString());
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
            Event ev = eb.createEvent();
            saveEvent(ev);
            updateIndicators();
        }
        catch(Exception err){
            System.out.println("Invalid input: " + err.getMessage());
        }
        finally {
            Stage stage = (Stage) confirm_BTN.getScene().getWindow();
            stage.close();
        }
    }
}
