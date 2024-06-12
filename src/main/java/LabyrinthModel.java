import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import puzzle.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LabyrinthModel implements State<Position>{
    public static final int BOARD_SIZE = 6;

    private final ReadOnlyObjectWrapper<Square>[][] board;
    private List<Position> verticalWalls;
    private List<Position> horizontalWalls;

    private Position Player;
    private Position Enemy;
    public LabyrinthModel() {
        board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];
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

    public static void main(String[] args) {
        var model = new LabyrinthModel();
        System.out.println(model);
        model.makeMove(new TwoPhaseMove<>(new Position(0, 0), new Position(1, 0)));
        System.out.println(model);
        model.makeMove(new TwoPhaseMove<>(new Position(0, 0), new Position(0, 1)));
        System.out.println(model);
        model.makeMove(new TwoPhaseMove<>(new Position(0, 1), new Position(0, 2)));
        System.out.println(model);
        model.makeMove(new TwoPhaseMove<>(new Position(0, 2), new Position(0, 3)));
        System.out.println(model);
        model.makeMove(new TwoPhaseMove<>(new Position(0, 3), new Position(0, 4)));
        System.out.println(model);
    }

    @Override
    public boolean isSolved() {
        return false;
    }

    @Override
    public boolean isLegalMove(Position position) {
        return false;
    }

    @Override
    public void makeMove(Position position) {

    }

    @Override
    public Set<Position> getLegalMoves() {
        return null;
    }

    /*
    @Override
    public boolean isLegalMove(TwoPhaseMove<Position> positions) {
        return isLegalToMoveFrom(positions.from()) &&
                !isMoveBlocked(positions);
    }

    public boolean isMoveBlocked(TwoPhaseMove<Position> positions) {
        Position from = positions.from();
        Position to = positions.to();
        if(verticalWalls.contains(from) && (to.col() - from.col() == 1)) {
            return true;
        }
        if(verticalWalls.contains(to) && (to.col() - from.col() == -1)) {
            return true;
        }
        if(horizontalWalls.contains(from) && (to.row() - from.col() == 1)) {
            return true;
        }
        if(horizontalWalls.contains(to) && (to.col() - from.col() == -1)) {
            return true;
        }

        return false;
    }

    @Override
    public void makeMove(TwoPhaseMove<Position> pos) {
        if(isLegalMove(pos)) {
            board[pos.to().row()][pos.to().col()]
                    .set(board[pos.from().row()][pos.from().col()].get());
            board[pos.from().row()][pos.from().col()].set(Square.NONE);
        }
    }

    @Override
    public Set<TwoPhaseMove<Position>> getLegalMoves() {
        return null;
    }

     */

    private boolean isPlayer(Position position) {
        return getSquare(position) == Square.PLAYER;
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

    @Override
    public TwoPhaseMoveState<Position> clone() {
        return null;
    }
}
