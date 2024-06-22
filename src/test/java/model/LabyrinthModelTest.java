package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tinylog.Logger;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LabyrinthModelTest {
    Position player = new Position(0, 0);
    Position enemy = new Position(2, 4);

    Position verticalWall = new Position(1, 1);
    Position horizontalWall = new Position(0, 0);
    LabyrinthModel model;
    LabyrinthModel movedRight;
    LabyrinthModel movedDown;
    LabyrinthModel beforeWin;

    @BeforeEach
    void setUp() {
        model = new LabyrinthModel();
        movedRight = new LabyrinthModel();
        movedRight.makeMove(Direction.RIGHT);

        movedDown = new LabyrinthModel();
        movedDown.makeMove(Direction.DOWN);

        beforeWin = new LabyrinthModel(new Position(0, 4), new Position(5, 5));

    }

    @Test
    void squareProperty() {
        assertEquals(model.squareProperty(0, 0).get(), Square.PLAYER);
        assertEquals(model.squareProperty(2, 4).get(), Square.ENEMY);
        assertEquals(model.squareProperty(3, 5).get(), Square.NONE);

        assertEquals(movedRight.squareProperty(0, 1).get(), Square.PLAYER);
        assertEquals(movedRight.squareProperty(1, 3).get(), Square.ENEMY);
        assertEquals(movedRight.squareProperty(0, 0).get(), Square.NONE);
    }


    @Test
    void getPosition() {
        assertEquals(player, model.getPosition());
        assertNotEquals(enemy, model.getPosition());

        assertNotEquals(player, movedRight.getPosition());
        assertNotEquals(player, movedDown.getPosition());
    }

    @Test
    void checkIfVerticalWallPositionPresent() {
        assertTrue(model.checkIfVerticalWallPositionPresent(verticalWall));
        assertFalse(model.checkIfVerticalWallPositionPresent(horizontalWall));
    }

    @Test
    void checkIfHorizontalWallPositionPresent() {
        assertTrue(model.checkIfHorizontalWallPositionPresent(verticalWall));
        assertTrue(model.checkIfHorizontalWallPositionPresent(horizontalWall));
    }

    @Test
    void isSolved() {
        assertFalse(model.isSolved());
        model.setSolved(true);
        assertTrue(model.isSolved());

        assertFalse(beforeWin.isSolved());
        beforeWin.makeMove(Direction.UP);
        assertTrue(beforeWin.isSolved());
    }

    @Test
    void isLegalMove() {
        assertFalse(model.isLegalMove(Direction.UP));
        assertFalse(model.isLegalMove(Direction.DOWN));
        assertFalse(model.isLegalMove(Direction.LEFT));
        assertTrue(model.isLegalMove(Direction.RIGHT));
        assertTrue(beforeWin.isLegalMove(Direction.UP));
    }

    @Test
    void makeMove() {
        model.makeMove(Direction.RIGHT);
        assertEquals(new Position(0, 1), model.getPosition());

        model.makeMove(Direction.DOWN);
        assertEquals(new Position(1, 1), model.getPosition());

        model.makeMove(Direction.UP);
        assertEquals(new Position(0, 1), model.getPosition());

        //Player getting caught by enemy, so it cant move
        model.makeMove(Direction.LEFT);
        assertEquals(new Position(0, 1), model.getPosition());
    }

    @Test
    void checkGameOver() {
        assertFalse(model.getGameOver());

        model = new LabyrinthModel(new Position(0, 0), new Position(0, 0));
        model.checkGameOver();

        assertTrue(model.getGameOver());
    }


    @Test
    void getDistance() {
        assertEquals(0, model.getDistance(2, 2));
        assertEquals(2, model.getDistance(0, 2));
        assertEquals(-2, model.getDistance(2, 0));
    }

    @Test
    void getLegalMoves() {
        Set<Direction> legalMovesFromStart = new HashSet<Direction>();
        legalMovesFromStart.add(Direction.RIGHT);
        assertEquals(legalMovesFromStart, model.getLegalMoves());

        Set<Direction> illegalMovesFromStart = new HashSet<Direction>();
        illegalMovesFromStart.add(Direction.UP);
        assertNotEquals(illegalMovesFromStart, model.getLegalMoves());
    }

    @Test
    void isMoveBlocked() {
        assertTrue(model.isMoveBlocked(Direction.DOWN));
        model = new LabyrinthModel(new Position(0, 0), new Position(0, 1));
        assertTrue(model.isMoveBlocked(Direction.RIGHT));
    }

    @Test
    void testToString() {
        String initialState = "LabyrinthModel[player=(0, 0),enemy=(2, 4)]";
        assertEquals(initialState, model.toString());
    }

    @Test
    void testEquals() {
        assertTrue(model.equals(model));
        assertTrue(model.equals(new LabyrinthModel()));
        assertFalse(model.equals(null));
        assertFalse(model.equals("Walter White"));
        var clone = model.clone();
        assertTrue(clone.equals(model));
        clone.makeMove(Direction.RIGHT);
        assertFalse(model.equals(clone));
    }

    @Test
    void testClone() {
        var clone = model.clone();
        assertTrue(clone.equals(model));
        assertNotSame(clone, model);
    }

    @Test
    void testHashCode() {
        assertTrue(model.hashCode() == model.hashCode());
        assertTrue(model.hashCode() == model.clone().hashCode());
    }
}