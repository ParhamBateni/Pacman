package Sample.Controller;

import Sample.View.GamePage;
import Sample.View.GameSettingsPage;
import Sample.View.Page;
import javafx.scene.input.MouseEvent;

public class WelcomePageController extends PageController implements playButtonSound {
    public void enterLoginPage(MouseEvent mouseEvent) throws Exception {
        play();
        new Page().changePage(mouseEvent, "/Sample/fxml/LoginPage.fxml");
    }

    public void enterSignupPage(MouseEvent mouseEvent) throws Exception {
        play();
        new Page().changePage(mouseEvent, "/Sample/fxml/SignupPage.fxml");
    }

    public void exitClicked(MouseEvent mouseEvent) {
        play();
        try {
            DataBaseController.saveUsersData();
        } catch (Exception e) {
            System.err.println("can not save data!");
        }
        System.exit(0);
    }

    public void newGameClicked(MouseEvent mouseEvent) throws Exception {
        play();
        new GamePage().playNewGame(stage);
    }

    public void gameSettingsClicked(MouseEvent mouseEvent) throws Exception {
        play();
        new GameSettingsPage().start(stage);
    }
}
