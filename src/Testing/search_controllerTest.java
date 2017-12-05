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
    private boolean[][] results, results2;
    @Before
    public void setup(){
        results=new boolean[7][24];
        results2=new boolean[7][24];
        for(int i=0; i<7;i++)
            for(int j=0;j<24;j++) {
                results[i][j] = true;
                results[i][j]=true;
            }
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
        pb.setEnd(LocalTime.parse("15:00"));
        userPrefs prefs=pb.createPrefs();
        DataServer.savePrefs(prefs);

        /*
                Workdays M T W R F
                from 9-15:00
         */
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
        results=new boolean[7][24];
        for(int i=0; i<7;i++)
            for(int j=0;j<24;j++)
                results[i][j]=true;
        destroyDB();
        DataServer.init();
        eventBuilder eb = new eventBuilder();

        Event testEvent3, testEvent4, testEvent5, new_event, testEvent6;

        eb.setName("testEvent3");
        eb.setsDay(LocalDate.parse("2017-12-05"));
        eb.seteDay(LocalDate.parse("2017-12-05"));
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
                System.out.printf("Checking off limits indexes, [0][%d] and [1][%d]", i);
                assertEquals(false, results[0][i]);
                assertEquals(false, results[1][i]);
            } else {
                System.out.printf("Checking acceptable indexes, [0][%d] and [1][%d]", i);
                assertEquals(true, results[0][i]);
                assertEquals(true, results[1][i]);
            }
        }
        for(int i=0;i<currHour+2;i++){
            System.out.printf("Checking time that has already passed+2 hours, [0][%d] and [1][%d]",i);
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
    //TODO debug this
    private boolean[][] eliminate_by_conflicts(boolean [][] results, LocalDate today, LocalTime currTime){
        Event[] events=DataServer.getAllEvent();
        int window_start, window_end, currYear, eLength, eOffset;

        window_start=today.getDayOfYear();
        window_end=today.getDayOfYear()+7;
        currYear=today.getYear();

        //eliminate hours from today that have passed, as well as the next 2 hours to give a buffer
        for(int i=0;i<currTime.getHour()+2;i++)
            results[0][i]=false;

        for(int i=0;i<Array.getLength(events);i++){
            if(events[i].getsDay().getYear()==currYear){
                //if it's in the desired window, examine more closely
                if(events[i].getsDay().getDayOfYear()>=window_start && events[i].getsDay().getDayOfYear()<window_end){
                    eLength=events[i].geteTime().getHour()-events[i].getsTime().getHour();//truncate minutes and compute length

                    //in cases where event does not start on the hour, add additional hour to avoid deeper analysis
                    if(events[i].getsTime().getMinute()!=0 || events[i].geteTime().getMinute()!=0) {
                        System.out.println("DBG incrementing to compensate for partial hours");
                        eLength++;
                    }
                    //for events that are less then 1 hour long, block the entire hour
                    if(eLength<1) {
                        System.out.println("DBG eLength<1, setting equal to 1");
                        eLength = 1;
                    }
                    eOffset=window_end-events[i].getsDay().getDayOfYear();
                    for(int j=eLength+events[i].getsTime().getHour(); j>events[i].getsTime().getHour();j--)
                        results[eOffset][j]=false;
                }
            }
        }
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