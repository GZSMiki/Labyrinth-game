package game;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import model.Direction;
import model.LabyrinthModel;
import org.tinylog.Logger;

public class LabyrinthController {
    @FXML
    private GridPane grid;

    @FXML
    private TextField numberOfMovesField;

    private LabyrinthModel model;

    private final IntegerProperty numberOfMoves = new SimpleIntegerProperty(0);

    @FXML
    private void initialize() {
        bindNumberOfMoves();
    }

    private void bindNumberOfMoves() {
        numberOfMovesField.textProperty().bind(numberOfMoves.asString());
    }
    private void registerKeyEventHandler() {
        Platform.runLater(() -> grid.getScene().setOnKeyPressed(this::handleKeyPress));
    }

    @FXML
    private void handleKeyPress(KeyEvent keyEvent) {
        var restartKeyCombination = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
        var quitKeyCombination = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
        if (restartKeyCombination.match(keyEvent)) {
            Logger.debug("Restarting game");
            restartGame();
        } else if (quitKeyCombination.match(keyEvent)) {
            Logger.debug("Exiting");
            Platform.exit();
        } else if (keyEvent.getCode() == KeyCode.UP) {
            Logger.debug("UP pressed");
            makeMoveIfLegal(Direction.UP);
        } else if (keyEvent.getCode() == KeyCode.RIGHT) {
            Logger.debug("RIGHT pressed");
            makeMoveIfLegal(Direction.RIGHT);
        } else if (keyEvent.getCode() == KeyCode.DOWN) {
            Logger.debug("DOWN pressed");
            makeMoveIfLegal(Direction.DOWN);
        } else if (keyEvent.getCode() == KeyCode.LEFT) {
            Logger.debug("LEFT pressed");
            makeMoveIfLegal(Direction.LEFT);
        }
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

    private void restartGame() {
        //createModel();
        numberOfMoves.set(0);
        //celarAndCreateGrid();
        //TODO
    }
}
