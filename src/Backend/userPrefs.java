package Backend;

import java.time.LocalTime;

public class userPrefs {
    private boolean[] workdays;
    private LocalTime wStart_time;
    private LocalTime wEnd_time;

    public userPrefs() {
        workdays=new boolean[7];
        for(int i=0;i<7;i++)
            workdays[i]=false;
        wStart_time=LocalTime.of(0,0);
        wEnd_time=LocalTime.of(0,0);
    }
    public userPrefs(boolean[] days, LocalTime start, LocalTime end){
        workdays=days;
        wStart_time=start;
        wEnd_time=end;
    }
    public boolean[] getWorkdays() {
        return workdays;
    }

    public void setWorkdays(boolean[] workdays) {
        this.workdays = workdays;
    }

    public LocalTime getwStart_time() {
        return wStart_time;
    }

    public void setwStart_time(LocalTime wStart_time) {
        this.wStart_time = wStart_time;
    }

    public LocalTime getwEnd_time() {
        return wEnd_time;
    }

    public void setwEnd_time(LocalTime wEnd_time) {
        this.wEnd_time = wEnd_time;
    }
}
