package Sample.View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainPage extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        URL fxmlAddress = getClass().getResource("/Sample/fxml/MainPage.fxml");
        Parent pane = FXMLLoader.load(fxmlAddress);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setX(490.0);
        stage.setY(190.0);
        stage.show();
    }
}
