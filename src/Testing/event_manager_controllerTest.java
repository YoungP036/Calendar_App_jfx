package Testing;

import org.junit.Test;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;

public class event_manager_controllerTest {



    public void test_alarm_system(){
        LocalDate today= LocalDate.now();
        LocalTime time= LocalTime.now();
        create_alarm(today,time.plusMinutes(1));
        System.out.println("main exiting\n");
    }

    public void create_alarm(LocalDate today, LocalTime time) {
        System.out.println("DBG in create alarm");
        Thread t = new Thread() {
            public void run() {
                File alarm_wav = new File("alarm.wav");
                System.out.println("DBG alarm thread running\n");
                while (true) {
                    System.out.println("DBG spinning");
                    if (LocalDate.now().compareTo(today) == 0 && LocalTime.now().compareTo(time) == 0) {
                        System.out.println("Breaking");
                        PlaySound(alarm_wav);
                        break;
                    }
                }
            }
        };
        t.start();
    }
    private static void PlaySound(File Sound){
        InputStream in;
        try{
            in = new FileInputStream(Sound);
            AudioStream audio = new AudioStream(in);
            AudioPlayer.player.start(audio);
            Thread.sleep(7500);
        }catch(Exception e){
            System.out.println("Alarm sound error: " + e.getMessage());
        }
    }
}