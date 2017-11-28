package Backend;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

//talks to mySQL DB
//getAllEvents returns Event arraylist
//getOneEvent- primary key needs to be a hash of the easily repeatable Event fields, sDay,eDay,sTime,eTime etc
//             if pk is autoincrement client would have to pull all events to find the one it wants
//saveEvent(Event e)
//searchOpening(Event e) - fill Event with numerics, the strings can be blank
public class DataServer {

    public static int init(){
        //check for and create DB if needed
        check_and_make_dir();
        if(check_and_make_DB()!=0){
            System.out.println("DB init failure");
            return -1;
        }

        //check for and create table if needed
        if(check_and_make_table()!=0){
            System.out.println("Table init failed\n");
            return -1;
        }
        return 0;
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

        try(Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch(SQLException e){
            System.out.println(e.getMessage());
            return -1;
        }
        return 0;
    }
    private static int check_and_make_DB(){
        String home = System.getProperty("user.home");
        File target_db = new File(home+"/cal_app/calDB.db");

        if(!target_db.exists()){
            try(Connection conn = connect()){
                if(conn !=null) {
                    DatabaseMetaData meta = conn.getMetaData();
//                    System.out.println("The driver name is " + meta.getDriverName());
//                    System.out.println("A new database was created");
                }
                else
                    return -1;
            } catch (SQLException e){
                System.out.println(e.getMessage());
                return -1;
            }
        }
        return 0;
    }
    private static void check_and_make_dir(){
        String home = System.getProperty("user.home");
        File target_dir = new File(home+"/cal_app");

        //create DB directory if needed
        boolean flag;
        if(!target_dir.exists()){
            flag=false;
            try{
                target_dir.mkdir();
                flag=true;
            }
            catch(SecurityException se){
                System.out.println("Insufficient privs to create database directory");
            }
//            if(flag)
//                System.out.println("DB directory created");
        }


        System.out.println("Home dir: " + System.getProperty("user.home"));
    }

/*
        I just realized this method is useless, all I need is getAllEvents into array, and manipulate array when needed
 */
//    public static Event getEvent(LocalDate date){
//        System.out.print(date.toString());
//        String sql="SELECT id, name, desc, loc, workType, sDay, eDay, sTime, eTime "
//                    + "FROM events WHERE sDay="+date.toString()+";"; // WHERE sDay=" + date.toString();
////        String sql = "SELECT id, name, desc, loc, workType, sDay, eDay, sTime, eTime FROM events";
//
//        Event event;
//        eventBuilder eb = new eventBuilder();
//
//        try(Connection conn = connect();
//            Statement stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery(sql)){
//            eb.setName(rs.getString("name"));
//            System.out.println("DBG name: " + rs.getString("name"));
//            eb.setDesc(rs.getString("desc"));
//            eb.setLoc(rs.getString("loc"));
//            if(rs.getInt("workType")==1)
//                eb.setType(true);
//            else
//                eb.setType(false);
//            eb.setsTime(LocalTime.parse(rs.getString("sTime")));
//            eb.seteTime(LocalTime.parse(rs.getString("eTime")));
//            eb.setsDay(LocalDate.parse(rs.getString("sDay")));
//            eb.seteDay(LocalDate.parse(rs.getString("eDay")));
//            event=eb.createEvent();
//            return event;
//        } catch(SQLException e){
//            System.out.println("Get single event error: " + e.getMessage());
//            return null;
//        }
//
//    }

    public static void deleteEvent(String sTime, String eTime, String sDay, String eDay){
        String sql = "DELETE FROM events WHERE sTime = ? AND eTime = ? AND sDay = ? AND eDay = ?";
        try(Connection conn = connect();
            PreparedStatement stmt=conn.prepareStatement(sql)){
                stmt.setString(1, sTime);
                stmt.setString(2, eTime);
                stmt.setString(3, sDay);
                stmt.setString(4, eDay);
                stmt.executeUpdate();
                System.out.println("Event deleted");
        }catch(SQLException e){
                System.out.println("Delete event error:"+e.getMessage());
        }
    }

    public static Event[] getAllEvent(){
        eventBuilder eb = new eventBuilder();

        String sql = "SELECT id, name, desc, loc, workType, sDay, eDay, sTime, eTime FROM events";
        String sqlcount="SELECT COUNT(*) FROM events";
        int count=0;

        //Get number of events
        Event[] events;
        try(Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet countRS = stmt.executeQuery(sqlcount)){
            while(countRS.next())
                count=countRS.getInt(1);
        }catch(SQLException e) {
            System.out.println("Row Count error: " + e.getMessage());
        }
        events = new Event[count];

        //get events actual
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            count=0;
            // loop through the result set
            while (rs.next()) {
                eb.setName(rs.getString("name"));
                eb.setDesc(rs.getString("desc"));
                eb.setLoc(rs.getString("loc"));
                eb.setsTime(LocalTime.parse(rs.getString("sTime")));
                eb.seteTime(LocalTime.parse(rs.getString("eTime")));
                eb.setsDay(LocalDate.parse(rs.getString("sDay")));
                eb.seteDay(LocalDate.parse(rs.getString("eDay")));
                if(rs.getInt("workType")==1)
                    eb.setType(true);
                else
                    eb.setType(false);
                events[count]=eb.createEvent();
                count++;
            }
        } catch (SQLException e) {
            System.out.println("error getting all events: " + e.getMessage());
        }
        return events;
    }

    public static int saveEvent(Event ev){
        String name = ev.getName();
        String desc = ev.getDesc();
        String loc = ev.getLoc();
        String sTime = ev.getsTime().toString();
        String eTime=ev.geteTime().toString();
        String sDay=ev.getsDay().toString();
        String eDay=ev.geteDay().toString();
        int eType;
        if(ev.isWorkType())
            eType=1;
        else
            eType=0;

        String sql = "INSERT INTO events(name,desc,loc,sTime,eTime,sDay,eDay,workType) VALUES(?,?,?,?,?,?,?,?)";
        try(Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,name);
            pstmt.setString(2,desc);
            pstmt.setString(3,loc);
            pstmt.setString(4,sTime);
            pstmt.setString(5,eTime);
            pstmt.setString(6,sDay);
            pstmt.setString(7,eDay);
            pstmt.setInt(8,eType);
//            System.out.println("DBG saving event\n");
            pstmt.executeUpdate();
        } catch(SQLException e){
                System.out.println("Save event error: " + e.getMessage());
                return -1;
        }
        return 0;
    }

    private static Connection connect(){
        String home = System.getProperty("user.home");
        String url = "jdbc:sqlite:" + home + "/cal_app/calDB.db";
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(url);
        } catch(SQLException e){
            System.out.println("Connection Fail: " + e.getMessage());
        }
        return conn;
    }

}
