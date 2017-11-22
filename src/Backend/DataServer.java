package Backend;

import java.io.File;
import java.sql.*;

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
        String home = System.getProperty("user.home");
        String url = "jdbc:sqlite:" + home + "/cal_app/calDB.db";

        String sql = "CREATE TABLE IF NOT EXISTS events (\n"
                + "id integer PRIMARY KEY,\n"
                + "name text NOT NULL,\n"
                + "desc text \n"
                + "capacity real\n"
                + "loc text \n"
                + "workType integer\n"
                + "sDay text NOT NULL,\n"
                + "eDay text NOT NULL,\n"
                + " sTime text NOT NULL,\n"
                + " eTime text NOT NULL\n"
                + ");";

        try(Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
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
            String url = "jdbc:sqlite:"+home+"/cal_app/calDB.db";

            try(Connection conn = DriverManager.getConnection(url)){

                if(conn !=null) {
                    DatabaseMetaData meta = conn.getMetaData();
                    System.out.println("The driver name is " + meta.getDriverName());
                    System.out.println("A new database was created");
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
            if(flag)
                System.out.println("DB directory created");
        }


        System.out.println("Home dir: " + System.getProperty("user.home"));
    }
//    public Event getEvent(){
//
//
//
//    }

//    public Event[] getAllEvent(){
//
//    }

    public int saveEvent(Event ev){

        return 0;
    }

    private static boolean dbExists(){

        return false;
    }
}
