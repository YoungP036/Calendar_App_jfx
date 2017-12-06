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
                results[i][j] = true;
            }
    }
    @Test
    public void eliminate_by_type_test(){
        destroyDB();
        DataServer.init();
        boolean[][] results=new boolean[7][24];
        boolean[][] results2=new boolean[7][24];
        for(int i=0;i<7;i++)
            for(int j=0;j<24;j++){
            results[i][j]=true;
            results2[i][j]=true;
            }
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
        for(int j=0;j<24;j++)
            System.out.printf("before elim by type tests: R1[%d][%d]=%S\n",0,j,results[0][j]);
        /*
                Workdays M T W R F
                from 9-15:00
         */
        results=eliminate_by_eType(results,1, LocalDate.parse("2017-12-06"));
        for(int i=0;i<7;i++) {
            System.out.println("\n***DAY " + i + "***");
            for (int j = 0; j < 24; j++)
                System.out.printf("[%d][%d]=%S\n", i, j, results[i][j]);
        }
        for(int i=0;i<24;i++){
            assertEquals(results[3][i],false);
            assertEquals(results[4][i],false);
        }
        for(int j=0;j<2;j++) {
            for(int i=0;i<prefs.getwStart_time().getHour()-1;i++)
                assertEquals(results[j][i],false);
            for (int i = prefs.getwStart_time().getHour(); i < prefs.getwEnd_time().getHour(); i++) {
                System.out.printf("DBG asserting trues @ [%d][%d]",j,i);
                assertEquals(results[j][i], true);
            }
            for(int i=prefs.getwEnd_time().getHour()+1;i<24;i++)
                assertEquals(results[j][i],false);
        }
        for(int j=5;j<7;j++) {
            for(int i=0;i<prefs.getwStart_time().getHour()-1;i++)
                assertEquals(results[j][i],false);
            for (int i = prefs.getwStart_time().getHour(); i < prefs.getwEnd_time().getHour(); i++) {
                System.out.printf("DBG asserting trues @ [%d][%d]",j,i);
                assertEquals(results[j][i], true);
            }
            for(int i=prefs.getwEnd_time().getHour()+1;i<24;i++)
                assertEquals(results[j][i],false);
        }

        results2=eliminate_by_eType(results2,2, LocalDate.parse("2017-12-06"));

        //assert  mornings/evenings valid, workday out of bounds
        for(int j=0;j<2;j++) {
            for(int i=0;i<prefs.getwStart_time().getHour();i++)
                assertEquals(results2[j][i],true);
            for (int i = prefs.getwStart_time().getHour(); i < prefs.getwEnd_time().getHour(); i++)
                assertEquals(results2[j][i],false);
            for(int i=prefs.getwEnd_time().getHour()+1;i<24;i++)
                assertEquals(results2[j][i],true);
        }
        for(int i=0;i<24;i++){
            assertEquals(results2[3][i],true);
            assertEquals(results2[4][i],true);
        }
        for(int j=5;j<7;j++) {
            for(int i=0;i<prefs.getwStart_time().getHour();i++)
                assertEquals(results2[j][i],true);
            for (int i = prefs.getwStart_time().getHour(); i < prefs.getwEnd_time().getHour(); i++)
                assertEquals(results2[j][i],false);
            for(int i=prefs.getwEnd_time().getHour()+1;i<24;i++)
                assertEquals(results2[j][i],true);
        }
    }

    @Test
    public void test_eliminate_by_conflict(){
        results=new boolean[7][24];
        results2=new boolean[7][24];
        for(int i=0; i<7;i++)
            for(int j=0;j<24;j++) {
                results[i][j] = true;
                results2[i][j]=true;
        }
        destroyDB();
        DataServer.init();
        eventBuilder eb = new eventBuilder();

        Event testEvent3, testEvent4;

        eb.setName("testEvent3");
        eb.setsDay(LocalDate.parse("2017-12-05"));
        eb.seteDay(LocalDate.parse("2017-12-05"));
        eb.setsTime(LocalTime.parse("01:00"));
        eb.seteTime(LocalTime.parse("06:00"));
        DataServer.saveEvent(eb.createEvent());

        eb.setName("testEvent4");
        eb.setsTime(LocalTime.parse("12:00"));
        eb.seteTime(LocalTime.parse("18:00"));
        DataServer.saveEvent(eb.createEvent());

        LocalDate current_day=LocalDate.parse("2017-12-05");
        LocalTime current_time=LocalTime.parse("08:00");

        results=eliminate_by_conflicts(results, current_day, current_time);

        assertEquals(false,results[0][0]);
        assertEquals(false,results[0][1]);
        assertEquals(false,results[0][2]);
        assertEquals(false,results[0][3]);
        assertEquals(false,results[0][4]);
        assertEquals(false,results[0][5]);
        assertEquals(false,results[0][6]);
        assertEquals(false,results[0][7]);
        assertEquals(false,results[0][8]);
        assertEquals(false,results[0][9]);
        assertEquals(false,results[0][10]);
        assertEquals(true,results[0][11]);
        assertEquals(false,results[0][12]);
        assertEquals(false,results[0][13]);
        assertEquals(false,results[0][14]);
        assertEquals(false,results[0][15]);
        assertEquals(false,results[0][16]);
        assertEquals(false,results[0][17]);
        assertEquals(false,results[0][18]);
        assertEquals(true,results[0][19]);
        assertEquals(true,results[0][20]);
        assertEquals(true,results[0][21]);
        assertEquals(true,results[0][22]);
        assertEquals(true,results[0][23]);
        for(int i=1;i<7;i++)
            for(int j=0;j<24;j++)
            assertEquals(true,results[i][j]);

        eb.setName("testEvent5");
        eb.setsDay(LocalDate.parse("2017-12-11"));
        eb.setsDay(LocalDate.parse("2017-12-11"));
        eb.setsTime(LocalTime.parse("01:30"));
        eb.seteTime(LocalTime.parse("03:00"));
        DataServer.saveEvent(eb.createEvent());

        eb.setsTime(LocalTime.parse("07:00"));
        eb.seteTime(LocalTime.parse("08:30"));
        eb.setName("testEvent6");
        DataServer.saveEvent(eb.createEvent());

        eb.setsTime(LocalTime.parse("13:30"));
        eb.seteTime(LocalTime.parse("15:30"));
        eb.setName("testEvent7");
        DataServer.saveEvent(eb.createEvent());


        current_day=LocalDate.parse("2017-12-05");
        current_time=LocalTime.parse("01:00");
        results2=eliminate_by_conflicts(results2, current_day, current_time);

        assertEquals(false,results2[0][0]);
        assertEquals(false,results2[0][1]);
        assertEquals(false,results2[0][2]);
        assertEquals(false,results2[0][3]);
        assertEquals(false,results2[0][5]);
        assertEquals(false,results2[0][6]);
        assertEquals(true,results2[0][7]);
        assertEquals(true,results2[0][8]);
        assertEquals(true,results2[0][9]);
        assertEquals(true,results2[0][10]);
        assertEquals(true,results2[0][11]);
        assertEquals(false,results2[0][12]);
        assertEquals(false,results2[0][13]);
        assertEquals(false,results2[0][14]);
        assertEquals(false,results2[0][15]);
        assertEquals(false,results2[0][16]);
        assertEquals(false,results2[0][17]);
        assertEquals(false,results2[0][18]);
        assertEquals(true,results2[0][19]);
        assertEquals(true,results2[0][20]);
        assertEquals(true,results2[0][21]);
        assertEquals(true,results2[0][22]);
        assertEquals(true,results2[0][23]);

        for(int i=1;i<6;i++)
            for(int j=0;j<24;j++)
                assertEquals(true,results2[i][j]);
        assertEquals(true,results2[6][0]);
        assertEquals(false,results2[6][1]);
        assertEquals(false,results2[6][2]);
        assertEquals(false,results2[6][3]);
        assertEquals(true,results2[6][4]);
        assertEquals(true,results2[6][5]);
        assertEquals(true,results2[6][6]);
        assertEquals(false,results2[6][7]);
        assertEquals(false,results2[6][8]);
        assertEquals(true,results2[6][9]);
        assertEquals(true,results2[6][10]);
        assertEquals(true,results2[6][11]);
        assertEquals(true,results2[6][12]);
        assertEquals(false,results2[6][13]);
        assertEquals(false,results2[6][14]);
        assertEquals(false,results2[6][15]);
        assertEquals(true,results2[6][16]);
        assertEquals(true,results2[6][17]);
        assertEquals(true,results2[6][18]);
        assertEquals(true,results2[6][19]);
        assertEquals(true,results2[6][20]);
        assertEquals(true,results2[6][21]);
        assertEquals(true,results2[6][22]);
        assertEquals(true,results2[6][23]);
    }

    @Test
    public void test_eliminate_by_length(){
        boolean[][] results = new boolean[7][24];
        for(int i=0;i<7;i++)
            for(int j=0;j<24; j++)
                results[i][j]=true;
        //increasingly larger windows

        /*
            Possible fittings
            1 hour @ 0,2,3,5,6,7,9,10,11,12,14,15,16,17,18,19
            2 hour @ 2,5,6,9,10,11,14,15,16,17
            3 hour @ 5,9,10,14,15,16
            4 hour @ 9,14
            5 hour @ 14
        */
        results=setResultshelper(results);
        results=eliminate_by_length(results, 1);
        assertEquals(true, results[0][0]);
        assertEquals(false, results[0][1]);
        assertEquals(true, results[0][2]);
        assertEquals(true, results[0][3]);
        assertEquals(false, results[0][4]);
        assertEquals(true, results[0][5]);
        assertEquals(true, results[0][6]);
        assertEquals(true, results[0][7]);
        assertEquals(false, results[0][8]);
        assertEquals(true, results[0][9]);
        assertEquals(true, results[0][10]);
        assertEquals(true, results[0][11]);
        assertEquals(true, results[0][12]);
        assertEquals(false, results[0][13]);
        assertEquals(true, results[0][14]);
        assertEquals(true, results[0][15]);
        assertEquals(true, results[0][16]);
        assertEquals(true, results[0][17]);
        assertEquals(true, results[0][18]);
        assertEquals(false, results[0][19]);
        assertEquals(false, results[0][20]);
        assertEquals(false, results[0][21]);
        assertEquals(false, results[0][22]);
        assertEquals(false, results[0][23]);

        //        2 hour @ 2,5,6,9,10,11,14,15,16,17
        results=setResultshelper(results);
        results=eliminate_by_length(results,2);
        assertEquals(false, results[0][0]);
        assertEquals(false, results[0][1]);
        assertEquals(true, results[0][2]);
        assertEquals(false, results[0][3]);
        assertEquals(false, results[0][4]);
        assertEquals(true, results[0][5]);
        assertEquals(true, results[0][6]);
        assertEquals(false, results[0][7]);
        assertEquals(false, results[0][8]);
        assertEquals(true, results[0][9]);
        assertEquals(true, results[0][10]);
        assertEquals(true, results[0][11]);
        assertEquals(false, results[0][12]);
        assertEquals(false, results[0][13]);
        assertEquals(true, results[0][14]);
        assertEquals(true, results[0][15]);
        assertEquals(true, results[0][16]);
        assertEquals(true, results[0][17]);
        assertEquals(false, results[0][18]);
        assertEquals(false, results[0][19]);
        assertEquals(false, results[0][20]);
        assertEquals(false, results[0][21]);
        assertEquals(false, results[0][22]);
        assertEquals(false, results[0][23]);

        //        3 hour @ 5,9,10,14,15,16

        results=setResultshelper(results);
        results=eliminate_by_length(results, 3);
        assertEquals(false, results[0][0]);
        assertEquals(false, results[0][1]);
        assertEquals(false, results[0][2]);
        assertEquals(false, results[0][3]);
        assertEquals(false, results[0][4]);
        assertEquals(true, results[0][5]);
        assertEquals(false, results[0][6]);
        assertEquals(false, results[0][7]);
        assertEquals(false, results[0][8]);
        assertEquals(true, results[0][9]);
        assertEquals(true, results[0][10]);
        assertEquals(false, results[0][11]);
        assertEquals(false, results[0][12]);
        assertEquals(false, results[0][13]);
        assertEquals(true, results[0][14]);
        assertEquals(true, results[0][15]);
        assertEquals(true, results[0][16]);
        assertEquals(false, results[0][17]);
        assertEquals(false, results[0][18]);
        assertEquals(false, results[0][19]);
        assertEquals(false, results[0][20]);
        assertEquals(false, results[0][21]);
        assertEquals(false, results[0][22]);
        assertEquals(false, results[0][23]);

        //        4 hour @ 9,14
        results=setResultshelper(results);
        results=eliminate_by_length(results, 4);
        assertEquals(false, results[0][0]);
        assertEquals(false, results[0][1]);
        assertEquals(false, results[0][2]);
        assertEquals(false, results[0][3]);
        assertEquals(false, results[0][4]);
        assertEquals(false, results[0][5]);
        assertEquals(false, results[0][6]);
        assertEquals(false, results[0][7]);
        assertEquals(false, results[0][8]);
        assertEquals(true, results[0][9]);
        assertEquals(false, results[0][10]);
        assertEquals(false, results[0][11]);
        assertEquals(false, results[0][12]);
        assertEquals(false, results[0][13]);
        assertEquals(true, results[0][14]);
        assertEquals(true, results[0][15]);
        assertEquals(false, results[0][16]);
        assertEquals(false, results[0][17]);
        assertEquals(false, results[0][18]);
        assertEquals(false, results[0][19]);
        assertEquals(false, results[0][20]);
        assertEquals(false, results[0][21]);
        assertEquals(false, results[0][22]);
        assertEquals(false, results[0][23]);

        //        5 hour @ 14
        results=setResultshelper(results);
        results=eliminate_by_length(results, 5);
        assertEquals(false, results[0][0]);
        assertEquals(false, results[0][1]);
        assertEquals(false, results[0][2]);
        assertEquals(false, results[0][3]);
        assertEquals(false, results[0][4]);
        assertEquals(false, results[0][5]);
        assertEquals(false, results[0][6]);
        assertEquals(false, results[0][7]);
        assertEquals(false, results[0][8]);
        assertEquals(false, results[0][9]);
        assertEquals(false, results[0][10]);
        assertEquals(false, results[0][11]);
        assertEquals(false, results[0][12]);
        assertEquals(false, results[0][13]);
        assertEquals(true,  results[0][14]);
        assertEquals(false, results[0][15]);
        assertEquals(false, results[0][16]);
        assertEquals(false, results[0][17]);
        assertEquals(false, results[0][18]);
        assertEquals(false, results[0][19]);
        assertEquals(false, results[0][20]);
        assertEquals(false, results[0][21]);
        assertEquals(false, results[0][22]);
        assertEquals(false, results[0][23]);
    }

    @Test
    public void IntegrationTest_master_search() {
        destroyDB();
        DataServer.init();
        boolean[][] results = new boolean[7][24];
        boolean[][] results2 = new boolean[7][24];
        boolean[] workdays = new boolean[7];

        prefsBuilder pb = new prefsBuilder();
        eventBuilder eb = new eventBuilder();
        for (int i = 0; i < 7; i++) {
            workdays[i] = true;
            for (int j = 0; j < 24; j++) {
                results[i][j] = true;
                results2[i][j] = true;
            }
        }
        workdays[0] = false;
        workdays[6] = false;
        pb.setDays(workdays);
        pb.setStart(LocalTime.parse("08:00"));
        pb.setEnd(LocalTime.parse("15:00"));
        DataServer.savePrefs(pb.createPrefs());

        eb.setName("e1");
        eb.setsTime(LocalTime.parse("02:00"));
        eb.seteTime(LocalTime.parse("04:00"));
        eb.setsDay(LocalDate.parse("2017-12-06"));
        eb.seteDay(LocalDate.parse("2017-12-06"));
        DataServer.saveEvent(eb.createEvent());

        eb.setName("e2");
        eb.setsTime(LocalTime.parse("11:00"));
        eb.seteTime(LocalTime.parse("13:00"));
        eb.setsDay(LocalDate.parse("2017-12-06"));
        eb.seteDay(LocalDate.parse("2017-12-06"));
        DataServer.saveEvent(eb.createEvent());

        results = master_search(results, 1, 1, LocalDate.parse("2017-12-06"), LocalTime.parse("01:00"));
        results2 = master_search(results2, 1, 2, LocalDate.parse("2017-12-06"), LocalTime.parse("01:00"));



//true @ 8 9 10 11 12 13 14 15 for days 1,2,5 and 7
        for(int i=1;i<2;i++) {
            assertEquals(false, results[i][0]);
            assertEquals(false, results[i][1]);
            assertEquals(false, results[i][2]);
            assertEquals(false, results[i][3]);
            assertEquals(false, results[i][4]);
            assertEquals(false, results[i][5]);
            assertEquals(false, results[i][6]);
            assertEquals(false, results[i][7]);
            assertEquals(true, results[i][8]);
            assertEquals(true, results[i][9]);
            assertEquals(true, results[i][10]);
            assertEquals(true, results[i][11]);
            assertEquals(true, results[i][12]);
            assertEquals(true, results[i][13]);
            assertEquals(true, results[i][14]);
            assertEquals(true, results[i][15]);
            assertEquals(false, results[i][16]);
            assertEquals(false, results[i][17]);
            assertEquals(false, results[i][18]);
            assertEquals(false, results[i][19]);
            assertEquals(false, results[i][20]);
            assertEquals(false, results[i][21]);
            assertEquals(false, results[i][22]);
            assertEquals(false, results[i][23]);
        }
        for(int i=5;i<7;i++) {
            assertEquals(false, results[i][0]);
            assertEquals(false, results[i][1]);
            assertEquals(false, results[i][2]);
            assertEquals(false, results[i][3]);
            assertEquals(false, results[i][4]);
            assertEquals(false, results[i][5]);
            assertEquals(false, results[i][6]);
            assertEquals(false, results[i][7]);
            assertEquals(true, results[i][8]);
            assertEquals(true, results[i][9]);
            assertEquals(true, results[i][10]);
            assertEquals(true, results[i][11]);
            assertEquals(true, results[i][12]);
            assertEquals(true, results[i][13]);
            assertEquals(true, results[i][14]);
            assertEquals(true, results[i][15]);
            assertEquals(false, results[i][16]);
            assertEquals(false, results[i][17]);
            assertEquals(false, results[i][18]);
            assertEquals(false, results[i][19]);
            assertEquals(false, results[i][20]);
            assertEquals(false, results[i][21]);
            assertEquals(false, results[i][22]);
            assertEquals(false, results[i][23]);
        }
        //true @ 8 9 10 14 15 for day 0
        assertEquals(false, results[0][0]);
        assertEquals(false, results[0][1]);
        assertEquals(false, results[0][2]);
        assertEquals(false, results[0][3]);
        assertEquals(false, results[0][4]);
        assertEquals(false, results[0][5]);
        assertEquals(false, results[0][6]);
        assertEquals(false, results[0][7]);
        assertEquals(true, results[0][8]);
        assertEquals(true, results[0][9]);
        assertEquals(true, results[0][10]);
        assertEquals(false, results[0][11]);
        assertEquals(false, results[0][12]);
        assertEquals(false, results[0][13]);
        assertEquals(true, results[0][14]);
        assertEquals(true, results[0][15]);
        assertEquals(false, results[0][16]);
        assertEquals(false, results[0][17]);
        assertEquals(false, results[0][18]);
        assertEquals(false, results[0][19]);
        assertEquals(false, results[0][20]);
        assertEquals(false, results[0][21]);
        assertEquals(false, results[0][22]);
        assertEquals(false, results[0][23]);


//        for(int i=0;i<24;i++)
//            System.out.printf("[%d][%d]=%S\n",3,i,results2[3][i]);
//        System.out.println("");
//
//        for(int i=0;i<24;i++)
//            System.out.printf("[%d][%d]=%S\n",4,i,results2[4][i]);
//
//        System.out.println("");
        //true @ all hours on weekend
        for(int i=0;i<24;i++){
            assertEquals(results2[3][i],true);
            assertEquals(results2[4][i],true);
        }


        for(int i=0;i<24;i++)
            System.out.printf("[%d][%d]=%S\n",0,i,results2[0][i]);
        //true @ 1 5 6 7 16 17 18 19 20 21 22 23
        assertEquals(false, results2[0][0]);
        assertEquals(false, results2[0][1]);
        assertEquals(false, results2[0][2]);
        assertEquals(false, results2[0][3]);
        assertEquals(false, results2[0][4]);
        assertEquals(true, results2[0][5]);
        assertEquals(true, results2[0][6]);
        assertEquals(true, results2[0][7]);
        assertEquals(false, results2[0][8]);
        assertEquals(false, results2[0][9]);
        assertEquals(false, results2[0][10]);
        assertEquals(false, results2[0][11]);
        assertEquals(false, results2[0][12]);
        assertEquals(false, results2[0][13]);
        assertEquals(false, results2[0][14]);
        assertEquals(false, results2[0][15]);
        assertEquals(true, results2[0][16]);
        assertEquals(true, results2[0][17]);
        assertEquals(true, results2[0][18]);
        assertEquals(true, results2[0][19]);
        assertEquals(true, results2[0][20]);
        assertEquals(true, results2[0][21]);
        assertEquals(true, results2[0][22]);
        assertEquals(true, results2[0][23]);
//        for(int i=0;i<24;i++){
//            System.out.printf("post masterSearch weekend day: [0][%d]=%S\n",i,results2[0][i]);
//        }
    }

    private void destroyDB(){
        String home = System.getProperty("user.home");
        String target_DB=home+"/cal_app/calDB.db";
        File f = new File(target_DB);
        boolean flag =f.delete();
    }
    private boolean[][] setResultshelper(boolean[][] results){
        results[0][0]= true;
        results[0][1]= false;
        results[0][2]= true;
        results[0][3]= true;
        results[0][4]= false;
        results[0][5]= true;
        results[0][6]= true;
        results[0][7]= true;
        results[0][8]= false;
        results[0][9]= true;
        results[0][10]=true;
        results[0][11]=true;
        results[0][12]=true;
        results[0][13]=false;
        results[0][14]=true;
        results[0][15]=true;
        results[0][16]=true;
        results[0][17]=true;
        results[0][18]=true;
        results[0][19]=false;
        results[0][20]=false;
        results[0][21]=false;
        results[0][22]=false;
        results[0][23]=false;
        for(int i=1;i<7;i++)
            for(int j=0;j<24; j++)
                results[i][j]=true;
        return results;
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
        //no work needed
        if (prefs == null || type == 3
                )
            return results;
            //do the work
        else {
            work_days = prefs.getWorkdays();
            int dayOfWeek = today.getDayOfWeek().getValue();

            //slide work_days according to current day of week, we want it to be parallel with the window we're interested in
            //so if wednesday is our window start, and its a workday, then work_day[0]=true
            for (int i = 0; i < dayOfWeek; i++) {
                temp = work_days[0];
                for (int j = 0; j < 7; j++)
                    if (j == 6) {
                        work_days[j] = temp;
                    } else
                        work_days[j] = work_days[j + 1];
            }

            try {
                wStart = prefs.getwStart_time().getHour();
                wEnd = prefs.getwEnd_time().getHour();

            } catch (Exception e) {
                System.out.println("User has not set preferences, continueing search\n");
                return results;
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