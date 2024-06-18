package model;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import puzzle.*;

import java.util.*;

public class LabyrinthModel implements State<Direction>{
    public static final int BOARD_SIZE = 6;

    private final ReadOnlyObjectWrapper<Square>[][] board;
    private final List<Position> verticalWalls;
    private final List<Position> horizontalWalls;

    private final ReadOnlyObjectWrapper<Position>[] positions;
    private Square turn;

    private ReadOnlyBooleanWrapper solved;
    private int index;

    public LabyrinthModel() {
        this(new Position(0,0),
                new Position(2, 4));
    }
    public LabyrinthModel(Position playerPosition, Position enemyPosition) {
        board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];
        turn = Square.PLAYER;
        index = 0;
        solved = new ReadOnlyBooleanWrapper(false);
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

        verticalWalls = new ArrayList<>();
        verticalWalls.add(new Position(0,4));
        verticalWalls.add(new Position(1,1));
        verticalWalls.add(new Position(1,3));
        verticalWalls.add(new Position(1,4));
        verticalWalls.add(new Position(2,0));
        verticalWalls.add(new Position(2,1));
        verticalWalls.add(new Position(2,2));
        verticalWalls.add(new Position(3,0));
        verticalWalls.add(new Position(3,3));
        verticalWalls.add(new Position(4,0));
        verticalWalls.add(new Position(4,1));

        horizontalWalls = new ArrayList<>();
        horizontalWalls.add(new Position(0, 0));
        horizontalWalls.add(new Position(1, 1));
        horizontalWalls.add(new Position(1, 4));
        horizontalWalls.add(new Position(2, 2));
        horizontalWalls.add(new Position(2, 4));
        horizontalWalls.add(new Position(3, 2));
    }

    public ReadOnlyObjectProperty<Square> squareProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }


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

    public Position getPosition() {
        return positions[index].get();
    }

    public ReadOnlyBooleanProperty solvedProperty() {
        return solved.getReadOnlyProperty();
    }


    public boolean checkIfVerticalWallPositionPresent(Position position) {
        return verticalWalls.contains(position);
    }
    public boolean checkIfHorizontalWallPositionPresent(Position position) {
        return horizontalWalls.contains(position);
    }

    private void setPosition(int index, Position position) {
        positions[index].set(position);
    }

    public void setSquare(Position position, Square square){
        board[position.row()][position.col()].set(square);
    }

    private void changeIndex(int index) {
        this.index = (index == 0 ? 1 : 0);
    }
    @Override
    public boolean isSolved() {
        return false;
    }

    @Override
    public boolean isLegalMove(Direction direction) {
        return switch (direction) {
            case UP -> canMoveUp();
            case RIGHT -> canMoveRight();
            case DOWN -> canMoveDown();
            case LEFT -> canMoveLeft();
        };
    }

    private boolean canMoveLeft() {
        return getPosition().col() > 0 && !isMoveBlocked(Direction.LEFT);
    }

    private boolean canMoveDown() {
        return getPosition().row() < BOARD_SIZE-1 && !isMoveBlocked(Direction.DOWN);
    }

    private boolean canMoveRight() {
        return getPosition().col() < BOARD_SIZE && !isMoveBlocked(Direction.RIGHT);
    }

    private boolean canMoveUp() {
        return getPosition().row() > 0 && !isMoveBlocked(Direction.UP);
    }

    @Override
    public void makeMove(Direction direction) {
        Position newPosition = getPosition().move(direction);
        setSquare(getPosition(), Square.NONE);
        setSquare(newPosition, turn);
        setPosition(index, newPosition);
        changeIndex(index);
        turn = turn.nextTurn();
    }

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
        return new LabyrinthModel(positions[0].get(), positions[1].get());
    }

    public boolean isMoveBlocked(Direction direction) {
        Position fromPos = getPosition();
        Position toPos;
        if(verticalWalls.contains(fromPos) && direction == Direction.RIGHT) {
            return true;
        }
        toPos = fromPos.moveLeft();
        if(verticalWalls.contains(toPos) && direction == Direction.LEFT) {
            return true;
        }
        if(horizontalWalls.contains(fromPos) && direction == Direction.DOWN) {
            return true;
        }
        toPos = fromPos.moveUp();
        if(horizontalWalls.contains(toPos) && direction == Direction.UP) {
            return true;
        }

        return false;
    }
    

    public String toString() {
        var sb = new StringBuilder();
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                sb.append(board[i][j].get().ordinal()).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
