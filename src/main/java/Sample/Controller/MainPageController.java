package Sample.Controller;

import Sample.Model.User;
import Sample.View.GamePage;
import Sample.View.GameSettingsPage;
import Sample.View.PopUpWindow;
import Sample.View.ScoreBoardPage;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;

public class MainPageController extends PageController implements playButtonSound {
    public void newGameClicked(MouseEvent mouseEvent) throws Exception {
        play();
        new GamePage().playNewGame(stage);
    }

    public void accountClicked(MouseEvent mouseEvent) throws Exception {
        play();
        Node node = (Node) mouseEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        URL fxmlAddress = getClass().getResource("/Sample/fxml/AccountPage.fxml");
        Parent pane = FXMLLoader.load(fxmlAddress);
        Scene scene = new Scene(pane);
        VBox vBox = (VBox) pane.getChildrenUnmodifiable().get(0);
        Label currentUser = new Label(User.loggedInUser.getUsername());
        currentUser.setTextFill(Color.CYAN);
        currentUser.setPadding(new Insets(0, 0, 0, 30));
        vBox.getChildren().add(currentUser);
        stage.setScene(scene);
        stage.show();
    }

    public void scoreBoardClicked(MouseEvent mouseEvent) throws Exception {
        play();
        new ScoreBoardPage().start(stage);
    }

    public void gameSettingsClicked(MouseEvent mouseEvent) throws Exception {
        play();
        new GameSettingsPage().start(stage);
    }

    public void logoutClicked(MouseEvent mouseEvent) throws Exception {
        play();
        User.loggedInUser = null;
        new PopUpWindow("user logged out successfully!", false).start(stage);
        new PageController().back(mouseEvent, true, false);
    }
}
