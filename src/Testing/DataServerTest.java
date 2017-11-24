package Testing;

import Backend.DataServer;
import Backend.Event;
import Backend.eventBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.Assert.*;

public class DataServerTest {
    private eventBuilder eb;
    private Event testEvent;
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
        date=LocalDate.of(2000, Month.MARCH,5);
        eb.setsDay(date);
        eb.seteDay(date);
        eb.setDesc("testDesc");
        eb.setLoc("testLoc");
        eb.setName("testName");
        eb.setType(true);
        testEvent=eb.createEvent();
    }

    //furthur verified by running cat on the database file, no get event method to verify programmatically ATM
    @Test
    public void testSaveEvent(){
        destroyDB();
        DataServer.init();
        assertEquals(DataServer.saveEvent(testEvent),0);
    }

    @Test
    public void testSaveAndReadEvent(){
        destroyDB();
        DataServer.init();
        DataServer.saveEvent(testEvent);
        DataServer.saveEvent(testEvent);
        eventArr=DataServer.getAllEvent();
        assertEquals(eventArr[1].getName(),"testName");
        assertEquals(eventArr[0].getName(),"testName");
        assertEquals(eventArr[0].getsTime().toString(),"05:15");
        assertEquals(eventArr[0].getsDay().toString(),"2000-03-05");
        assertEquals(eventArr[0].isWorkType(), true);
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