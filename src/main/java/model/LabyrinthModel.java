package model;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import lombok.Getter;
import org.tinylog.Logger;
import puzzle.*;

import java.util.*;

/**
 * Represents the model of the labyrinth.
 */

public class LabyrinthModel implements State<Direction>{
    /**
     * The size of the board.
     */
    public static final int BOARD_SIZE = 6;

    private ReadOnlyObjectWrapper<Square>[][] board;
    private static final List<Position> verticalWalls = Arrays.asList(
            new Position(0,4),
            new Position(1,1),
            new Position(1,3),
            new Position(1,4),
            new Position(2,0),
            new Position(2,1),
            new Position(2,2),
            new Position(3,0),
            new Position(3,3),
            new Position(4,0),
            new Position(4,1)
    );
    private static final List<Position> horizontalWalls = Arrays.asList(
            new Position(0, 0),
            new Position(1, 1),
            new Position(1, 4),
            new Position(2, 2),
            new Position(2, 4),
            new Position(3, 2)
    );

    private ReadOnlyObjectWrapper<Position>[] positions;

    @Getter
    private Square turn;

    private static final int PLAYER = 0;
    private static final int ENEMY = 1;

    private ReadOnlyBooleanWrapper solved;

    private ReadOnlyBooleanWrapper gameOver;

    private int index;

    private static final Position winPosition = new Position(-1, 4);
    private static final Position finishPosition = new Position(0, 4);

    /**
     * Creates a {@code LabyrinthModel} object that represents
     * the initial state of the labyrinth.
     */
    public LabyrinthModel() {
        this(new Position(0,0),
                new Position(2, 4));
    }

    /**
     * Creates a {@code LabyrinthModel} object initializing the position
     * of the player and enemy. Expects two {@code Positon} objects.
     *
     * @param playerPosition position of the player
     * @param enemyPosition position of the enemy
     */
    public LabyrinthModel(Position playerPosition, Position enemyPosition) {
        board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];
        turn = Square.PLAYER;
        index = 0;
        solved = new ReadOnlyBooleanWrapper(false);
        gameOver = new ReadOnlyBooleanWrapper(false);
        this.positions = new ReadOnlyObjectWrapper[2];
        this.positions[0] = new ReadOnlyObjectWrapper<>(playerPosition);
        this.positions[1] = new ReadOnlyObjectWrapper<>(enemyPosition);

        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                if (i == playerPosition.row() && j == playerPosition.col())  {
                    board[i][j] = new ReadOnlyObjectWrapper<Square>(Square.PLAYER);
                }
                else if (i == enemyPosition.row() && j == enemyPosition.col()) {
                    board[i][j] = new ReadOnlyObjectWrapper<Square>(Square.ENEMY);
                }
                else board[i][j] = new ReadOnlyObjectWrapper<Square>(Square.NONE);
            }
        }
    }

    /**
     * Returns a read only property of the square located at the specified row and column.
     *
     * @param row the row index of the square on the board
     * @param col the column index of the square on the board
     * @return a ReadOnlyObjectProperty of the square at the specified position
     */
    public ReadOnlyObjectProperty<Square> squareProperty(int row, int col) {
        return board[row][col].getReadOnlyProperty();
    }

    /*
    public static void main(String[] args) {
        var model = new LabyrinthModel(new Position(0,0), new Position(2, 4));
        System.out.println(model);
        model.makeMove(Direction.RIGHT);
        System.out.println(model);
        model.makeMove(Direction.LEFT);
        System.out.println(model);
        model.makeMove(Direction.RIGHT);
        System.out.println(model);
        model.makeMove(Direction.UP);
        System.out.println(model);
        model.makeMove(Direction.RIGHT);
        System.out.println(model);
        model.makeMove(Direction.RIGHT);
        System.out.println(model);
    }
    */
    private void changeTurn() {
        changeIndex(index);
        turn = turn.nextTurn();
    }

    /**
     * {@return the position of the player or enemy, based on the current turn.}
     */
    public Position getPosition() {
        return positions[index].get();
    }

    /**
     * @return {@code true} if the game is over, {@code false} otherwise
     */
    public boolean getGameOver() {
        return gameOver.get();
    }

    /**
     * Checks if there is a vertical wall at the given position.
     *
     * @param position the position where we check the presence of the wall
     * @return {@code true} if wall is present, {@code false} otherwise
     */
    public boolean checkIfVerticalWallPositionPresent(Position position) {
        return verticalWalls.contains(position);
    }

    /**
     * Checks if there is a horizontal wall at the given position.
     *
     * @param position the position where we check the presence of the wall
     * @return {@code true} if wall is present, {@code false} otherwise
     */
    public boolean checkIfHorizontalWallPositionPresent(Position position) {
        return horizontalWalls.contains(position);
    }

    private void setPosition(int index, Position position) {
        positions[index].set(position);
    }

    /**
     * Sets the {@code solved} variables state.
     *
     * @param state {@code true} if the game is solved, {@code false} otherwise
     */
    public void setSolved(boolean state) {
        solved.set(state);
    }

    private void setSquare(Position position, Square square){
        board[position.row()][position.col()].set(square);
    }

    private void changeIndex(int index) {
        this.index = (index == 0 ? 1 : 0);
    }

    /**
     * {@return whether the puzzle is solved}
     */
    @Override
    public boolean isSolved() {
        return solved.get();
    }

    /**
     * {@return whether is it possible to move the player or enemy
     * in the direction specified}
     *
     * @param direction the direction in which the player/enemy is moving
     */
    @Override
    public boolean isLegalMove(Direction direction) {
        if(gameOver.get() == false) {
            return switch (direction) {
                case UP -> canMoveUp();
                case RIGHT -> canMoveRight();
                case DOWN -> canMoveDown();
                case LEFT -> canMoveLeft();
            };
        }
        return false;
    }

    private boolean canMoveLeft() {
        return getPosition().col() > 0 && !isMoveBlocked(Direction.LEFT);
    }

    private boolean canMoveDown() {
        return getPosition().row() < BOARD_SIZE-1 && !isMoveBlocked(Direction.DOWN);
    }

    private boolean canMoveRight() {
        return getPosition().col() < BOARD_SIZE-1 && !isMoveBlocked(Direction.RIGHT);
    }

    private boolean canMoveUp() {
        if(getPosition().equals(finishPosition)) {
            return true;
        }
        return getPosition().row() > 0 && !isMoveBlocked(Direction.UP);
    }


    /**
     * Moves the player/enemy in the direction specified.
     *
     * @param direction the direction in which the player/enemy is moving
     */
    @Override
    public void makeMove(Direction direction) {
        if(gameOver.get() == false){
            Position newPosition = getPosition().move(direction);
            if(newPosition.equals(winPosition)) {
                setSolved(true);
                setPosition(index, newPosition);
            } else {
                setSquare(getPosition(), Square.NONE);
                setSquare(newPosition, turn);
                setPosition(index, newPosition);
            }
            if(turn.equals(Square.PLAYER) && !isSolved()) {
                changeTurn();
                enemyMove();
            }
        }
    }

    /**
     * Checks that the player and enemy position is the same.
     * Sets the {@code gameOver} variable {@code true}, if the positions are the same.
     */
    public void checkGameOver() {
        if(positions[PLAYER].get().equals(positions[ENEMY].get())) {
            gameOver.set(true);
        }
    }

    private void enemyMove() {
        int numberOfMoves = 2;
        while (numberOfMoves > 0 && gameOver.get() == false) {
            int x = getDistance(positions[ENEMY].get().row(), positions[PLAYER].get().row());
            int y = getDistance(positions[ENEMY].get().col(), positions[PLAYER].get().col());
            if(y < 0 && isLegalMove(Direction.LEFT)) {
                makeMove(Direction.LEFT);
            } else if (y > 0 && isLegalMove(Direction.RIGHT)) {
                makeMove(Direction.RIGHT);
            } else if(x > 0 && isLegalMove(Direction.DOWN)) {
                makeMove(Direction.DOWN);
            } else if (x < 0 && isLegalMove(Direction.UP)) {
                makeMove(Direction.UP);
            }
            checkGameOver();
            numberOfMoves--;
        }
        changeTurn();
    }

    /**
     * Convenience method for calculating distance between two points.
     *
     * @param from the starting point
     * @param to the ending point
     * @return the distance between the two point
     */
    public int getDistance(int from, int to) {
        return to - from;
    }

    /**
     * {@return the set of all moves that can be applied to the model}
     */
    @Override
    public Set<Direction> getLegalMoves() {
        var legalMoves = new HashSet<Direction>();
        for (var direction : Direction.values()) {
            if (isLegalMove(direction)) {
                legalMoves.add(direction);
            }
        }
        return legalMoves;
    }

    @Override
    public State<Direction> clone() {
        Position playerPos = new Position(positions[PLAYER].get().row(), positions[PLAYER].get().col());
        Position enemyPos = new Position(positions[ENEMY].get().row(), positions[ENEMY].get().col());
        ReadOnlyBooleanWrapper solved = new ReadOnlyBooleanWrapper(this.solved.get());
        ReadOnlyObjectWrapper<Position>[] pos = new ReadOnlyObjectWrapper[2];
        pos[0] = new ReadOnlyObjectWrapper<>(playerPos);
        pos[1] = new ReadOnlyObjectWrapper<>(enemyPos);
        LabyrinthModel copy = new LabyrinthModel(pos[PLAYER].get(), pos[ENEMY].get());
        copy.setSolved(solved.get());
        return copy;
    }

    /**
     * Checks that the direction we want to move blocked by a wall or the enemy.
     *
     * @param direction we want to move
     * @return {@code true} if the move is blocked, {@code false} otherwise
     */
    public boolean isMoveBlocked(Direction direction) {
        Position fromPos = getPosition();
        Position toPos = fromPos.move(direction);
        if(verticalWalls.contains(fromPos) && direction == Direction.RIGHT) {
            return true;
        }
        if(verticalWalls.contains(toPos) && direction == Direction.LEFT) {
            return true;
        }
        if(horizontalWalls.contains(fromPos) && direction == Direction.DOWN) {
            return true;
        }
        if(horizontalWalls.contains(toPos) && direction == Direction.UP) {
            return true;
        }
        return toPos.equals(positions[ENEMY].get()) && turn.equals(Square.PLAYER);
    }


    @Override
    public String toString() {
        return String.format("LabyrinthModel[player=%s,enemy=%s]", positions[PLAYER].get(), positions[ENEMY].get());
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        LabyrinthModel that = (LabyrinthModel) o;
        Logger.debug(positions[PLAYER].get());
        Logger.debug(that.positions[PLAYER].get());
        return positions[PLAYER].get().equals(that.positions[PLAYER].get()) &&
                positions[ENEMY].get().equals(that.positions[ENEMY].get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(positions[PLAYER].get(), positions[ENEMY].get());
    }
}
