import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import puzzle.*;

import java.util.*;

public class LabyrinthModel implements State<Direction>{
    public static final int BOARD_SIZE = 6;

    private final ReadOnlyObjectWrapper<Square>[][] board;
    private List<Position> verticalWalls;
    private List<Position> horizontalWalls;

    private Position Player;
    private Position Enemy;
    private Square turn;

    private Direction direction;
    public LabyrinthModel() {
        board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];
        turn = Square.PLAYER;

        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                if (i == 0 && j == 0)  {
                    board[i][j] = new ReadOnlyObjectWrapper<Square>(Square.PLAYER);
                    Player = new Position(i, j);
                }

                else if (i == 2 && j == 4) {
                    board[i][j] = new ReadOnlyObjectWrapper<Square>(Square.ENEMY);
                    Enemy = new Position(i, j);
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
        var model = new LabyrinthModel();
        
    }

    private Position getCurrentTurnPosition(Square turn) {
        if(turn == Square.PLAYER) return Player;
        return Enemy;
    }

    private void nextTurn() {
        turn = turn.equals(Square.PLAYER) ? Square.ENEMY : Square.PLAYER;
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

        /*
        if(isOnBoard(position)) {
            if(!isMoveBlocked(position) && isMoveDistanceOne(position)){
                return true;
            }
        }
        return false;
         */
    }

    private boolean canMoveLeft() {
        return getCurrentTurnPosition(turn).col() > 0 && !isMoveBlocked(Direction.LEFT);
    }

    private boolean canMoveDown() {
        return getCurrentTurnPosition(turn).row() > 0 && !isMoveBlocked(Direction.DOWN);
    }

    private boolean canMoveRight() {
        return getCurrentTurnPosition(turn).col() < BOARD_SIZE && !isMoveBlocked(Direction.RIGHT);
    }

    private boolean canMoveUp() {
        return getCurrentTurnPosition(turn).row() > 0 && !isMoveBlocked(Direction.UP);
    }

    @Override
    public void makeMove(Direction direction) {

        switch(direction) {
            case UP -> moveUp(turn);
            case DOWN -> moveDown(turn);
            case LEFT -> moveLeft(turn);
            case RIGHT -> moveRight(turn);
        }
        turn = turn.equals(Square.PLAYER) ? Square.ENEMY : Square.PLAYER;
        /*
        Position currentPos = getCurrentTurnPosition(turn);
        if(isLegalMove(direction)) {
            board[pos.row()][pos.col()].set(turn);
            board[currentPos.row()][currentPos.col()].set(Square.NONE);
            nextTurn();
            //TODO: make ai for the enemy, then call it here, if the turn is Enemy
        }

         */
    }

    private void moveRight(Square turn) {
    }

    private void moveLeft(Square turn) {
        
    }

    private void moveDown(Square turn) {
        
    }

    private void moveUp(Square turn) {
        Position newPosition = turn.equals(Square.PLAYER) ? Player : Enemy;
        newPosition.move(Direction.UP);
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
        return null;
    }

    public boolean isMoveBlocked(Direction direction) {
        Position fromPos = getCurrentTurnPosition(turn);
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

    private boolean isMoveDistanceOne(Position position) {
        Position currentPos = getCurrentTurnPosition(turn);
        int rowDistance = position.row() - currentPos.row();
        int colDistance = position.col() - currentPos.col();
        if(Math.abs(rowDistance) > 1 || Math.abs(colDistance) > 1) {
            return false;
        }
        return true;
    }
    private Square getSquare(Position pos) {
        int row = pos.row();
        int col = pos.col();
        return board[row][col].get();
    }

    private boolean isOnBoard(Position position) {
        if(position.row() < 0 || position.col() < 0 ||
                position.row() > BOARD_SIZE || position.col() > BOARD_SIZE) {
            return false;
        }
        return true;
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
