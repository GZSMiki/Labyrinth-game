package game;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import model.Direction;
import model.LabyrinthModel;
import model.Position;
import org.tinylog.Logger;

import java.util.Optional;

public class LabyrinthController {
    @FXML
    private GridPane grid;

    @FXML
    private TextField numberOfMovesField;

    private LabyrinthModel model;

    private final IntegerProperty numberOfMoves = new SimpleIntegerProperty(0);

    @FXML
    private void initialize() {
        model = new LabyrinthModel();
        for (int i = 0; i < grid.getRowCount(); i++) {
            for (int j = 0; j < grid.getColumnCount(); j++) {
                var square = createSquare(i, j);
                grid.add(square, j, i);
            }
        }
    }


    private StackPane createSquare(int row, int col) {
        var square = new StackPane();
        StringBuilder borderStyle = new StringBuilder("-fx-border-color: black; -fx-border-width: ");

        if(row == 0 && col != 4) {
            borderStyle.append("10 ");
        } else {
            borderStyle.append("1 ");
        }
        if(col == grid.getColumnCount()-1 ||
                model.checkIfVerticalWallPositionPresent(new Position(row, col))) {
            borderStyle.append("10 ");
        } else {
            borderStyle.append("1 ");
        }
        if(row == grid.getRowCount()-1 ||
                model.checkIfHorizontalWallPositionPresent(new Position(row, col))) {
            borderStyle.append("10 ");
        } else {
            borderStyle.append("1 ");
        }
        if(col == 0) {
            borderStyle.append("10;");
        } else {
            borderStyle.append("1;");
        }

        square.setStyle(borderStyle.toString());
        return square;
    }


}
