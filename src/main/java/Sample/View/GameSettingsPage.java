package Sample.View;

import Sample.Model.Pacman;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.net.URL;

public class GameSettingsPage extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL fxmlAddress = getClass().getResource("/Sample/fxml/GameSettingsPage.fxml");
        Parent pane = FXMLLoader.load(fxmlAddress);
        Button soundButton = ((Button) pane.getChildrenUnmodifiable().get(5));
        Button hardModeButton = ((Button) pane.getChildrenUnmodifiable().get(10));
        if (WelcomePage.isSoundMuted) {
            soundButton.setText("UnMute");
        } else {
            soundButton.setText("Mute");
        }
        if (GamePage.isHardModeOn) {
            hardModeButton.setText("On");
        } else {
            hardModeButton.setText("Off");
        }
        ((Slider) pane.getChildrenUnmodifiable().get(1)).adjustValue(Pacman.health);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }
}
