package result;

import gameresult.OnePlayerGameResult;
import gameresult.manager.json.JsonOnePlayerGameResultManager;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.tinylog.Logger;
import util.DurationUtil;


import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class ResultController {

    private static final int MAX_NUMBER_OF_ROWS = 10;
    @FXML
    private TableView<OnePlayerGameResult> leaderTable;
    @FXML
    private TableColumn<OnePlayerGameResult, String> playerName;
    @FXML
    private TableColumn<OnePlayerGameResult, Integer> numberOfMoves;
    @FXML
    private TableColumn<OnePlayerGameResult, String> duration;
    @FXML
    private TableColumn<OnePlayerGameResult, String> created;


    @FXML
    private void initialize() throws IOException {
        playerName.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        numberOfMoves.setCellValueFactory(new PropertyValueFactory<>("numberOfMoves"));
        duration.setCellValueFactory(
                data -> {
                    var duration = data.getValue().getDuration();
                    return new ReadOnlyStringWrapper(DurationUtil.formatDuration(duration));
        });
        created.setCellValueFactory(
                data -> {
                    var date = data.getValue().getCreated();
                    var formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
                    return new ReadOnlyStringWrapper(formatter.format(date));
        });
        ObservableList<OnePlayerGameResult> observableList = FXCollections.observableArrayList();
        observableList.addAll(new JsonOnePlayerGameResultManager(Path.of("gameresult.json"))
                .getBestByNumberOfMoves(MAX_NUMBER_OF_ROWS));
        leaderTable.setItems(observableList);
    }

    @FXML
    private void backToMenu(ActionEvent event) throws IOException {
        Logger.info("Back to menu button pressed.");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startscreen.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Start screen");
        stage.show();
    }
}
