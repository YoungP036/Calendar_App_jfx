package Testing;

import Backend.Event;
import Backend.eventBuilder;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.Assert.*;

public class eventBuilderTest {

    private eventBuilder eb = new eventBuilder();
    private Event testEvent;

    @Test
    public void testTimes() throws Exception {
        LocalTime time;
        LocalDate date;

        time=LocalTime.of(5,15);
        eb.setsTime(time);
        eb.seteTime(time);

        date=LocalDate.of(2000, Month.MARCH,5);
        eb.setsDay(date);
        eb.seteDay(date);

        eb.setDesc("desc");
        eb.setLoc("loc");
        eb.setName("name");
        eb.setType(true);
        testEvent=eb.createEvent();

        assertEquals(testEvent.getsTime().toString(),"05:15");
        assertEquals(testEvent.geteTime().toString(),"05:15");
        assertEquals(testEvent.getsDay().toString(),"2000-03-05");
        assertEquals(testEvent.geteDay().toString(),"2000-03-05");
        assertEquals(testEvent.getDesc(),"desc");
        assertEquals(testEvent.getLoc(),"loc");
        assertEquals(testEvent.getName(),"name");
        assertEquals(testEvent.isWorkType(),true);
    }
}