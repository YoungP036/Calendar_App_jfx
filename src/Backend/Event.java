package Backend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Date;

//start&end time, start&end day, desc, loc, name, type
public class Event {
    private LocalTime sTime;
    private LocalTime eTime;
    private LocalDate sDay;
    private LocalDate eDay;
    private String desc;
    private String loc;
    private String name;
    private boolean workType;
    public Event() throws ParseException {

        sTime=LocalTime.of(0,0);
        eTime=LocalTime.of(0,0);
        sDay =LocalDate.of(1970, Month.JANUARY,1);
        eDay =LocalDate.of(1970, Month.JANUARY,1);
        desc="";
        loc="";
        name="";
        workType=false;
    }

    public Event(LocalTime sTime, LocalTime eTime, LocalDate sDay, LocalDate eDay, String desc, String loc,
                 String name, boolean workType){
        this.sTime=sTime;
        this.eTime=eTime;
        this.sDay=sDay;
        this.eDay=eDay;
        this.desc=desc;
        this.loc=loc;
        this.name=name;
        this.workType=workType;

    }
    public LocalTime getsTime(){
        return sTime;
    }

    public void setsTime(LocalTime sTime) throws ParseException{
        this.sTime=sTime;
    }

    public LocalTime geteTime() {

        return eTime;
    }

    public void seteTime(LocalTime eTime) throws ParseException {

        this.eTime=eTime;
    }

    public LocalDate getsDay() {

        return sDay;
    }

    public void setsDay(LocalDate sDay) {

        this.sDay = sDay;
    }

    public LocalDate geteDay() {

        return eDay;
    }

    public void seteDay(LocalDate eDay)
    {
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
