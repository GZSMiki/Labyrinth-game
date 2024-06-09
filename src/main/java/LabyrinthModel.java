import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import puzzle.*;

import java.util.Set;

public class LabyrinthModel implements TwoPhaseMoveState<Position>{
    public static final int BOARD_SIZE = 6;

    private final ReadOnlyObjectWrapper<Square>[][] board;
    private int[][] verticalWalls;
    private int[][] horizontalWalls;
    public LabyrinthModel() {
        board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                if (i == 0 && j == 0)  board[i][j] = new ReadOnlyObjectWrapper<Square>(Square.PLAYER);
                else if (i == 2 && j == 4) board[i][j] = new ReadOnlyObjectWrapper<Square>(Square.ENEMY);
                else board[i][j] = new ReadOnlyObjectWrapper<Square>(Square.NONE);
            }
        }
    }

    public ReadOnlyObjectProperty<Square> squareProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }

    public void move(int fromX, int fromY, int toX, int toY) {
        board[toX][toY].set(board[fromX][fromY].get());
        board[fromX][fromY].set(Square.NONE);
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
        model.move(0, 0, 0, 1);
        System.out.println(model);
    }

    @Override
    public boolean isLegalToMoveFrom(Position position) {
        return false;
    }

    @Override
    public boolean isSolved() {
        return false;
    }

    @Override
    public boolean isLegalMove(TwoPhaseMove<Position> positionTwoPhaseMove) {
        return false;
    }

    @Override
    public void makeMove(TwoPhaseMove<Position> positionTwoPhaseMove) {

    }

    @Override
    public Set<TwoPhaseMove<Position>> getLegalMoves() {
        return null;
    }

    @Override
    public TwoPhaseMoveState<Position> clone() {
        return null;
    }
}
