package Testing;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.MONTH;
import static org.junit.Assert.*;

public class main_controllerTest {

    @Test
    public void testGridCordsToActualDay(){
        //TODO consult calendar and set up some asserts for due date week
    }
    @Test
    public void testGetFirstDay(){
        assertEquals(getFirstDay(0,2018),"Monday");
        assertEquals(getFirstDay(1,2018),"Thursday");
        assertEquals(getFirstDay(10,2017),"Wednesday");
    }
    @Test
    public void testNumDays(){
        assertEquals(getNumDays(0,2018),31);
        assertEquals(getNumDays(1,2018),28);
        assertEquals(getNumDays(11,2017),31);
    }
    private static String getFirstDay(int month, int year){

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayofMonth=cal.getTime();
        DateFormat sdf = new SimpleDateFormat("EEEEEEEE");

        return sdf.format(firstDayofMonth);
    }


    private static int getNumDays(int month, int year){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH,1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}