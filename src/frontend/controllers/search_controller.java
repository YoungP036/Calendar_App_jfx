package frontend.controllers;

import Backend.DataServer;
import Backend.Event;
import Backend.eventBuilder;
import Backend.userPrefs;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;

import static frontend.controllers.main_controller.updateIndicators;

public class search_controller extends universal_controller {
    @FXML private TextField length_TXT, name_TXT, loc_TXT;
    @FXML private Label loc_LBL, name_LBL;
    @FXML private ListView eList_LST;
    @FXML private CheckBox work_CHK, personal_CHK, all_CHK;
    @FXML private Button add_BTN;

    @FXML
    private void validate() {
        String mins = length_TXT.getText();
        if (!validate_minutes(mins))
            invalid_input_dialogue("Search lengths should in hours and greater then 0");
    }

    @FXML
    private void add_from_listview(MouseEvent e){
        LocalTime start, end;
        LocalDate day;
        int hour;
        String list_item=eList_LST.getSelectionModel().getSelectedItem().toString();
        start=LocalTime.parse(list_item.toString().substring(14,19));
        day=LocalDate.parse(list_item.toString().substring(0,10));

        //get start time, compute end time by adding new event length
        end=LocalTime.parse(list_item.toString().substring(14,19));
        hour=end.getHour();
        hour+=Integer.parseInt(length_TXT.getText());

        try{
            end=LocalTime.parse(Integer.toString(hour)+list_item.toString().substring(16,19));
        }catch(DateTimeException err){
            System.out.printf("Event is too long, get some sleep instead\n");
        }

        System.out.printf("Start time=%S\t End time=%S\n",start.toString(), end.toString());
        eventBuilder eb= new eventBuilder();
        eb.setName(name_TXT.getText());
        eb.setLoc(loc_TXT.getText());
        eb.setsDay(day);
        eb.seteDay(day);
        eb.setsTime(start);
        eb.seteTime(end);
        DataServer.saveEvent(eb.createEvent());
        updateIndicators();

        Stage stage = (Stage) add_BTN.getScene().getWindow();
        stage.close();
    }

    //does not take into account sleep, sleep is for the weak
    @FXML
    private void search() {
        boolean[][] search_results = new boolean[7][24];
        eList_LST.getItems().clear();


        int type;
        int length;
        if (validate_hours(length_TXT.getText()))
            length = Integer.parseInt(length_TXT.getText());
        else {
            invalid_input_dialogue("Search lengths should in hours and greater then 0");
            return;
        }
        if (work_CHK.isSelected() && !personal_CHK.isSelected() && !all_CHK.isSelected())
            type = 1;
        else if (personal_CHK.isSelected() && !work_CHK.isSelected() && !all_CHK.isSelected())
            type = 2;
        else
            type = 3;

        search_results = master_search(search_results, length, type, LocalDate.now(), LocalTime.now());

        if (search_results == null)
            return;

        String entry;
        LocalDate day;
        LocalDate currDay = LocalDate.now();
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 24; j++) {
                if (search_results[i][j] == true) {
                    day = currDay.plusDays(i);
                    if(j<10)
                        entry=day.toString()+ " at 0"+j+":00";
                    else
                        entry = day.toString() + " at " + j + ":00";
                    eList_LST.getItems().add(entry);
                }
            }
        loc_LBL.setVisible(true);
        name_LBL.setVisible(true);
        loc_TXT.setVisible(true);
        name_TXT.setVisible(true);
        add_BTN.setVisible(true);
    }

    private boolean[][] master_search(boolean[][] results, int eLength, int type, LocalDate today, LocalTime currTime) {
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 24; j++)
                results[i][j] = true;

        results = eliminate_by_eType(results, type, today);

        if (results != null)
            results = eliminate_by_conflicts(results, today, currTime);
        else {
            System.out.println("DBG terminating search");
            return null;
        }
        results = eliminate_by_length(results, eLength);
        return results;
    }

    private boolean[][] eliminate_by_eType(boolean[][] results, int type, LocalDate today) {
        userPrefs prefs = DataServer.getPrefs();
        int wStart, wEnd;
        boolean temp;
        boolean[] work_days;
        int dayOfWeek = today.getDayOfWeek().getValue();

        //no work needed
        if (prefs == null || type == 3)
            return results;
        //do the work
        else {
            //if prefs arent set, return results as is so search may continue for the jobless bum
            try {
                work_days = prefs.getWorkdays();
                wStart = prefs.getwStart_time().getHour();
                wEnd = prefs.getwEnd_time().getHour();

            } catch (Exception e) {
                System.out.println("User has not set preferences, continueing search\n");
                return results;
            }

            //slide work_days array until it is parallel with our [today, today+7] window size
            for (int i = 0; i < dayOfWeek; i++) {
                temp = work_days[0];
                for (int j = 0; j < 7; j++)
                    if (j == 6) {
                        work_days[j] = temp;
                    } else
                        work_days[j] = work_days[j + 1];
            }

            //CASE 1: Opening needed for WORK event, only consider working hours
            if (type == 1) {
                for (int i = 0; i < Array.getLength(work_days); i++) {
                    //if its not a work day, eliminate that whole day
                    if (work_days[i] == false)
                        for (int j = 0; j < 24; j++)
                            results[i][j] = false;
                    else {
                        for (int j = 0; j < wStart; j++)
                            results[i][j] = false;
                        for (int j = wStart; j < wEnd; j++)
                            results[i][j] = true;
                        for (int j = wEnd + 1; j < 24; j++)
                            results[i][j] = false;
                    }
                }
            }
            //CASE 2: Opening needed for PERSONAL event, exclude working hours
            else if (type == 2) {
                for (int i = 0; i < Array.getLength(work_days); i++) {
                    //if its not a work day, keep the whole day
                    if (work_days[i] == false)
                        for (int j = 0; j < 24; j++)
                            results[i][j] = true;
                    else {
                        for (int j = 0; j < wStart; j++)
                            results[i][j] = true;
                        for (int j = wStart; j < wEnd + 1; j++)
                            results[i][j] = false;
                        for (int j = wEnd + 1; j < 24; j++)
                            results[i][j] = true;
                    }
                }
            }
        }
        return results;
    }

    /* Iterate through events
    1. if its the same year, continue
    2. if the event occurs within the 7 day window from [today,today+7], continue
    3. get length of event, and the offset mapping the events start day onto our window interval
    4. set eLength indexes in results to false at the correct offset
 */
    private boolean[][] eliminate_by_conflicts(boolean[][] results, LocalDate today, LocalTime currTime) {
        Event[] events = DataServer.getAllEvent();
        System.out.printf("DBG currDay=%S\tcurrTime=%S\n", today.toString(), currTime.toString());
        for (int i = 0; i < Array.getLength(events); i++) {
            System.out.printf("Event: %S on %S from %S to %S\n", events[i].getName(), events[i].getsDay().toString(), events[i].getsTime().toString(), events[i].geteTime().toString());
        }
        int window_start, window_end, currYear, eLength, eOffset;
        int eYear, eDay, esHour, eeHour;
        window_start = today.getDayOfYear();
        window_end = today.getDayOfYear() + 6;
        currYear = today.getYear();

        //eliminate hours from today that have passed, as well as the next 2 hours to give a buffer
        for (int i = 0; i < currTime.getHour() + 3; i++)
            results[0][i] = false;


        //handle one day at a time
        for (int i = 0; i < Array.getLength(events); i++) {
            eYear = events[i].getsDay().getYear();
            if (eYear == currYear) {//check year matches
                eDay = events[i].getsDay().getDayOfYear();
                if (eDay >= window_start && eDay <= window_end) {//check its in our window
                    esHour = events[i].getsTime().getHour();
                    eeHour = events[i].geteTime().getHour();
                    eLength = eeHour - esHour;
                    if (eLength < 1) eLength = 1;
                    eOffset = eDay - window_start;

                    for (int j = esHour + eLength; j >= esHour; j--)
                        results[eOffset][j] = false;
                }
            }
        }
        return results;
    }

    /*
Alg
    1. create parallel int array
    2. each index holds the number of hours from that index that are open
 */
    private boolean[][] eliminate_by_length(boolean[][] results, int eLength) {
        int[][] openings = new int[7][24];
        int len = 0;
        boolean flag;
        int k;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 24; j++) {
                flag = true;
                len = 0;
                if (results[i][j] == true) {
                    k = j;
                    //look ahead until a false index is found
                    while (k < 24 && flag) {
                        if (results[i][k] == true) {
                            len++;
                            k++;
                        } else
                            flag = false;
                    }
                    openings[i][j] = len;
                } else
                    openings[i][j] = 0;
            }
        }

        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 24; j++) {
                if (openings[i][j] >= eLength)
                    results[i][j] = true;
                else
                    results[i][j] = false;
            }
        return results;
    }
}