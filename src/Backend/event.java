package Backend;

import java.sql.Date;
import java.sql.Time;

//start&end time, start&end day, desc, loc, name, type
public class event {
    private Time sTime;
    private Time eTime;
    private Date sDay;
    private Date eDay;
    private String desc;
    private String loc;
    private String name;
    private boolean workType;

    public event() {
    }

    public Time getsTime() {
        return sTime;
    }

    public void setsTime(Time sTime) {
        this.sTime = sTime;
    }

    public Time geteTime() {
        return eTime;
    }

    public void seteTime(Time eTime) {
        this.eTime = eTime;
    }

    public Date getsDay() {
        return sDay;
    }

    public void setsDay(Date sDay) {
        this.sDay = sDay;
    }

    public Date geteDay() {
        return eDay;
    }

    public void seteDay(Date eDay) {
        this.eDay = eDay;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWorkType() {
        return workType;
    }

    public void setWorkType(boolean workType) {
        this.workType = workType;
    }
}
