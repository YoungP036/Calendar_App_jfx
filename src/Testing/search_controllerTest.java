package Testing;

import Backend.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class search_controllerTest {
    private boolean[][] results;
    @Before
    public void setup(){
        results=new boolean[7][24];
        for(int i=0; i<7;i++)
            for(int j=0;j<24;j++)
                results[i][j]=true;
    }
    @Test
    public void eliminate_by_type_test(){
        destroyDB();
        DataServer.init();
        boolean[][] results=new boolean[7][24];
        boolean[][] results2=new boolean[7][24];
        boolean[] flags = new boolean[7];
        prefsBuilder pb = new prefsBuilder();
        flags[0]=false;
        flags[1]=true;
        flags[2]=true;
        flags[3]=true;
        flags[4]=true;
        flags[5]=true;
        flags[6]=false;
        pb.setDays(flags);
        pb.setStart(LocalTime.parse("09:00"));
        pb.setEnd(LocalTime.parse("03:00"));
        userPrefs prefs=pb.createPrefs();
        DataServer.savePrefs(prefs);
        results=eliminate_by_eType(results,1);
        for(int i=0;i<24;i++){
            assertEquals(results[0][i],false);
            assertEquals(results[6][i],false);
        }
        for(int j=1;j<5;j++) {
            for(int i=0;i<prefs.getwStart_time().getHour();i++)
                assertEquals(results[j][i],false);
            for (int i = prefs.getwStart_time().getHour(); i < prefs.getwEnd_time().getHour(); i++)
                assertEquals(results[j][i],true);
            for(int i=prefs.getwEnd_time().getHour();i<24;i++)
                assertEquals(results[j][i],false);
        }
        results2=eliminate_by_eType(results2,2);
        for(int i=0;i<24;i++){
            assertEquals(results2[0][i],true);
            assertEquals(results2[6][i],true);
        }
        for(int j=1;j<5;j++) {
            for(int i=0;i<prefs.getwStart_time().getHour();i++)
                assertEquals(results2[j][i],true);
            for (int i = prefs.getwStart_time().getHour(); i < prefs.getwEnd_time().getHour(); i++)
                assertEquals(results2[j][i],false);
            for(int i=prefs.getwEnd_time().getHour();i<24;i++)
                assertEquals(results2[j][i],true);
        }
    }

    @Test
    public void test_eliminate_by_conflict(){
        destroyDB();
        DataServer.init();
        eventBuilder eb = new eventBuilder();

        Event testEvent3, testEvent4, testEvent5, new_event, testEvent6;

        eb.setName("testEvent3");
        eb.setsDay(LocalDate.parse("2017-12-05"));
        eb.setsDay(LocalDate.parse("2017-12-05"));
        eb.setsTime(LocalTime.parse("01:00"));
        eb.seteTime(LocalTime.parse("06:00"));
        testEvent3=eb.createEvent();

        eb.setsTime(LocalTime.parse("12:00"));
        eb.seteTime(LocalTime.parse("18:00"));
        eb.setName("testEvent4");
        testEvent4=eb.createEvent();

        eb.setName("testEvent5");
        eb.setsDay(LocalDate.parse("2017-12-04"));
        eb.setsDay(LocalDate.parse("2017-12-04"));
        eb.setsTime(LocalTime.parse("01:00"));
        eb.seteTime(LocalTime.parse("06:00"));
        testEvent5=eb.createEvent();

        eb.setsTime(LocalTime.parse("12:00"));
        eb.seteTime(LocalTime.parse("18:00"));
        eb.setName("testEvent6");
        testEvent6=eb.createEvent();

        int event_length=120;
        LocalDate current_day=LocalDate.parse("2017-12-04");
        LocalTime current_time=LocalTime.parse("08:00");
        int currHour=current_time.getHour();
        DataServer.saveEvent(testEvent3);
        DataServer.saveEvent(testEvent4);
        DataServer.saveEvent(testEvent5);
        DataServer.saveEvent(testEvent6);

        boolean[][] results=new boolean[7][24];
        results=eliminate_by_conflicts(results, current_day, current_time);

        for(int i=0;i<24;i++) {
            if ((i <= 6 && i >= 1) || (i <= 18 && i >= 12)) {
                assertEquals(false, results[0][i]);
                assertEquals(false, results[1][i]);
            } else {
                assertEquals(true, results[0][i]);
                assertEquals(true, results[1][i]);
            }
        }
        for(int i=0;i<currHour+2;i++){
            assertEquals(false, results[0][i]);
            assertEquals(false, results[1][i]);
        }
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

    private boolean[][] eliminate_by_conflicts(boolean [][] results, LocalDate today, LocalTime currTime){


        return results;
    }
    //testing method only
    private void destroyDB(){
        String home = System.getProperty("user.home");
        String target_DB=home+"/cal_app/calDB.db";
        File f = new File(target_DB);
        boolean flag =f.delete();
//        System.out.println("DB deleted: " + flag);
    }
}