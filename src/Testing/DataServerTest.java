package Testing;

import Backend.DataServer;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class DataServerTest {
    @Before
    public void setUp() throws Exception {
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
        System.out.println("DB deleted: " + flag);
    }


}