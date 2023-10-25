package Sample.Controller;

import Sample.Model.User;
import Sample.View.PopUpWindow;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class SignupPageController extends PageController {
    public TextField usernameField;
    public PasswordField passwordField;

    public void signupClicked(MouseEvent mouseEvent) throws Exception {
        play();
        if (usernameField.getText().equals("") || passwordField.getText().equals("")) {
            new PopUpWindow("fill the fields", true).start(stage);
        } else {
            String username = usernameField.getText();
            String password = passwordField.getText();
            usernameField.setText("");
            passwordField.setText("");
            if (User.getUserByUsername(username) != null) {
                new PopUpWindow("user with this name already exists", true).start(stage);
            } else {
                new User(username, password);
                new PopUpWindow("user signed up successfully!", false).start(stage);
                back(mouseEvent, true, false);
            }
        }
    }

    public void backClicked(MouseEvent mouseEvent) throws Exception {
        back(mouseEvent, true, false);
    }
}
