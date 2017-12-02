package Testing;

import javafx.fxml.FXML;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.MONTH;
import static org.junit.Assert.*;

public class controllerTests {

    @Test
    public void testGridCordsToActualDay(){
        assertEquals(gridCordsToDayActual(11, 2017, 1,5),1);
        assertEquals(gridCordsToDayActual(11, 2017, 2,0),3);
        assertEquals(gridCordsToDayActual(11, 2017, 6,0),31);
        assertEquals(gridCordsToDayActual(11, 2017, 5,6),30);
        assertEquals(gridCordsToDayActual(11, 2017, 4,3),20);
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

    //copied private methods into test class so i dont have to needlessly make them public, or implement reflection
    private static int getNumDays(int month, int year){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH,1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
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

    private int gridCordsToDayActual(int month, int year, int row, int col){

        return 0;
    }
}