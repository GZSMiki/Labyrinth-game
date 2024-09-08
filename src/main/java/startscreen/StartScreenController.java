package startscreen;

import game.LabyrinthController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;


public class StartScreenController {
    @FXML
    private TextField username;

    @FXML
    private void initialize() {
        this.username.setText(System.getProperty("user.name"));
    }

    @FXML
    private void switchToGame() throws IOException {
        if(username.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(null);
            alert.setContentText("Please enter a username!");
            alert.showAndWait();
        } else {
            Logger.info("Username set to {}", username.getText());
            loadScene("/game.fxml");
        }
    }

    @FXML
    private void switchToResults() throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Not implemented!");
        alert.setTitle(null);
        alert.showAndWait();
    }

    private void loadScene(String path) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent root = loader.load();
        LabyrinthController labcontroller = loader.getController();
        labcontroller.setUsername(username.getText());
        Stage stage = (Stage) username.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
