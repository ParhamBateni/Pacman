package Sample.Controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public interface playButtonSound {
    default void play() {
        javafx.scene.media.Media media = new Media(new File("src\\main\\resources\\Sample\\Audio\\button-3.wav")
                .toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }
}
