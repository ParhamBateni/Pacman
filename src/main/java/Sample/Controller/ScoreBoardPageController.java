package Sample.Controller;

import Sample.Model.User;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class ScoreBoardPageController extends PageController {


    public ArrayList<Text> getUsersInfoSorted() {
        ArrayList<Text> usersInfo = new ArrayList<>();
        int count = 1;
        int previousCount = 1;
        User previousUser = User.get10MaxScoreUsers().get(0);
        for (User user : User.get10MaxScoreUsers()) {
            Text text;
            if (count != 1 && user.getMaxScore() != previousUser.getMaxScore()) {
                String info = count + ". " + user.getUsername() + "    " + user.getMaxScore();
                text = new Text(info);
                text.setFont(Font.font(null, FontWeight.BOLD, 15));
                if (user == User.loggedInUser) text.setFill(Color.CYAN);
                else {
                    text.setFill(Color.CHARTREUSE);
                }
                previousCount = count;
            } else {
                String info = previousCount + ". " + user.getUsername() + "    " + user.getMaxScore();
                text = new Text(info);
                if (user == User.loggedInUser) text.setFill(Color.CYAN);
                else {
                    text.setFill(Color.CHARTREUSE);
                }
                text.setFont(Font.font(null, FontWeight.BOLD, 15));
            }
            count++;
            previousUser = user;
            usersInfo.add(text);
        }
        return usersInfo;
    }

    public void backClicked(MouseEvent mouseEvent) throws Exception {
        back(mouseEvent, false, true);
    }
}
