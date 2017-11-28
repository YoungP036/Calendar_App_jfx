package frontend.controllers;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class universal_controller {

    @FXML
    private Button cancel_BTN;
    public void complete_shutdown(){
        Platform.exit();
    }

    @FXML
    private void return_to_main(){
        Stage stage = (Stage)cancel_BTN.getScene().getWindow();
        stage.close();
    }

    public static boolean validate_time(String time){
        boolean flag=false;
        Pattern time_pattern = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]");
        Matcher m = time_pattern.matcher(time);
        return m.matches();
    }
    public static boolean validate_minutes(String minutes) {
        return Integer.parseInt(minutes)>0;
    }
    public void invalid_input_dialogue(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid Input");
        alert.setHeaderText("Input error");
        alert.setContentText("-Event times should be HH:MM on a 24 hour clock\n- Search lengths should be in minutes and greater then 0");
        alert.showAndWait();
    }

    public static void new_window(AnchorPane screen){
        Stage newStage = new Stage();

        if(screen.getScene()==null){
            Scene scene = new Scene(screen);
            newStage.setScene(scene);
        }
        else
            newStage.setScene(screen.getScene());

        newStage.show();
    }

}
