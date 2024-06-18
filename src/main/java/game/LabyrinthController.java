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
        bindNumberOfMoves();
    }

    private void bindNumberOfMoves() {
        numberOfMovesField.textProperty().bind(numberOfMoves.asString());
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
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }
    @FXML
    private void handleMouseClick(MouseEvent event) {
        var source = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(source);
        var col = GridPane.getColumnIndex(source);
        Logger.debug("Click on square ({},{})", row, col);
        getDirectionFromClick(row, col).ifPresentOrElse(this::makeMoveIfLegal,
                () -> Logger.warn("Click does not correspond to any of the directions"));
    }

    private void makeMoveIfLegal(Direction direction) {
        if (model.isLegalMove(direction)) {
            Logger.info("Moving {}", direction);
            model.makeMove(direction);
            Logger.trace("New state after move: {}", model);
            numberOfMoves.set(numberOfMoves.get() + 1);
        } else {
            Logger.warn("Illegal move: {}", direction);
        }
    }

    private Optional<Direction> getDirectionFromClick(Integer row, Integer col) {
        var positionOfCircle = model.getPosition();
        try {
            return Optional.of(Direction.of(row - positionOfCircle.row(), col - positionOfCircle.col()));
        } catch (IllegalArgumentException e) {
            // The click does not correspond to any of the four directions
        }
        return Optional.empty();
    }
}
