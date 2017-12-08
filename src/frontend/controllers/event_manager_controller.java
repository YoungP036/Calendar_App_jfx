package frontend.controllers;

import Backend.DataServer;
import Backend.Event;
import Backend.eventBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalTime;

import static Backend.DataServer.saveEvent;
import static frontend.controllers.main_controller.updateIndicators;

public class event_manager_controller extends universal_controller{


    @FXML private  TextField name_TXT, loc_TXT, sTime_TXT, eTime_TXT;
    @FXML private  TextArea desc_TXT;
    @FXML private  DatePicker start_DATE, end_DATE;
    @FXML private  CheckBox type_CHECK;
    @FXML ListView events_LST;
    private Event selected_event;

    @FXML //this is called immedietly after running main, not when window is brought into focus
    private void initialize(){
        refresh_fields();
        refresh_list();
    }

    @FXML //refreshes entire screen
    private void refresh(){
        refresh_fields();
        refresh_list();
    }

    @FXML //refreshes input fields
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

    @FXML //refreshes listview
    private void refresh_list(){
        events_LST.getItems().clear();
        Event[] events= DataServer.getAllEvent();
        for(int i = 0; i< Array.getLength(events); i++){
            events_LST.getItems().add(events[i].getName()+","+events[i].getsDay().toString()+","+events[i].getsTime().toString());
        }
    }

    //TODO if find way to refresh event manager when it COMES INTO FOCUS, expand edit feature
    //TODO automatically populate event manager with the event that was selected in main_screen
    //TODO currently can get that info via selected_event field
    @FXML //TODO need something akin to an onFocus event to prompt a refresh but apparantly that event doesnt exist
    private void check_selected(){
//        Event ev = main_controller.getSelected_event();
//        main_controller.setSelected_event(null);
    }

    //spins off a thread that will play an alert sound when the time is right
    private void create_alarm(LocalDate today, LocalTime time) {
        System.out.println("DBG in create alarm");
        Thread t = new Thread() {
            public void run() {
                File alarm_wav = new File("alarm.wav");
                System.out.println("DBG alarm thread running\n");
                while (true) {
                    System.out.println("DBG spinning");
                    if (LocalDate.now().compareTo(today) == 0 && LocalTime.now().compareTo(time) == 0) {
                        System.out.println("Breaking");
                        PlaySound(alarm_wav);
                        break;
                    }
                }
            }
        };
        t.start();
    }

    private static void PlaySound(File Sound){
        InputStream in;
        try{
            in = new FileInputStream(Sound);
            AudioStream audio = new AudioStream(in);
            AudioPlayer.player.start(audio);
            Thread.sleep(7500);
        }catch(Exception e){
            System.out.println("Alarm sound error: " + e.getMessage());
        }
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

    @FXML //activate via add new button, creates event using info from input fields
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
            refresh_list();
        }
        catch(Exception err){
            System.out.println("Invalid input: " + err.getMessage());
        }

    }

    @FXML //activated via delete button, requires event selected from list, deletes that event
    private void confirm_delete(){
        if(selected_event!=null){
            DataServer.deleteEvent(selected_event.getsTime().toString(), selected_event.geteTime().toString(),
                    selected_event.getsDay().toString(), selected_event.geteDay().toString());
            selected_event=null;
            refresh_fields();
            refresh_list();
            updateIndicators();
        }
    }

    @FXML //activated via edit old button, requires an event to be selected, deletes old event and creates new using input field info
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

    @FXML //activated on events listview click, gets reference to selected event at instance level
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

    @FXML //activated via set alarm button, requires an event to be selected from right hand side list, alarm dies on program exit
    private void set_alarm(){
        if(selected_event!=null) {
            LocalTime time = selected_event.getsTime();
            LocalDate date = selected_event.getsDay();
            System.out.println("DBG calling create_alarm(date,time)");

            create_alarm(date, time);
        }
    }

    @FXML //activated via test alarm button, will facilitate demonstration
    private void test_alarm(){
        create_alarm(LocalDate.now(), LocalTime.now().plusMinutes(1));
    }
}
