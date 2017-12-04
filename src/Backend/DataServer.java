package Backend;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.lang.reflect.Array;

/*REFACTOR TODOS FOR IF ALL USER STORIES COMPLETE
    1. review all method parameters that are strings, chances are they should be LocalDate, or LocalTime
        - lots of pointless toString()ing going on

*/
public class DataServer {
    public static int init() {
        //check for and create DB if needed
        check_and_make_dir();
        if (check_and_make_DB() != 0) {
            System.out.println("DB init failure");
            return -1;
        }

        //check for and create table if needed
        if (check_and_make_table() != 0) {
            System.out.println("Table init failed\n");
            return -1;
        }
        return 0;
    }


    public static void deleteEventRange(LocalDate sDay, LocalTime time_range_start, LocalTime time_range_end) {
        Event[] events = getAllEvent();
        int count = 0;
        for (int i = 0; i < Array.getLength(events); i++) {
            //if correct day and start time is within range to be deleted, delete from DB
            if (events[i].getsDay().compareTo(sDay) == 0)
                //00:00-06:00 must be inclusive on both ends, special case
                if (time_range_start.compareTo(LocalTime.parse("00:00")) == 0) {
                    if (events[i].getsTime().compareTo(time_range_start) >= 0 && events[i].getsTime().compareTo(time_range_end) <= 0) {
                        System.out.println("DBG deleting event: " + events[i].getName());
                        count++;
                        deleteEvent(events[i].getsTime().toString(), events[i].geteTime().toString(), events[i].getsDay().toString(), events[i].geteDay().toString());
                    }
                } else {
                    if (events[i].getsTime().compareTo(time_range_start) > 0 && events[i].getsTime().compareTo(time_range_end) <= 0) {
                        System.out.println("DBG deleting event: " + events[i].getName());
                        count++;
                        deleteEvent(events[i].getsTime().toString(), events[i].geteTime().toString(), events[i].getsDay().toString(), events[i].geteDay().toString());
                    }
                }
        }
        System.out.println("DBG Deleted event count: " + count);
    }


    public static void deleteEvent(String sTime, String eTime, String sDay, String eDay) {
        String sql = "DELETE FROM events WHERE sTime = ? AND eTime = ? AND sDay = ? AND eDay = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sTime);
            stmt.setString(2, eTime);
            stmt.setString(3, sDay);
            stmt.setString(4, eDay);
            stmt.executeUpdate();
            System.out.println("Event deleted");
        } catch (SQLException e) {
            System.out.println("Delete event error:" + e.getMessage());
        }
    }

    public static Event[] getAllEvent() {
        eventBuilder eb = new eventBuilder();

        String sql = "SELECT id, name, desc, loc, workType, sDay, eDay, sTime, eTime FROM events";
        String sqlcount = "SELECT COUNT(*) FROM events";
        int count = 0;

        //Get number of events
        Event[] events;
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet countRS = stmt.executeQuery(sqlcount)) {
            while (countRS.next())
                count = countRS.getInt(1);
        } catch (SQLException e) {
            System.out.println("Row Count error: " + e.getMessage());
        }
        System.out.println("DBG currentEventCount=" + count);
        events = new Event[count];

        //get events actual
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            count = 0;
            // loop through the result set
            while (rs.next()) {
                eb.setName(rs.getString("name"));
                eb.setDesc(rs.getString("desc"));
                eb.setLoc(rs.getString("loc"));
                eb.setsTime(LocalTime.parse(rs.getString("sTime")));
                eb.seteTime(LocalTime.parse(rs.getString("eTime")));
                eb.setsDay(LocalDate.parse(rs.getString("sDay")));
                eb.seteDay(LocalDate.parse(rs.getString("eDay")));
                if (rs.getInt("workType") == 1)
                    eb.setType(true);
                else
                    eb.setType(false);
                events[count] = eb.createEvent();
                count++;
            }
        } catch (SQLException e) {
            System.out.println("error getting all events: " + e.getMessage());
        }
        return events;
    }

    public static int savePrefs(userPrefs prefs) {
        String sTime = prefs.getwStart_time().toString();
        String eTime = prefs.getwEnd_time().toString();
        boolean[] workdays = prefs.getWorkdays();

        String sql = "INSERT INTO prefs(wsTime, weTime, sunday, monday, tuesday, wednesday, thursday, friday, saturday) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sTime);
            pstmt.setString(2, eTime);
            for (int i = 0; i < 7; i++)
                System.out.println(i + ": " + workdays[i]);
            pstmt.setBoolean(3, workdays[0]);
            pstmt.setBoolean(4, workdays[1]);
            pstmt.setBoolean(5, workdays[2]);
            pstmt.setBoolean(6, workdays[3]);
            pstmt.setBoolean(7, workdays[4]);
            pstmt.setBoolean(8, workdays[5]);
            pstmt.setBoolean(9, workdays[6]);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("add prefs error " + e.getMessage());
            return -1;
        }//end catch
        return 0;
    }//end method

    public static userPrefs getPrefs() {
        userPrefs prefs = null;
        boolean[] wdays = new boolean[7];
        prefsBuilder pb = new prefsBuilder();
        String sql = "SELECT id, wsTime, weTime, sunday, monday, tuesday, wednesday, thursday, friday, saturday FROM prefs WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, 1);
            ResultSet rs = stmt.executeQuery();

            //the last row will be the most recent set of prefs
            while (rs.next()) {
                pb.setStart(LocalTime.parse(rs.getString("wsTime")));
                pb.setEnd(LocalTime.parse(rs.getString("weTime")));
                wdays[0] = rs.getBoolean("sunday");
                wdays[1] = rs.getBoolean("monday");
                wdays[2] = rs.getBoolean("tuesday");
                wdays[3] = rs.getBoolean("wednesday");
                wdays[4] = rs.getBoolean("thursday");
                wdays[5] = rs.getBoolean("friday");
                wdays[6] = rs.getBoolean("saturday");
                pb.setDays(wdays);
            }
            return pb.createPrefs();
        } catch (SQLException e) {
            System.out.println("get prefs error:" + e.getMessage());
        }
        return prefs;

    }

//    private static boolean getBoolState(String str){
//        if(str.compareTo("true")==0)
//            return true;
//        else
//            return false;
//    }

    public static int saveEvent(Event ev) {
        if(hasConflict(ev))
            return 0;

        int eType;
        if (ev.isWorkType())
            eType = 1;
        else
            eType = 0;

        String sql = "INSERT INTO events(name,desc,loc,sTime,eTime,sDay,eDay,workType) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ev.getName());
            pstmt.setString(2, ev.getDesc());
            pstmt.setString(3, ev.getLoc());
            pstmt.setString(4, ev.getsTime().toString());
            pstmt.setString(5, ev.geteTime().toString());
            pstmt.setString(6, ev.getsDay().toString());
            pstmt.setString(7, ev.geteDay().toString());
            pstmt.setInt(8, eType);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Save event error: " + e.getMessage());
            return -1;
        }
        return 0;
    }

    private static Connection connect() {
        String home = System.getProperty("user.home");
        String url = "jdbc:sqlite:" + home + "/cal_app/calDB.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Connection Fail: " + e.getMessage());
        }
        return conn;
    }

    private static int check_and_make_table() {
        String sql = "CREATE TABLE IF NOT EXISTS events (\n"
                + "id integer PRIMARY KEY,\n"
                + "name text NOT NULL,\n"
                + "desc text, \n"
                + "loc text, \n"
                + "workType integer, \n"
                + "sDay text NOT NULL,\n"
                + "eDay text NOT NULL,\n"
                + "sTime text NOT NULL,\n"
                + "eTime text NOT NULL\n"
                + ");";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("events table create error : " + e.getMessage());
            return -1;
        }

        sql = "CREATE TABLE IF NOT EXISTS prefs (\n"
                + "id integer PRIMARY KEY,\n"
                + "wsTime text NOT NULL,\n"
                + "weTime text NOT NULL,\n"
                + "sunday boolean NOT NULL,\n"
                + "monday boolean NOT NULL,\n"
                + "tuesday boolean NOT NULL,\n"
                + "wednesday boolean NOT NULL,\n"
                + "thursday boolean NOT NULL,\n"
                + "friday boolean NOT NULL,\n"
                + "saturday boolean NOT NULL\n"
                + ");";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("prefs table create error " + e.getMessage());
            return -1;
        }
        return 0;
    }

    private static int check_and_make_DB() {
        String home = System.getProperty("user.home");
        File target_db = new File(home + "/cal_app/calDB.db");

        if (!target_db.exists()) {
            try (Connection conn = connect()) {
                if (conn != null) {
                    DatabaseMetaData meta = conn.getMetaData();
                } else
                    return -1;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return -1;
            }
        }
        return 0;
    }

    private static void check_and_make_dir() {
        String home = System.getProperty("user.home");
        File target_dir = new File(home + "/cal_app");

        //create DB directory if needed
        boolean flag;
        if (!target_dir.exists()) {
            flag = false;
            try {
                target_dir.mkdir();
                flag = true;
            } catch (SecurityException se) {
                System.out.println("Insufficient privs to create database directory");
            }
        }


        System.out.println("Home dir: " + System.getProperty("user.home"));
    }

    private static boolean hasConflict(Event e) {
        Event[] events = DataServer.getAllEvent();
        boolean flag=false;
        for (int i = 0; i < Array.getLength(events); i++) {
            //if theyre on the same day, investigate further
            if (e.getsDay().compareTo(events[i].getsDay()) == 0 && flag==false) {
                if ( (e.getsTime().compareTo(events[i].getsTime()) >= 0 )&& (e.getsTime().compareTo(events[i].geteTime()) <= 0)) {
                    System.out.println(e.getName() + " sTime conflicts with " + events[i].getName());
                    flag= true;
                }
                if ((e.geteTime().compareTo(events[i].getsTime()) >= 0) && (e.geteTime().compareTo(events[i].geteTime()) <= 0)){
                    System.out.println(e.getName() + " eTime conflicts with " + events[i].getName());
                    flag= true;
                }
            }//end same day if
        }//end for
        return flag;
    }//end has conflict
}//end class
