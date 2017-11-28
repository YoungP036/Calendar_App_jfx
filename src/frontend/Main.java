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

    static private Pane root;
    static public List<AnchorPane> screenList = new ArrayList<>();
    private static int idx_cur=0;
    @Override
    public void start(Stage primaryStage) throws Exception{
        try{
            root = FXMLLoader.load(getClass().getResource("screens/anchor.fxml"));
            screenList.add(FXMLLoader.load(getClass().getResource("screens/main_screen.fxml")));
            screenList.add(FXMLLoader.load(getClass().getResource("screens/pref_screen.fxml")));
            screenList.add(FXMLLoader.load(getClass().getResource("screens/add_screen.fxml")));
            screenList.add(FXMLLoader.load(getClass().getResource("screens/search_screen.fxml")));

            set_pane(0);
            Scene scene = new Scene(root,610,411);
//            scene.getStyleSheets().add(getClass().getResource("app.css").toExternalFOrm());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void set_pane(int idx){
        root.getChildren().remove(screenList.get(idx_cur));
        root.getChildren().add(screenList.get(idx));
        idx_cur=idx;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
