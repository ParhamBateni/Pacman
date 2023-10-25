package Sample.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;

public class Page {
    public void changePage(MouseEvent mouseEvent, String fxmlAddressString) throws Exception {
        Node node = (Node) mouseEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        URL fxmlAddress = getClass().getResource(fxmlAddressString);
        Parent pane = FXMLLoader.load(fxmlAddress);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }
}
