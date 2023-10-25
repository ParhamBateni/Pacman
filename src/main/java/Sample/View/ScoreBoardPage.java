package Sample.View;

import Sample.Controller.ScoreBoardPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;

public class ScoreBoardPage extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL fxmlAddress = getClass().getResource("/Sample/fxml/ScoreBoardPage.fxml");
        AnchorPane pane = FXMLLoader.load(fxmlAddress);
        ArrayList<Text> usersInfo = new ScoreBoardPageController().getUsersInfoSorted();
        VBox scoreBoard = new VBox();
        for (Text text : usersInfo) {
            scoreBoard.getChildren().add(text);
        }
        scoreBoard.setLayoutX(250.0);
        scoreBoard.setLayoutY(100.0);
        pane.getChildren().add(scoreBoard);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

}
