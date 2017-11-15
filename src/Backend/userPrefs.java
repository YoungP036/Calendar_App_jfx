package Backend;

import java.sql.Time;

public class userPrefs {
    private boolean workdays[];
    private Time wStart_time;
    private Time wEnd_time;

    public userPrefs() {
        for(int i=0;i<7;i++) {
            workdays[i] = false;
        }
    }

    public boolean[] getWorkdays() {
        return workdays;
    }

    public void setWorkdays(boolean[] workdays) {
        this.workdays = workdays;
    }

    public Time getwStart_time() {
        return wStart_time;
    }

    public void setwStart_time(Time wStart_time) {
        this.wStart_time = wStart_time;
    }

    public Time getwEnd_time() {
        return wEnd_time;
    }

    public void setwEnd_time(Time wEnd_time) {
        this.wEnd_time = wEnd_time;
    }
}
