package Backend;

import java.time.LocalTime;

public class prefsBuilder {
    private boolean[] workdays;
    private LocalTime wStart_time;
    private LocalTime wEnd_time;

    public prefsBuilder(){}

    public void setDays(boolean[] days){
        workdays=days;
    }

    public void setStart(LocalTime time){
        wStart_time=time;
    }

    public void setEnd(LocalTime time){
        wEnd_time=time;
    }

    public userPrefs createPrefs(){
        return new userPrefs(workdays,wStart_time, wEnd_time);
    }
}
