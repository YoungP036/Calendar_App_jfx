package Testing;

import Backend.DataServer;
import Backend.prefsBuilder;
import Backend.userPrefs;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Array;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class search_controllerTest {
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

    private boolean[][] eliminate_by_eType(boolean[][] results, int type){

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