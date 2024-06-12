import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import puzzle.*;

import java.util.*;

public class LabyrinthModel implements State<Position>{
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
        System.out.println(model);
        model.makeMove(new Position(0,1));
        System.out.println(model);
        model.makeMove(new Position(2, 3));
        System.out.println(model);
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
    public boolean isLegalMove(Position position) {
        if(isOnBoard(position)) {
            if(!isMoveBlocked(position) && isMoveDistanceOne(position)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void makeMove(Position pos) {
        Position currentPos = getCurrentTurnPosition(turn);
        if(isLegalMove(pos)) {
            board[pos.row()][pos.col()].set(turn);
            board[currentPos.row()][currentPos.col()].set(Square.NONE);
            nextTurn();
            //TODO: make ai for the enemy, then call it here, if the turn is Enemy
        }
    }

    @Override
    public Set<Position> getLegalMoves() {
        Position currentPos = getCurrentTurnPosition(turn);
        var legalMoves = new HashSet<Position>();
        Position up = new Position(currentPos.row()+1, currentPos.col());
        Position down = new Position(currentPos.row()-1, currentPos.col());
        Position left = new Position(currentPos.row(), currentPos.col()-1);
        Position right = new Position(currentPos.row(), currentPos.col()+1);

        if(isLegalMove(up)) {
            legalMoves.add(up);
        }
        if(isLegalMove(down)) {
            legalMoves.add(down);
        }
        if(isLegalMove(left)) {
            legalMoves.add(left);
        }
        if(isLegalMove(right)) {
            legalMoves.add(right);
        }
        return legalMoves;
    }

    @Override
    public State<Position> clone() {
        return null;
    }

    public boolean isMoveBlocked(Position position) {
        Position currentPos = getCurrentTurnPosition(turn);
        direction = Direction.of(position.row() - currentPos.row(),
                position.col() - currentPos.col());

        if(verticalWalls.contains(currentPos) && direction == Direction.RIGHT) {
            return true;
        }
        if(verticalWalls.contains(position) && direction == Direction.LEFT) {
            return true;
        }
        if(horizontalWalls.contains(currentPos) && direction == Direction.DOWN) {
            return true;
        }
        if(horizontalWalls.contains(position) && direction == Direction.UP) {
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
