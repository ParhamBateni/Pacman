package Sample.View;

import Sample.Controller.DataBaseController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class WelcomePage extends Application {
    private static final Media media = new Media(new File("src\\main\\resources\\Sample\\Audio\\faded.mp3")
            .toURI().toString());
    public static MediaPlayer mediaPlayer;
    public static boolean isSoundMuted = false;
    public static WelcomePage welcomePage;
    public static Stage stage;

    public static void setSoundsMuted(boolean mute) {
        if (mute) {
            isSoundMuted = true;
            mediaPlayer.stop();
        } else {
            isSoundMuted = false;
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setAutoPlay(true);
        }
    }

    public static void main(String[] args) {
        try {
            DataBaseController.usersDataBaseInitialization();
        } catch (Exception e) {
            System.err.println("can not initialize data base!");
        }
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        WelcomePage.stage = stage;
        welcomePage = this;
        URL fxmlAddress = getClass().getResource("/Sample/fxml/WelcomePage.fxml");
        Parent pane = FXMLLoader.load(fxmlAddress);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        if (!isSoundMuted && mediaPlayer == null) {
            setSoundsMuted(false);
        }
        stage.getIcons().add(new Image("/Sample/JPG/Pacman.jpg"));
        stage.setTitle("Pacman");
        stage.setX(490.0);
        stage.setY(190.0);
        stage.show();
    }


}
