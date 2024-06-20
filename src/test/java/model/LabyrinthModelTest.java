package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LabyrinthModelTest {
    Position player = new Position(0, 0);
    Position enemy = new Position(2, 4);

    Position verticalWall = new Position(1, 1);
    Position horizontalWall = new Position(0, 0);
    LabyrinthModel model;

    @BeforeEach
    void initialize() {
        model = new LabyrinthModel();
    }

    @Test
    void squareProperty() {
        assertEquals(model.squareProperty(0, 0).get(), Square.PLAYER);
        assertEquals(model.squareProperty(2, 4).get(), Square.ENEMY);
        assertEquals(model.squareProperty(3, 5).get(), Square.NONE);
    }


    @Test
    void getPosition() {
        assertEquals(player, model.getPosition());
        assertNotEquals(enemy, model.getPosition());
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
    void setSquare() {
        model.setSquare(new Position(5, 5), Square.ENEMY);
        assertEquals(model.squareProperty(5,5).get(), Square.ENEMY);

        model.setSquare(new Position(4, 4), Square.PLAYER);
        assertEquals(model.squareProperty(4,4).get(), Square.PLAYER);

        model.setSquare(new Position(3, 3), Square.NONE);
        assertEquals(model.squareProperty(3,3).get(), Square.NONE);
    }

    @Test
    void isSolved() {
        assertFalse(model.isSolved());
        model.setSolved(true);

        assertTrue(model.isSolved());
    }

    @Test
    void isLegalMove() {
        assertFalse(model.isLegalMove(Direction.UP));
        assertFalse(model.isLegalMove(Direction.DOWN));
        assertFalse(model.isLegalMove(Direction.LEFT));
        assertTrue(model.isLegalMove(Direction.RIGHT));
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

    }
}