package Sample.Controller;

import Sample.Model.User;
import Sample.View.PopUpWindow;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginPageController extends PageController implements playButtonSound {
    public TextField usernameField;
    public PasswordField passwordField;

    public void loginClicked(MouseEvent mouseEvent) throws Exception {
        play();
        if (usernameField.getText().equals("") || passwordField.getText().equals("")) {
            new PopUpWindow("fill the fields", true).start(stage);
        } else {
            User user = User.getUserByUsername(usernameField.getText());
            String password = passwordField.getText();
            usernameField.setText("");
            passwordField.setText("");
            if (user == null) {
                new PopUpWindow("user with this name does not exist", true).start(stage);
            } else if (!user.getPassword().equals(password)) {
                new PopUpWindow("wrong password", true).start(stage);
            } else {
                User.setLoggedInUser(user);
                new PopUpWindow("user logged in successfully!", false).start(stage);
                back(mouseEvent, false, true);
            }
        }
    }

    public void backClicked(MouseEvent mouseEvent) throws Exception {
        back(mouseEvent, true, false);
    }
}
