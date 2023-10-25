package Sample.Controller;

import Sample.Model.User;
import Sample.View.PopUpWindow;
import Sample.View.WelcomePage;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AccountPageController extends PageController implements playButtonSound {
    public TextField oldPassword;
    public TextField newPassword;
    private Stage popUp = new Stage();
    private final EventHandler yesButtonHandler = new EventHandler() {
        @Override
        public void handle(Event event) {
            play();
            popUp.close();
            User.loggedInUser.removeUser();
            try {
                WelcomePage.welcomePage.start(stage);
            } catch (Exception e) {
            }
        }
    };
    private final EventHandler noButtonHandler = new EventHandler() {
        @Override
        public void handle(Event event) {
            play();
            popUp.close();
        }
    };

    public void changePasswordClicked(MouseEvent mouseEvent) throws Exception {
        play();
        if (oldPassword.getText().equals("") || newPassword.getText().equals("")) {
            new PopUpWindow("fill the fields", true).start(stage);
        } else {
            String oldPasswordString = oldPassword.getText();
            String newPasswordSting = newPassword.getText();
            oldPassword.setText("");
            newPassword.setText("");
            if (!oldPasswordString.equals(User.loggedInUser.getPassword())) {
                new PopUpWindow("wrong password", true).start(stage);
            } else {
                User.loggedInUser.setPassword(newPasswordSting);
                new PopUpWindow("password changed successfully!", false).start(stage);
            }
        }
    }

    public void deleteAccountClicked(MouseEvent mouseEvent) {
        play();
        popUp = new Stage();
        VBox messageBox = new VBox(20);
        popUp.initModality(Modality.APPLICATION_MODAL);
        BackgroundFill background_fill = new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(background_fill);
        Button yesButton = new Button("yes");
        Button noButton = new Button("no");
        yesButton.setOnAction(yesButtonHandler);
        noButton.setOnAction(noButtonHandler);
        Label text = new Label("are you sure you want to delete your account?");
        text.setTextFill(Color.RED);
        HBox buttonPlace = new HBox(20);
        buttonPlace.setAlignment(Pos.CENTER);
        messageBox.getChildren().add(text);
        messageBox.getChildren().add(buttonPlace);
        buttonPlace.getChildren().add(noButton);
        buttonPlace.getChildren().add(yesButton);
        text.setFont(Font.font(20));
        messageBox.setAlignment(Pos.BASELINE_CENTER);
        messageBox.setBackground(background);
        Scene scene = new Scene(messageBox, 500, 100);
        scene.getStylesheets().add("/Sample/CSS/MenuCss.css");
        popUp.setScene(scene);
        popUp.show();
    }

    public void backClicked(MouseEvent mouseEvent) throws Exception {
        back(mouseEvent, false, true);
    }
}
