package Testing;

import javafx.fxml.FXML;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class event_manager_controllerTest {


    public void test_alarm_system(){
        LocalDate today= LocalDate.now();
        LocalTime time= LocalTime.now();
        int min = time.getMinute();
        min++;
        time=LocalTime.of(time.getHour(),min);
        create_alarm(today,time);

    }

    @FXML
    private void create_alarm(LocalDate date, LocalTime time){
        //TODO spin thread to play alarm @date&&time
    }
}