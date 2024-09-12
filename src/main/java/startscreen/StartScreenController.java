package startscreen;

import game.LabyrinthController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
    private void switchToGame(ActionEvent event) throws IOException {
        if(username.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(null);
            alert.setContentText("Please enter a username!");
            alert.showAndWait();
        } else {
            Logger.info("Username set to {}", username.getText());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game.fxml"));
            Parent root = loader.load();
            LabyrinthController labcontroller = loader.getController();
            labcontroller.setUsername(username.getText());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("Game");
            stage.show();
        }
    }

    @FXML
    private void switchToResults(ActionEvent event) throws IOException {
        Logger.info("Switching to results");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameresult.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Results");
        stage.show();
    }
}
