package frontend.controllers;

import Backend.DataServer;
import Backend.Event;
import frontend.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import javax.annotation.Resources;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

public class main_controller extends universal_controller{

    private static int currMonth,currYear;
    @FXML private Label month_LBL, year_LBL;
    @FXML private GridPane days_pane;
    //one day label per calendar cell
    @FXML private Label dlabel_00, dlabel_01, dlabel_02, dlabel_03, dlabel_04, dlabel_05, dlabel_06;
    @FXML private Label dlabel_07, dlabel_08, dlabel_09, dlabel_010, dlabel_011, dlabel_012, dlabel_013;
    @FXML private Label dlabel_014, dlabel_015, dlabel_016, dlabel_017, dlabel_018, dlabel_019, dlabel_020;
    @FXML private Label dlabel_021, dlabel_022, dlabel_023, dlabel_024, dlabel_025, dlabel_026, dlabel_027;
    @FXML private Label dlabel_028, dlabel_029, dlabel_030, dlabel_031, dlabel_032, dlabel_033, dlabel_034;
    @FXML private Label dlabel_035, dlabel_036, dlabel_037, dlabel_038, dlabel_039, dlabel_040, dlabel_041;
    //6 indicators per calendar cell
//row 1
    @FXML private Pane  indicator_10, indicator_20, indicator_30, indicator_40;
    @FXML private Pane  indicator_11, indicator_21, indicator_31, indicator_41;
    @FXML private Pane  indicator_12, indicator_22, indicator_32, indicator_42;
    @FXML private Pane  indicator_13, indicator_23, indicator_33, indicator_43;
    @FXML private Pane  indicator_14, indicator_24, indicator_34, indicator_44;
    @FXML private Pane  indicator_15, indicator_25, indicator_35, indicator_45;
    @FXML private Pane  indicator_16, indicator_26, indicator_36, indicator_46;
//row 2
    @FXML private Pane indicator_27, indicator_37, indicator_47, indicator_57;
    @FXML private Pane  indicator_18, indicator_28, indicator_38, indicator_48;
    @FXML private Pane  indicator_19, indicator_29, indicator_39, indicator_49;
    @FXML private Pane  indicator_110, indicator_210, indicator_310, indicator_410;
    @FXML private Pane  indicator_111, indicator_211, indicator_311, indicator_411;
    @FXML private Pane  indicator_112, indicator_212, indicator_312, indicator_412;
    @FXML private Pane  indicator_113, indicator_213, indicator_313, indicator_413;
//row 3
    @FXML private Pane  indicator_114, indicator_214, indicator_314, indicator_414;
    @FXML private Pane  indicator_115, indicator_215, indicator_315, indicator_415;
    @FXML private Pane  indicator_116, indicator_216, indicator_316, indicator_416;
    @FXML private Pane  indicator_117, indicator_217, indicator_317, indicator_417;
    @FXML private Pane  indicator_118, indicator_218, indicator_318, indicator_418;
    @FXML private Pane  indicator_119, indicator_219, indicator_319, indicator_419;
    @FXML private Pane  indicator_120, indicator_220, indicator_320, indicator_420;
//row 4
    @FXML private Pane  indicator_121, indicator_221, indicator_321, indicator_421;
    @FXML private Pane  indicator_122, indicator_222, indicator_322, indicator_422;
    @FXML private Pane  indicator_123, indicator_223, indicator_323, indicator_423;
    @FXML private Pane  indicator_124, indicator_224, indicator_324, indicator_424;
    @FXML private Pane  indicator_125, indicator_225, indicator_325, indicator_425;
    @FXML private Pane  indicator_126, indicator_226, indicator_326, indicator_426;
    @FXML private Pane  indicator_127, indicator_227, indicator_327, indicator_427;
//row 5
    @FXML private Pane  indicator_128, indicator_228, indicator_328, indicator_428;
    @FXML private Pane  indicator_129, indicator_229, indicator_329, indicator_429;
    @FXML private Pane  indicator_130, indicator_230, indicator_330, indicator_430;
    @FXML private Pane  indicator_131, indicator_231, indicator_331, indicator_431;
    @FXML private Pane  indicator_132, indicator_232, indicator_332, indicator_432;
    @FXML private Pane  indicator_133, indicator_233, indicator_333, indicator_433;
    @FXML private Pane  indicator_134, indicator_234, indicator_334, indicator_434;
//row 6
    @FXML private Pane  indicator_135, indicator_235, indicator_335, indicator_435;
    @FXML private Pane  indicator_136, indicator_236, indicator_336, indicator_436;
    @FXML private Pane  indicator_137, indicator_237, indicator_337, indicator_437;
    @FXML private Pane  indicator_138, indicator_238, indicator_338, indicator_438;
    @FXML private Pane  indicator_139, indicator_239, indicator_339, indicator_439;
    @FXML private Pane  indicator_140, indicator_240, indicator_340, indicator_440;
    @FXML private Pane  indicator_141, indicator_241, indicator_341, indicator_441;

//    @FXML private Button prev_month_BTN, next_month_BTN;
    private static Pane[][][] indicators;
    private static Label[] day_labels;
    private int selected_day_row;
    private int selected_day_col;
    private int selected_indicator;


    @FXML
    private void initialize(){
        indicators = new Pane[6][7][4];
        day_labels = new Label[42];

        day_labels[0]=dlabel_00; day_labels[1]=dlabel_01; day_labels[2]=dlabel_02; day_labels[3]=dlabel_03; day_labels[4]=dlabel_04; day_labels[5]=dlabel_05; day_labels[6]=dlabel_06;
        day_labels[7]=dlabel_07; day_labels[8]=dlabel_08; day_labels[9]=dlabel_09; day_labels[10]=dlabel_010; day_labels[11]=dlabel_011; day_labels[12]=dlabel_012; day_labels[13]=dlabel_013;
        day_labels[14]=dlabel_014; day_labels[15]=dlabel_015; day_labels[16]=dlabel_016; day_labels[17]=dlabel_017; day_labels[18]=dlabel_018; day_labels[19]=dlabel_019; day_labels[20]=dlabel_020;
        day_labels[21]=dlabel_021; day_labels[22]=dlabel_022; day_labels[23]=dlabel_023; day_labels[24]=dlabel_024; day_labels[25]=dlabel_025; day_labels[26]=dlabel_026; day_labels[27]=dlabel_027;
        day_labels[28]=dlabel_028; day_labels[29]=dlabel_029; day_labels[30]=dlabel_030; day_labels[31]=dlabel_031; day_labels[32]=dlabel_032; day_labels[33]=dlabel_033; day_labels[34]=dlabel_034;
        day_labels[35]=dlabel_035; day_labels[36]=dlabel_036; day_labels[37]=dlabel_037; day_labels[38]=dlabel_038; day_labels[39]=dlabel_039; day_labels[40]=dlabel_040; day_labels[41]=dlabel_041;
        //row 1
        indicators[0][0][0]=indicator_10; indicators[0][0][1]=indicator_20; indicators[0][0][2]=indicator_30; indicators[0][0][3]=indicator_40;
        indicators[0][1][0]=indicator_11; indicators[0][1][1]=indicator_21; indicators[0][1][2]=indicator_31; indicators[0][1][3]=indicator_41;
        indicators[0][2][0]=indicator_12; indicators[0][2][1]=indicator_22; indicators[0][2][2]=indicator_32; indicators[0][2][3]=indicator_42;
        indicators[0][3][0]=indicator_13; indicators[0][3][1]=indicator_23; indicators[0][3][2]=indicator_33; indicators[0][3][3]=indicator_43;
        indicators[0][4][0]=indicator_14; indicators[0][4][1]=indicator_24; indicators[0][4][2]=indicator_34; indicators[0][4][3]=indicator_44;
        indicators[0][5][0]=indicator_15; indicators[0][5][1]=indicator_25; indicators[0][5][2]=indicator_35; indicators[0][5][3]=indicator_45;
        indicators[0][6][0]=indicator_16; indicators[0][6][1]=indicator_26; indicators[0][6][2]=indicator_36; indicators[0][6][3]=indicator_46;

        //row 2
        indicators[1][0][0]=indicator_27; indicators[1][0][1]=indicator_37; indicators[1][0][2]=indicator_47; indicators[1][0][3]=indicator_57;
        indicators[1][1][0]=indicator_18; indicators[1][1][1]=indicator_28; indicators[1][1][2]=indicator_38; indicators[1][1][3]=indicator_48;
        indicators[1][2][0]=indicator_19; indicators[1][2][1]=indicator_29; indicators[1][2][2]=indicator_39; indicators[1][2][3]=indicator_49;
        indicators[1][3][0]=indicator_110; indicators[1][3][1]=indicator_210; indicators[1][3][2]=indicator_310; indicators[1][3][3]=indicator_410;
        indicators[1][4][0]=indicator_111; indicators[1][4][1]=indicator_211; indicators[1][4][2]=indicator_311; indicators[1][4][3]=indicator_411;
        indicators[1][5][0]=indicator_112; indicators[1][5][1]=indicator_212; indicators[1][5][2]=indicator_312; indicators[1][5][3]=indicator_412;
        indicators[1][6][0]=indicator_113; indicators[1][6][1]=indicator_213; indicators[1][6][2]=indicator_313; indicators[1][6][3]=indicator_413;

        //row 3
        indicators[2][0][0]=indicator_114; indicators[2][0][1]=indicator_214; indicators[2][0][2]=indicator_314; indicators[2][0][3]=indicator_414;
        indicators[2][1][0]=indicator_115; indicators[2][1][1]=indicator_215; indicators[2][1][2]=indicator_315; indicators[2][1][3]=indicator_415;
        indicators[2][2][0]=indicator_116; indicators[2][2][1]=indicator_216; indicators[2][2][2]=indicator_316; indicators[2][2][3]=indicator_416;
        indicators[2][3][0]=indicator_117; indicators[2][3][1]=indicator_217; indicators[2][3][2]=indicator_317; indicators[2][3][3]=indicator_417;
        indicators[2][4][0]=indicator_118; indicators[2][4][1]=indicator_218; indicators[2][4][2]=indicator_318; indicators[2][4][3]=indicator_418;
        indicators[2][5][0]=indicator_119; indicators[2][5][1]=indicator_219; indicators[2][5][2]=indicator_319; indicators[2][5][3]=indicator_419;
        indicators[2][6][0]=indicator_120; indicators[2][6][1]=indicator_220; indicators[2][6][2]=indicator_320; indicators[2][6][3]=indicator_420;

        // row 4
        indicators[3][0][0]=indicator_121; indicators[3][0][1]=indicator_221; indicators[3][0][2]=indicator_321; indicators[3][0][3]=indicator_421;
        indicators[3][1][0]=indicator_122; indicators[3][1][1]=indicator_222; indicators[3][1][2]=indicator_322; indicators[3][1][3]=indicator_422;
        indicators[3][2][0]=indicator_123; indicators[3][2][1]=indicator_223; indicators[3][2][2]=indicator_323; indicators[3][2][3]=indicator_423;
        indicators[3][3][0]=indicator_124; indicators[3][3][1]=indicator_224; indicators[3][3][2]=indicator_324; indicators[3][3][3]=indicator_424;
        indicators[3][4][0]=indicator_125; indicators[3][4][1]=indicator_225; indicators[3][4][2]=indicator_325; indicators[3][4][3]=indicator_425;
        indicators[3][5][0]=indicator_126; indicators[3][5][1]=indicator_226; indicators[3][5][2]=indicator_326; indicators[3][5][3]=indicator_426;
        indicators[3][6][0]=indicator_127; indicators[3][6][1]=indicator_227; indicators[3][6][2]=indicator_327; indicators[3][6][3]=indicator_427;

        //row 5
        indicators[4][0][0]=indicator_128; indicators[4][0][1]=indicator_228; indicators[4][0][2]=indicator_328; indicators[4][0][3]=indicator_428;
        indicators[4][1][0]=indicator_129; indicators[4][1][1]=indicator_229; indicators[4][1][2]=indicator_329; indicators[4][1][3]=indicator_429;
        indicators[4][2][0]=indicator_130; indicators[4][2][1]=indicator_230; indicators[4][2][2]=indicator_330; indicators[4][2][3]=indicator_430;
        indicators[4][3][0]=indicator_131; indicators[4][3][1]=indicator_231; indicators[4][3][2]=indicator_331; indicators[4][3][3]=indicator_431;
        indicators[4][4][0]=indicator_132; indicators[4][4][1]=indicator_232; indicators[4][4][2]=indicator_332; indicators[4][4][3]=indicator_432;
        indicators[4][5][0]=indicator_133; indicators[4][5][1]=indicator_233; indicators[4][5][2]=indicator_333; indicators[4][5][3]=indicator_433;
        indicators[4][6][0]=indicator_134; indicators[4][6][1]=indicator_234; indicators[4][6][2]=indicator_334; indicators[4][6][3]=indicator_434;
        //row 6
        indicators[5][0][0]=indicator_135; indicators[5][0][1]=indicator_235; indicators[5][0][2]=indicator_335; indicators[5][0][3]=indicator_435;
        indicators[5][1][0]=indicator_136; indicators[5][1][1]=indicator_236; indicators[5][1][2]=indicator_336; indicators[5][1][3]=indicator_436;
        indicators[5][2][0]=indicator_137; indicators[5][2][1]=indicator_237; indicators[5][2][2]=indicator_337; indicators[5][2][3]=indicator_437;
        indicators[5][3][0]=indicator_138; indicators[5][3][1]=indicator_238; indicators[5][3][2]=indicator_338; indicators[5][3][3]=indicator_438;
        indicators[5][4][0]=indicator_139; indicators[5][4][1]=indicator_239; indicators[5][4][2]=indicator_339; indicators[5][4][3]=indicator_439;
        indicators[5][5][0]=indicator_140; indicators[5][5][1]=indicator_240; indicators[5][5][2]=indicator_340; indicators[5][5][3]=indicator_440;
        indicators[5][6][0]=indicator_141; indicators[5][6][1]=indicator_241; indicators[5][6][2]=indicator_341; indicators[5][6][3]=indicator_441;

       //get and set current date to set  month, year, and day labels
        DataServer.init();
        LocalDate now = LocalDate.now();
        String year = Integer.toString(now.getYear());
        currYear=now.getYear();
        String monthStr= now.getMonth().toString();
        currMonth=now.getMonth().getValue()-1;
        month_LBL.setText(monthStr);
        year_LBL.setText(year);
        setCalendarCellLabels(now.getMonth().getValue()-1,now.getYear());
        reset_indicators();
        updateIndicators();
    }
    @FXML private static void reset_indicators(){
        for(int i=0;i<6;i++)
            for(int j=0;j<7;j++)
                for(int k=0;k<4;k++)
                    indicators[i][j][k].setStyle("-fx-background-color: #00cc00; -fx-border-color: #00cc00");

    }



    @FXML protected static void updateIndicators(){
        reset_indicators();
        Event[] events = DataServer.getAllEvent();
        int indicator=-1;
        int numEvents= Array.getLength(events);
        String firstDayStr = getFirstDay(currMonth,currYear);
        int firstDay=-1;
        switch(firstDayStr) {
            case "Sunday":
                firstDay = 0;
                break;
            case "Monday":
                firstDay = 1;
                break;
            case "Tuesday":
                firstDay = 2;
                break;
            case "Wednesday":
                firstDay = 3;
                break;
            case "Thursday":
                firstDay = 4;
                break;
            case "Friday":
                firstDay = 5;
                break;
            case "Saturday":
                firstDay = 6;
                break;
        }

        for(int i=0;i<numEvents;i++){
            LocalTime sTime=events[i].getsTime();
            LocalDate sDay=events[i].getsDay();
            int year =sDay.getYear();
            int month =sDay.getMonth().getValue()-1;
            int day = sDay.getDayOfMonth();
            int hour=sTime.getHour();


            if(year!=currYear || month!=currMonth)
                continue;

            //convert to 'relative day'
            day=day+firstDay-1;
            int row=0;
            while(day>6){
                day-=7;
                row++;
            }
            if(hour>=0 && hour <=6)
                indicator=0;
            else if(hour>6 && hour <=12)
                indicator=1;
            else if(hour>12 && hour <=18)
                indicator=2;
            else if(hour>18 && hour<=24)
                indicator=3;
            String style=indicators[row][day][indicator].getStyle().toString();
            if(events[i].isWorkType()){
                style=style.replaceAll("-fx-background-color: #[0-9a-f]+", "-fx-background-color: #ccff00");
                indicators[row][day][indicator].setStyle(style);
            }
            else {
                style=style.replaceAll("-fx-background-color: #[0-9a-f]+", "-fx-background-color: #000000");
                indicators[row][day][indicator].setStyle(style);
            }
        }
        System.out.println("DBG indicators updated");
    }
    @FXML private void selected_event(MouseEvent e){
        Node source  = (Node)e.getSource();
        Node parent= source.getParent().getParent();
        int prev_day_col, prev_day_row, prev_ind;
        Integer child_row = days_pane.getRowIndex(source);
        Integer parent_row = days_pane.getRowIndex(parent);
        Integer parent_col = days_pane.getColumnIndex(parent);
        if(parent_row==null)
            parent_row=0;
        if(parent_col==null)
            parent_col=0;
        if(child_row==null)
            child_row=0;
        prev_day_col=selected_day_col;
        prev_day_row=selected_day_row;
        prev_ind=selected_indicator;
        selected_indicator=child_row;
        selected_day_col=parent_col;
        selected_day_row=parent_row-1;//for some reason rows are starting at 1, everything else is starting at 0

        System.out.printf("DBG Mouse clicked indicator %d in cell [%d,%d]\n",child_row.intValue(),parent_row.intValue(),parent_col.intValue());

        //deselect anything that was previously selected
        String prev_style=indicators[prev_day_row][prev_day_col][prev_ind].getStyle().toString();
        prev_style=prev_style.replaceAll("-fx-border-color: #[0-9a-f]+","-fx-border-color: #00cc00");
        indicators[prev_day_row][prev_day_col][prev_ind].setStyle(prev_style);

        //handle case where cell clicked is identical to last cell clicked, 2 sub cases, even number clicks and odd number clicks
        if(prev_day_row==selected_day_row&& prev_day_col==selected_day_col&&prev_ind==selected_indicator)
            //TODO if border is green its a select, leave globals
            //else its a deselect, set globals to OOB
            return;
        //normal case where clicked cell is different from last clicked cell
        else{
            prev_style=indicators[selected_day_row][selected_day_col][selected_indicator].getStyle().toString();
            System.out.println("prev style: " + prev_style);
            prev_style=prev_style.replaceAll("-fx-border-color: #[0-9a-f]+", "-fx-border-color: #6600ff");
            System.out.println("new style: " + prev_style);
            indicators[selected_day_row][selected_day_col][selected_indicator].setStyle(prev_style);
        }
    }

    @FXML
    private void exportEvents(){
        String name,desc,loc,sTime,eTime,sDay,eDay,type;
        Event[] events = DataServer.getAllEvent();
        String home = System.getProperty("user.home");
        String str="";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(home+"/cal_app/events.txt"));
            for (int i = 0; i < Array.getLength(events); i++) {
                str="";
                System.out.println("iter " + Integer.toString(i));
                str = str+events[i].getName();
                desc = events[i].getDesc();
                if(desc.compareTo("")!=0)
                    str=str+","+desc;
                loc = events[i].getLoc();
                if(loc.compareTo("")!=0)
                    str=str+","+loc;

                str=str+","+events[i].getsTime().toString()+","+events[i].geteTime().toString()+","+events[i].getsDay().toString()+","+events[i].geteDay().toString();

                if (events[i].isWorkType())
                    str= str+",work";
                else
                    str = str+",personal";
                str="("+str+")\n";
                writer.write(str);
            }
            writer.close();
        }
            catch(IOException e){
                System.out.println("Export Events error: "+e.getMessage());
            }
    }

    @FXML
    private void monthBackBTN(){
        if(currMonth==0){
            currMonth=11;
            currYear--;
        }
        else
            currMonth--;
       setCalendarCellLabels(currMonth,currYear);
       setMonthYearLabels(month_LBL,year_LBL);
       updateIndicators();
    }

    @FXML
    private void monthForwardBTN(){
        if(currMonth==11){
            currMonth=0;
            currYear++;
        }
        else
            currMonth++;

        setCalendarCellLabels(currMonth,currYear);
        setMonthYearLabels(month_LBL, year_LBL);
        updateIndicators();
    }

    @FXML
    private static void setMonthYearLabels(Label month_LBL, Label year_LBL){
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months=dfs.getMonths();
        String month = months[currMonth];

        month_LBL.setText(month);
        year_LBL.setText(Integer.toString(currYear));
    }

    private static void setCalendarCellLabels(int month, int year){
        String firstDayStr = getFirstDay(month,year);
        System.out.println(firstDayStr);
        int firstDay=-1;
        int numDays=getNumDays(month,year);
//        System.out.println("DBG NumDays="+numDays);
        switch(firstDayStr) {
            case "Sunday":
                firstDay = 0;
                break;
            case "Monday":
                firstDay = 1;
                break;
            case "Tuesday":
                firstDay = 2;
                break;
            case "Wednesday":
                firstDay = 3;
                break;
            case "Thursday":
                firstDay = 4;
                break;
            case "Friday":
                firstDay = 5;
                break;
            case "Saturday":
                firstDay = 6;
                break;
        }

        if(firstDay==-1){
            System.out.println("DBG firstDay error\n");
            return;
        }
        int currDay=1;
        for(int i=0;i<firstDay;i++)
            day_labels[i].setText("");
        for(int i=firstDay;i<numDays+firstDay;i++,currDay++)
            day_labels[i].setText(Integer.toString(currDay));
        for(int i=41;i>=firstDay+numDays;i--)
            day_labels[i].setText("");
    }


    @FXML private void open_pref(){
        new_window(Main.screenList.get(1));
    }
    @FXML private void open_create(){new_window(Main.screenList.get(2)); }
    @FXML private void open_search(){new_window(Main.screenList.get(3));}



    @FXML private void delete_event() throws InterruptedException {
        if (selected_day_col != -1 && selected_day_row != -1 && selected_indicator != -1) {
            LocalTime min_time=null;
            LocalTime max_time=null;
            switch (selected_indicator) {
                case 0:
                    min_time = LocalTime.parse("00:00");
                    max_time = LocalTime.parse("06:00");
                    break;
                case 1:
                    min_time=LocalTime.parse("06:01");
                    max_time=LocalTime.parse("12:00");
                    break;
                case 2:
                    min_time=LocalTime.parse("12:01");
                    max_time=LocalTime.parse("18:00");
                    break;
                case 3:
                    min_time=LocalTime.parse("18:01");
                    max_time=LocalTime.parse("23:59");
                    break;
            }
            int target_day=gridCordsToDayActual(currMonth,currYear, selected_day_row, selected_day_col);
            String monthString, dayString;
            LocalDate target_date;
            //if day or month is single digit, must prepend a 0 to match LocalDate/LocalTime formats
            if(currMonth<10)
                monthString="0"+Integer.toString(currMonth+1);
            else
                monthString=Integer.toString(currMonth+1);
            if(target_day<10)
                dayString="0"+Integer.toString(target_day);
            else
                dayString=Integer.toString(target_day);
            target_date=LocalDate.parse(Integer.toString(currYear)+"-"+monthString+"-"+dayString);
            System.out.println("target_date="+target_date.toString());
            DataServer.deleteEventRange(target_date, min_time, max_time);
            Thread.sleep(100);
            updateIndicators();
        }
    }

    //given a row/col cordinate, use currMonth/currYear to compute actual day of month
    private int day_to_offset(String day){
        switch(day){
            case "Sunday":
                return 0;
            case "Monday":
                return 1;
            case "Tuesday":
                return 2;
            case "Wednesday":
                return 3;
            case "Thursday":
                return 4;
            case "Friday":
                return 5;
            case "Saturday":
                return 6;
            default:
                System.out.println("Day to offset returned -1");
                return -1;
        }
    }
    private int gridCordsToDayActual(int month, int year, int row, int col){
        int dayActual=-1;
        String firstDay=getFirstDay(month,year);
        int offset=day_to_offset(firstDay);
        dayActual=(row)*7; //multiply by 7 for 7 days per row/week
        dayActual+=col;//add day/col of week
        dayActual-=offset-1; //compensate for blank spaces in row 1 assosciated with where 1st week of month starts
        return dayActual;
    }

    @FXML
    private void delete_all_events(){
        Event[] events=DataServer.getAllEvent();
        String sDay, eDay, sTime, eTime;
        for(int i=0;i<Array.getLength(events);i++) {
            sDay=events[i].getsDay().toString();
            eDay=events[i].geteDay().toString();
            sTime=events[i].getsTime().toString();
            eTime=events[i].geteTime().toString();
            DataServer.deleteEvent(sTime,eTime,sDay,eDay);
        }
        updateIndicators();
    }
}
