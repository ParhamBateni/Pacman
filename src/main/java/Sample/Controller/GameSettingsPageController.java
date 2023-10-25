package Sample.Controller;

import Sample.Model.Pacman;
import Sample.Model.User;
import Sample.View.GamePage;
import Sample.View.MazeSelectionPage;
import Sample.View.WelcomePage;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class GameSettingsPageController extends PageController implements playButtonSound {
    public Slider lifeBar;
    public Button soundButton;
    public Button hardModeButton;

    public void setPacmanLife() {
        play();
        Pacman.setMaxLife((int) lifeBar.getValue());
    }

    public void backClicked(MouseEvent mouseEvent) throws Exception {
        play();
        if (User.loggedInUser != null) {
            back(mouseEvent, false, true);
        } else {
            back(mouseEvent, true, false);
        }
    }

    public void MuteAllSounds(MouseEvent mouseEvent) {
        if (soundButton.getText().equals("Mute")) {
            soundButton.setText("UnMute");
            WelcomePage.mediaPlayer.pause();
            WelcomePage.isSoundMuted = true;
        } else {
            soundButton.setText("Mute");
            WelcomePage.mediaPlayer.play();
            WelcomePage.isSoundMuted = false;
        }

    }

    public void enterMazeSelectionPage(MouseEvent mouseEvent) throws Exception {
        play();
        new MazeSelectionPage().start(stage);
    }

    public void hardModeClicked(MouseEvent mouseEvent) {
        if (hardModeButton.getText().equals("On")) {
            GamePage.isHardModeOn = false;
            hardModeButton.setText("Off");
        } else {
            GamePage.isHardModeOn = true;
            hardModeButton.setText("On");
        }
    }
}
