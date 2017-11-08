package frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    static Pane root;
    static List<AnchorPane> grid = new ArrayList<>();
    private static int idx_cur=0;
    @Override
    public void start(Stage primaryStage) throws Exception{
        try{
            root = FXMLLoader.load(getClass().getResource("anchor.fxml"));
            grid.add(FXMLLoader.load(getClass().getResource("main_screen.fxml")));
            grid.add(FXMLLoader.load(getClass().getResource("pref_screen.fxml")));
            grid.add(FXMLLoader.load(getClass().getResource("edit_screen.fxml")));
//            grid.add((GridPane)FXMLLoader.load(getClass().getResource("pref_screen.fxml")));
            //add more to grid here

//            root.getChildren().add(grid.get(0));
            set_pane(0);
            Scene scene = new Scene(root,610,411);
//            scene.getStyleSheets().add(getClass().getResource("app.css").toExternalFOrm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e){
            e.printStackTrace();
        }


//        Parent root = FXMLLoader.load(getClass().getResource("main_screen.fxml"));
////        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 610, 411));
//        primaryStage.setResizable(false);
//        primaryStage.show();
    }

    public static AnchorPane get_pane(int idx){
        return grid.get(idx);
    }
    public static void set_pane(int idx){
        root.getChildren().remove(grid.get(idx_cur));
        root.getChildren().add(grid.get(idx));
        idx_cur=idx;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
