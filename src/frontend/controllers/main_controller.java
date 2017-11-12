package frontend.controllers;

import frontend.Main;

public class main_controller extends universal_controller{
    public void open_pref(){
        new_window(Main.screenList.get(1));
    }
    public void open_edit(){
        //TODO POPULATE WITH EXISTING DATA
        new_window(Main.screenList.get(2));
    }
    public void open_create(){
        //TODO CREATE
//        Main.set_pane(2);
        new_window(Main.screenList.get(2));
    }
    public void open_search(){
//        Main.set_pane(3);
        new_window(Main.screenList.get(3));
    }
    public void delete_event(){

        //TODO DELETE EVENT
        System.out.println("TODO DELETE EVENT");
    }

    public void save_events(){
        //TODO get all events, output to text file on /home/desktop
        System.out.println("TODO EXPORT ALL TO .TXT");
    }

}
