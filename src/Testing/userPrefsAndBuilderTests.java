package Testing;

import Backend.prefsBuilder;
import Backend.userPrefs;
import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.*;

public class userPrefsAndBuilderTests {
    private userPrefs testPrefs1, testPrefs2,testPrefs3;
    private prefsBuilder pb=new prefsBuilder();
    LocalTime time;
    boolean[] flags=new boolean[7];
    @Test
    public void testConstructors() throws Exception {
        testPrefs1=new userPrefs();

        time= LocalTime.of(0,0);
        for(int i=0;i<7;i++) {
            flags[i]=false;
            assertEquals(testPrefs1.getWorkdays()[i], false);
        }
        assertEquals(testPrefs1.getwStart_time().toString(),"00:00");
        assertEquals(testPrefs1.getwEnd_time().toString(),"00:00");

        testPrefs2=new userPrefs(flags,time,time);
        for(int i=0;i<7;i++)
            assertEquals(testPrefs2.getWorkdays()[i],false);
        assertEquals(testPrefs2.getwStart_time().toString(),"00:00");
        assertEquals(testPrefs2.getwEnd_time().toString(),"00:00");
    }

    @Test
    public void testBuilder() throws Exception{
        time= LocalTime.of(0,0);
        pb.setDays(flags);
        pb.setStart(time);
        pb.setEnd(time);
        testPrefs3=pb.createPrefs();
        for(int i=0;i<7;i++)
            assertEquals(testPrefs3.getWorkdays()[i],false);
        assertEquals(testPrefs3.getwStart_time().toString(),"00:00");
        assertEquals(testPrefs3.getwEnd_time().toString(),"00:00");
    }
}