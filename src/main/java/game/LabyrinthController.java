package game;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import model.Direction;
import model.LabyrinthModel;
import model.Position;
import model.Square;
import org.tinylog.Logger;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LabyrinthController {
    @FXML
    private GridPane grid;

    @FXML
    private TextField numberOfMovesField;

    @FXML
    private Button checkWinButton;

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

        List<Circle> circle = createPlayerAndEnemyCircle(row, col);
        square.getChildren().add(circle.get(0));
        square.getChildren().add(circle.get(1));
        square.setStyle(borderStyle.toString());
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    @FXML
    private void winButtonClicked() {
        if(model.getPosition().equals(new Position(0, 4)) &&
                model.getTurn().equals(Square.PLAYER)) {
            Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
            winAlert.setTitle("Winner");
            winAlert.setContentText("You win!");
            winAlert.showAndWait();
            Logger.debug("You win!");
        }
        Logger.debug("itt vagyok");
    }

    private List<Circle> createPlayerAndEnemyCircle(int row, int col) {
        List<Circle> circles = new ArrayList<>();
        var playerCircle = new Circle(20);
        var enemyCircle = new Circle(20);

        playerCircle.fillProperty().bind(createSquareBinding(model.squareProperty(row, col)));
        enemyCircle.fillProperty().bind(createSquareBinding(model.squareProperty(row, col)));
        circles = List.of(playerCircle, enemyCircle);
        return circles;
    }

    private ObservableValue<? extends Paint> createSquareBinding(ReadOnlyObjectProperty<Square> property) {
        return new ObjectBinding<Paint>() {
            {
                super.bind(property);
            }
            @Override
            protected Paint computeValue() {
                return switch (property.get()) {
                    case NONE -> Color.TRANSPARENT;
                    case PLAYER -> Color.LIGHTBLUE;
                    case ENEMY -> Color.BLACK;
                };
            }
        };
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
        if (model.isLegalMove(direction) && model.getTurn().equals(Square.PLAYER)) {
            Logger.info("Moving {}", direction);
            model.makeMove(direction);
            Logger.trace("New state after move: {}", model);
            //model.changeTurn();
            numberOfMoves.set(numberOfMoves.get() + 1);
            //model.enemyMove();
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
