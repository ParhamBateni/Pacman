package Sample.Controller;

import Sample.Model.User;
import javafx.scene.input.MouseEvent;

public class MazeSelectionPageController extends PageController implements playButtonSound {

    public void backClicked(MouseEvent mouseEvent) throws Exception {
        play();
        if (User.loggedInUser == null)
            back(mouseEvent, true, false);
        else {
            back(mouseEvent, false, true);
        }
    }

}
