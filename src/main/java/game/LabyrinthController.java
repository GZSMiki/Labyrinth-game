package game;

import gameresult.OnePlayerGameResult;
import gameresult.manager.GameResultManager;
import gameresult.manager.json.JsonOnePlayerGameResultManager;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class LabyrinthController {
    @FXML
    private GridPane grid;

    @FXML
    private TextField numberOfMovesField;

    @FXML
    private Label usernameLabel;

    private LabyrinthModel model;

    private final IntegerProperty numberOfMoves = new SimpleIntegerProperty(0);

    private LocalDateTime startTime = LocalDateTime.now();

    @FXML
    private void initialize() {
        restartGame();
        bindNumberOfMoves();
    }

    public String getUsername() {
        return this.usernameLabel.getText();
    }
    public void setUsername(String name) {
        usernameLabel.setText(name);
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
        square.getStyleClass().add("square");
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    private void restartGame() {
        model = new LabyrinthModel();
        numberOfMoves.set(0);
        grid.getChildren().clear();
        for (int i = 0; i < grid.getRowCount(); i++) {
            for (int j = 0; j < grid.getColumnCount(); j++) {
                var square = createSquare(i, j);
                grid.add(square, j, i);
            }
        }
        startTime = LocalDateTime.now();
        Logger.info("Game restarted!");
    }

    

    @FXML
    private void restartButtonClicked() {
        restartGame();
    }
    @FXML
    private void winButtonClicked() {
        if(model.getPosition().equals(LabyrinthModel.FINISH_POSITION) &&
                model.getTurn().equals(Square.PLAYER)) {
            numberOfMoves.set(numberOfMoves.get() + 1);
            model.makeMove(Direction.UP);
            Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
            winAlert.setTitle("Winner");
            winAlert.setContentText("You completed the labyrinth with "+numberOfMoves.get()+" moves!");
            winAlert.showAndWait();
            Logger.info("{} completed the labyrinth!", usernameLabel.getText());
            handleWinAndSave();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong position");
            alert.setContentText("You are not in the right position to complete the labyrinth!");
            alert.showAndWait();
        }

    }

    @FXML
    private void handleWinAndSave() {
        try {
            Path path = Path.of("gameresult.json");
            if (!Files.exists(path)){
                Files.createFile(path);
                Files.writeString(path, "[]");

            }
            GameResultManager<OnePlayerGameResult> manager =
                    new JsonOnePlayerGameResultManager(Path.of("gameresult.json"));
            manager.add(createGameResult());
            Logger.info("Added game result to JSON file.");
        } catch (IOException e) {
            Logger.error("Failed to save game result: {}", e.getMessage());
        }
    }

    private OnePlayerGameResult createGameResult() {
        return OnePlayerGameResult.builder()
                .playerName(getUsername())
                .solved(true)
                .numberOfMoves(numberOfMoves.get())
                .duration(Duration.ofSeconds(ChronoUnit.SECONDS.between(startTime, LocalDateTime.now())))
                .created(ZonedDateTime.now())
                .build();
    }


    private List<Circle> createPlayerAndEnemyCircle(int row, int col) {
        List<Circle> circles;
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
            numberOfMoves.set(numberOfMoves.get() + 1);
            if(model.getGameOver()) {
                Alert gameOverAlert = new Alert(Alert.AlertType.INFORMATION);
                gameOverAlert.setTitle("Game Over");
                gameOverAlert.setContentText("You got caught by the enemy!");
                gameOverAlert.showAndWait();
            }
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
