package frontend.controllers;

import Backend.DataServer;
import Backend.Event;
import Backend.userPrefs;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalTime;

public class search_controller extends universal_controller{
    @FXML private TextField length_TXT, name_TXT, loc_TXT;
    @FXML private ListView eList_LST;
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
        eList_LST.getItems().clear();


        int type=0;
        int length= Integer.parseInt(length_TXT.getText());

        if(work_CHK.isSelected())
            type=1;
        else if(personal_CHK.isSelected())
            type=2;
        else
            type=3;

        search_results=master_search(search_results, length, type, LocalDate.now(), LocalTime.now());

        String entry;
        LocalDate day;
        LocalDate currDay = LocalDate.now();
        for(int i=0;i<7;i++)
            for(int j=0;j<24;j++){
                if(search_results[i][j]==true) {
                    day = currDay.plusDays(i);
                    entry = day.toString() + " at " + j + ":00";
                    eList_LST.getItems().add(entry);
                }
            }
        System.out.println("TODO Search opening for " + length + " minute Event");
    }

    private boolean[][] master_search(boolean[][] results, int eLength,int type, LocalDate today, LocalTime currTime){
        for(int i=0;i<7;i++)
            for(int j=0;j<24;j++)
                results[i][j]=true;

        results=eliminate_by_eType(results, type);
        results=eliminate_by_conflicts(results, today, currTime);
        results=eliminate_by_length(results, eLength);
        return results;
    }
    private boolean[][] eliminate_by_eType(boolean[][] results, int type){
        userPrefs prefs= DataServer.getPrefs();
        if(prefs==null || type==3 || type==0)
            return results;
        else{
            boolean[] work_days=prefs.getWorkdays();
            int wStart=prefs.getwStart_time().getHour();
            int wEnd=prefs.getwEnd_time().getHour();

            //CASE 1: Opening needed for WORK event, only consider working hours
            if(type==1){
                for(int i = 0; i< Array.getLength(work_days); i++){
                    //if its not a work day, eliminate that whole day
                    if(work_days[i]==false)
                        for(int j=0;j<24;j++)
                            results[i][j]=false;
                    else{
                        for(int j=0;j<wStart;j++)
                            results[i][j]=false;
                        for(int j=wStart;j<wEnd;j++)
                            results[i][j]=true;
                        for(int j=wEnd;j<24;j++)
                            results[i][j]=false;
                    }
                }
            }
            //CASE 2: Opening needed for PERSONAL event, exclude working hours
            else if(type==2){
                for(int i = 0; i< Array.getLength(work_days); i++){
                    //if its not a work day, keep the whole day
                    if(work_days[i]==false)
                        for(int j=0;j<24;j++)
                            results[i][j]=true;
                    else{
                        for(int j=0;j<wStart;j++)
                            results[i][j]=true;
                        for(int j=wStart;j<wEnd;j++)
                            results[i][j]=false;
                        for(int j=wEnd;j<24;j++)
                            results[i][j]=true;
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
    private boolean[][] eliminate_by_conflicts(boolean [][] results, LocalDate today, LocalTime currTime){
        Event[] events=DataServer.getAllEvent();
        int window_start, window_end, currYear, eLength, eOffset;
        int eYear, eDay, esHour,eeHour;
        window_start=today.getDayOfYear();
        window_end=today.getDayOfYear()+6;
        currYear=today.getYear();

        //eliminate hours from today that have passed, as well as the next 2 hours to give a buffer
        for(int i=0;i<currTime.getHour()+3;i++)
            results[0][i] = false;


        //handle one day at a time
        for(int i=0;i<Array.getLength(events);i++){
            eYear=events[i].getsDay().getYear();
            if(eYear==currYear){//check year matches
                eDay=events[i].getsDay().getDayOfYear();
                if(eDay>=window_start && eDay<=window_end){//check its in our window
                    esHour=events[i].getsTime().getHour();
                    eeHour=events[i].geteTime().getHour();
                    eLength=eeHour-esHour;
                    if(eLength<1) eLength=1;
                    eOffset=eDay-window_start;

                    for(int j=esHour+eLength;j>=esHour;j--)
                        results[eOffset][j]=false;
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
//        for(int i=0;i<7;i++)
//            for(int j=0;j<24;j++)
//                System.out.printf("[%d][%d]=%S\n",i,j,results[i][j]);
        int[][] openings = new int[7][24];
        int len=0;
        boolean flag;
//                =true;
        int k;
        for(int i=0;i<7;i++) {
            for (int j = 0; j < 24; j++) {
//                System.out.printf("[%d][%d]\n",i,j);
                flag=true;
                len=0;
                if (results[i][j] == true) {
                    k=j;
                    //look ahead until a false index is found
                    while(k<24 && flag) {
                        if(results[i][k]==true){
                            len++;
                            k++;
                        }
                        else
                            flag=false;
                    }
                    openings[i][j]=len;
                }
                else
                    openings[i][j]=0;
            }
        }

        for(int i=0;i<7;i++)
            for(int j=0;j<24;j++){
//                System.out.printf("DBG openings[%d][%d]=%d\n",i,j,openings[i][j]);
                if(openings[i][j]>=eLength)
                    results[i][j]=true;
                else
                    results[i][j]=false;
            }

        return results;
    }

}
