package id.mky.bewerbungsmanager;

import id.mky.bewerbungsmanager.DB.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class BewerbungsManager extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        DatabaseInitializer.init();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/id/mky/bewerbungsmanager/view/main-view.fxml"));
        Scene scene = new Scene(loader.load());

        URL cssUrl = getClass().getResource("/id/mky/bewerbungsmanager/style/main-Style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("CSS-Datei nicht gefunden!");
        }

        stage.setTitle("Bewerbungsmanager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
