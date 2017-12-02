package Testing;

import Backend.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.Assert.*;

public class DataServerTest {
    private eventBuilder eb;
    private Event testEvent,testEvent2;
    private Event eventIn;
    private Event eventArr[];
    private LocalTime time;
    private LocalDate date;

    @Before
    public void setUp() throws Exception {
        eb = new eventBuilder();
        time=LocalTime.of(5,15);
        eb.setsTime(time);
        eb.seteTime(time);
        eb.setsDay(LocalDate.parse("2000-03-05"));
        eb.seteDay(LocalDate.parse("2000-03-05"));
        eb.setDesc("eventDesc1");
        eb.setLoc("eventLoc1");
        eb.setName("eventName1");
        eb.setType(true);
        testEvent=eb.createEvent();

        eb.setsDay(LocalDate.parse("2005-01-01"));
        eb.seteDay(LocalDate.parse("2005-01-01"));
        time=LocalTime.of(10,10);
        eb.setsTime(time);
        eb.seteTime(time);
        eb.setDesc("eventDesc2");
        eb.setLoc("eventLoc2");
        eb.setName("eventName2");
        eb.setType(false);
        testEvent2=eb.createEvent();
    }

    @Test public void testSaveReadPrefs(){
        userPrefs prefs,prefs_out;
        boolean[] days=new boolean[7];
        prefsBuilder pb = new prefsBuilder();
        pb.setStart(LocalTime.parse("08:00"));
        pb.setEnd(LocalTime.parse("04:00"));
        for(int i=0;i<7;i++){
            if(i%2==0)
                days[i]=true;
            else
                days[i]=false;
        }

        pb.setDays(days);
        prefs=pb.createPrefs();
        destroyDB();
        DataServer.init();
        DataServer.savePrefs(prefs);
        DataServer.savePrefs(prefs);
        DataServer.savePrefs(prefs);
        prefs_out=DataServer.getPrefs();
        assertEquals(prefs_out.getwStart_time(),prefs.getwStart_time());
        assertEquals(prefs_out.getwEnd_time(),prefs.getwEnd_time());
        for(int i=0;i<7;i++){
            if(i%2==0)
                assertEquals(prefs_out.getWorkdays()[i],true);
            else
                assertEquals(prefs_out.getWorkdays()[i],false);
        }
    }
    @Test public void testDeleteEvent(){
        destroyDB();
        DataServer.init();
        DataServer.saveEvent(testEvent);
        DataServer.saveEvent(testEvent2);
        String sTime="05:15";
        String eTime="05:15";
        String sDay="2000-03-05";
        String eDay="2000-03-05";

        DataServer.deleteEvent( sTime,  eTime,  sDay,  eDay);
        Event[] events = DataServer.getAllEvent();
        assertNotEquals(events[0].getName(),"eventName1");
        assertEquals(events[0].getName(),"eventName2");
    }

    @Test public void testDeleteEventRange(){
        destroyDB();
        DataServer.init();

        Event testEvent3, testEvent4, testEvent5, testEvent6;

        eb.setName("testEvent3");
        eb.setsDay(LocalDate.parse("2005-01-01"));
        eb.setsDay(LocalDate.parse("2005-01-01"));
        eb.setsTime(LocalTime.parse("01:00"));
        eb.setsTime(LocalTime.parse("02:00"));
        testEvent3=eb.createEvent();

        eb.setsTime(LocalTime.parse("03:00"));
        eb.seteTime(LocalTime.parse("04:00"));
        eb.setName("testEvent4");
        testEvent4=eb.createEvent();

        eb.setsTime(LocalTime.parse("06:00"));
        eb.seteTime(LocalTime.parse("07:00"));
        eb.setName("testEvent5");
        testEvent5=eb.createEvent();

        eb.setsDay(LocalDate.parse("2005-01-02"));
        eb.seteDay(LocalDate.parse("2005-01-02"));
        eb.setsTime(LocalTime.parse("01:00"));
        eb.seteTime(LocalTime.parse("02:00"));
        eb.setName("testEvent6");
        testEvent6=eb.createEvent();

        DataServer.saveEvent(testEvent3);//should get deleted
        DataServer.saveEvent(testEvent5);
        DataServer.saveEvent(testEvent4);//should get deleted
        DataServer.saveEvent(testEvent6);
        DataServer.deleteEventRange(LocalDate.parse("2005-01-01"), LocalTime.parse("00:00"), LocalTime.parse("05:59"));
        Event[] remaining_events = DataServer.getAllEvent();

        //should be deleted -> e3: 2005/1/1, 1-2
        //should be deleted -> e4: 2005/1/1, 3-4
        //should exist -> e5: 2005/1/1, 6-7
        //should exist -> e6: 2005/1/2, 1-2
        assertNotEquals(remaining_events[0].getName().toString(),"testEvent3");
        assertNotEquals(remaining_events[0].getName().toString(),"testEvent4");
        assertNotEquals(remaining_events[1].getName().toString(),"testEvent3");
        assertNotEquals(remaining_events[1].getName().toString(),"testEvent4");
        assertEquals(remaining_events[0].getName().toString(), "testEvent5");
        assertEquals(remaining_events[1].getName().toString(), "testEvent6");

    }
    /*
            currently useless, probably abandoning the method this tests
     */
//    @Test
//    public void testGetOneEvent(){
//        destroyDB();
//        LocalDate date = LocalDate.parse("2000-03-05");
//        DataServer.init();
//        DataServer.saveEvent(testEvent);
//        DataServer.saveEvent(testEvent2);
//        Event outputEvent = DataServer.getEvent(date);
//        assertEquals(outputEvent.getsDay().toString(), date.toString());
//        assertEquals(outputEvent.getName(), "eventName1");
//    }
    @Test
    public void testSaveEvent(){
        destroyDB();
        DataServer.init();
        assertEquals(DataServer.saveEvent(testEvent),0);
    }

    @Test
    public void testSaveAndgetAllEvent(){
        destroyDB();
        DataServer.init();
        DataServer.saveEvent(testEvent);
        DataServer.saveEvent(testEvent2);
        eventArr=DataServer.getAllEvent();
        System.out.println("index 0 name : " + eventArr[0].getName());
        System.out.println("index 1 name: " + eventArr[1].getName());
        assertEquals(eventArr[0].getName(),"eventName1");
        assertEquals(eventArr[0].getsTime().toString(),"05:15");
        assertEquals(eventArr[0].getsDay().toString(),"2000-03-05");
        assertEquals(eventArr[0].isWorkType(), true);

        assertEquals(eventArr[1].getName(),"eventName2");
        assertEquals(eventArr[1].getsTime().toString(),"10:10");
        assertEquals(eventArr[1].getsDay().toString(),"2005-01-01");
        assertEquals(eventArr[1].isWorkType(), false);
    }
    @Test
    public void testInit(){
//test both for when DB exists and when it does not
        destroyDB();
        assertEquals(DataServer.init(),0);
        assertEquals(DataServer.init(),0);
    }

    private void destroyDB(){
        String home = System.getProperty("user.home");
        String target_DB=home+"/cal_app/calDB.db";
        File f = new File(target_DB);
        boolean flag =f.delete();
//        System.out.println("DB deleted: " + flag);
    }


}