package Testing;

import Backend.Event;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.Assert.*;

public class EventTest {
   private Event constructortestEvent;
   private Event testEvent;
   private LocalTime testTime;
   private LocalDate testDate;
    @Before
    public void setUp() throws ParseException{
        constructortestEvent=new Event();
        testEvent=new Event();
    }

    @Test
    public void eventConstructorTest() throws ParseException {
        assertEquals(constructortestEvent.getsTime().toString(),"00:00");
        assertEquals(constructortestEvent.geteTime().toString(),"00:00");
        assertEquals(constructortestEvent.getsDay().toString(),"1970-01-01");
        assertEquals(constructortestEvent.geteDay().toString(),"1970-01-01");
        assertEquals(constructortestEvent.getDesc(),"");
        assertEquals(constructortestEvent.getLoc(),"");
        assertEquals(constructortestEvent.getName(),"");
        assertEquals(constructortestEvent.isWorkType(),false);
    }

    @Test
    public void testTimes() throws Exception {
        testTime=LocalTime.of(5,15);
        testEvent.seteTime(testTime);
        assertEquals(testEvent.geteTime().toString(),"05:15");
    }

    @Test
    public void testDates() throws Exception {
        testDate=LocalDate.of(2000,Month.MARCH,5);
        testEvent.seteDay(testDate);
        assertEquals(testEvent.geteDay().toString(),"2000-03-05");
    }

    public static class concreteEventBuilderTest {
        @Before
        public void setUp() throws Exception {
        }

    }
}