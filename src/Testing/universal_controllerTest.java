package Testing;

import org.junit.Test;

import static frontend.controllers.universal_controller.validate_minutes;
import static frontend.controllers.universal_controller.validate_time;
import static org.junit.Assert.*;

public class universal_controllerTest {

    @Test
    public void testTimeValidator(){
    String badtime = "20:99";
    String goodtime = "20:00";
    String midnight="00:00";
    String noon = "12:00";

    assertEquals(validate_time(badtime),false);
    assertEquals(validate_time(goodtime),true);
    assertEquals(validate_time(midnight),true);
    assertEquals(validate_time(noon),true);
    }

    @Test
    public void testMinutesValidator(){
        String a= "0";
        String b= "60";
        String c= "-1";

        assertEquals(validate_minutes(a),false);
        assertEquals(validate_minutes(b),true);
        assertEquals(validate_minutes(c),false);
    }


}