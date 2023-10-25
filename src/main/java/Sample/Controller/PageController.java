package Sample.Controller;

import Sample.View.Page;
import Sample.View.WelcomePage;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class PageController implements playButtonSound {
    protected Stage stage = WelcomePage.stage;

    public void back(MouseEvent mouseEvent, boolean isBackToWelcomeMenu, boolean isBackToMainMenu) throws Exception {
        play();
        try {
            DataBaseController.saveUsersData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isBackToWelcomeMenu)
            new Page().changePage(mouseEvent, "/Sample/fxml/WelcomePage.fxml");
        else if (isBackToMainMenu)
            new Page().changePage(mouseEvent, "/Sample/fxml/MainPage.fxml");
    }
}
