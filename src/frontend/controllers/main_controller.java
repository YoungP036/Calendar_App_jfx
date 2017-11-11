package frontend.controllers;

import frontend.Main;

public class main_controller extends universal_controller{
    public void open_pref(){
        Main.set_pane(1);
    }
    public void open_edit(){
        //TODO POPULATE WITH EXISTING DATA
        Main.set_pane(2);
    }
    public void open_create(){
        //TODO CREATE
        Main.set_pane(2);
    }
    public void open_search(){
        Main.set_pane(3);
    }
    public void delete_event(){

        //TODO DELETE EVENT
        System.out.println("placeholder delete");
    }

    public void save_events(){
        //TODO get all events, output to text file on /home/desktop
        System.out.println("TODO get all events and save to text");
    }

}
