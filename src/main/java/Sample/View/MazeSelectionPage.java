package Sample.View;

import Sample.Controller.playButtonSound;
import Sample.Model.Map;
import Sample.Model.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;

public class MazeSelectionPage extends Application implements playButtonSound {
    private AnchorPane pane;
    private MenuButton mazes;
    private ArrayList<Map> userMaps;
    private Map currentMap;
    private final EventHandler newMazeHandler = new EventHandler() {
        @Override
        public void handle(Event event) {
            play();
            Map map = new Map();
            map.createMaze();
            Pane mazePane = map.Init(false);
            currentMap = map;
            mazePane.setLayoutX(180.0);
            mazePane.setLayoutY(72.0);
            pane.getChildren().add(mazePane);

        }
    };
    private User currentUser;
    private Stage stage;
    private final EventHandler selectMazeHandler = new EventHandler() {
        @Override
        public void handle(Event event) {
            play();
            if (currentMap != null) {
                currentUser.setSelectedMap(currentMap);
                try {
                    new PopUpWindow("maze selected successfully!", false).start(stage);
                } catch (Exception e) {
                }
            } else {
                try {
                    new PopUpWindow("no mazes to select", true).start(stage);
                } catch (Exception e) {
                }
            }
        }
    };
    private final EventHandler saveMazeHandler = new EventHandler() {
        @Override
        public void handle(Event event) {
            if (currentMap.isSaved) {
                try {
                    new PopUpWindow("maze is already saved", false).start(stage);
                    return;
                } catch (Exception e) {
                }
            }
            if (currentMap != null) {
                currentUser.addMapToSavedMaps(currentMap);
                mazes.getItems().add(new MenuItem("maze " + (mazes.getItems().size() + 1)));
                currentMap.isSaved = true;
                updateItems();
                try {
                    new PopUpWindow("maze saved successfully!", false).start(stage);
                } catch (Exception e) {
                }
            } else {
                try {
                    new PopUpWindow("no mazes to save", true).start(stage);
                } catch (Exception e) {
                }
            }
        }
    };

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        URL fxmlAddress = getClass().getResource("/Sample/fxml/MazeSelectionPage.fxml");
        pane = FXMLLoader.load(fxmlAddress);
        mazes = (MenuButton) pane.getChildren().get(1);
        Button newMazeButton = (Button) pane.getChildren().get(5);
        Button selectMazeButton = (Button) pane.getChildren().get(4);
        Button saveMazeButton = (Button) pane.getChildren().get(3);
        if (User.loggedInUser != null) {
            currentUser = User.loggedInUser;
        } else {
            currentUser = User.notLoggedInUser;
        }
        userMaps = currentUser.getSavedMaps();
        for (int i = 1; i <= userMaps.size(); i++) {
            mazes.getItems().add(new MenuItem("maze " + i));
        }
        newMazeButton.setOnAction(newMazeHandler);
        selectMazeButton.setOnAction(selectMazeHandler);
        saveMazeButton.setOnAction(saveMazeHandler);
        updateItems();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    private void updateItems() {
        for (int i = 0; i < mazes.getItems().size(); i++) {
            handleMenuItems(userMaps.get(i), mazes.getItems().get(i));
        }
    }

    private void handleMenuItems(Map map, MenuItem menuItem) {
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                play();
                currentMap = map;
                Pane mazePane = map.Init(false);
                mazePane.setLayoutX(180.0);
                mazePane.setLayoutY(72.0);
                mazes.setText(menuItem.getText());
                pane.getChildren().add(mazePane);
            }
        });
    }
}
