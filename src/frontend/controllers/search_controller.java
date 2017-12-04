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
        userPrefs prefs= DataServer.getPrefs();
        if(prefs==null || type==3 || type==0)
            return results;
        else{
            boolean[] work_days=prefs.getWorkdays();
            int wStart=prefs.getwStart_time().getHour();
            int wEnd=prefs.getwEnd_time().getHour();

            //modify results for a work event type search
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
            //modify results for a personal event type search
            else if(type==2){
                for(int i = 0; i< Array.getLength(work_days); i++){
                    //if its not a work day, eliminate that whole day
                    if(work_days[i]==true)
                        for(int j=0;j<24;j++)
                            results[i][j]=false;
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

}
