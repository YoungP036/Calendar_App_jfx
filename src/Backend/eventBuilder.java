package Backend;

import java.time.LocalDate;
import java.time.LocalTime;

public class eventBuilder
{
    private LocalTime sTime;
    private LocalTime eTime;
    private LocalDate sDay;
    private LocalDate eDay;
    private String desc;
    private String loc;
    private String name;
    private boolean workType;

    public eventBuilder()
    {
    }

    public void setsTime(LocalTime time) {
        this.sTime = time;
    }

    public void seteTime(LocalTime time) {
        this.eTime = time;
    }

    public void setsDay(LocalDate day){
        this.sDay=day;
    }

    public void seteDay(LocalDate day){
        this.eDay=day;
    }

    public void setDesc(String str){
        this.desc=str;
    }
    public void setLoc(String str){
        this.loc=str;
    }
    public void setName(String str){
            this.name=str;
    }
    public void setType(boolean flag){
        this.workType=flag;
    }

    public Event createEvent() {
        return new Event(sTime,eTime,sDay,eDay,desc,loc,name,workType);
    }

}