package model;

import java.util.Objects;

public record Position(int row, int col) {
    public Position move(Direction direction) {
        return new Position(row + direction.getRowChange(), col + direction.getColChange());
    }

    /**
     * Convenience method that is equivalent to {@code move(model.Direction.UP)}.
     *
     * @return the position above this position
     */
    public Position moveUp() {
        return move(Direction.UP);
    }

    /**
     * Convenience method that is equivalent to {@code move(model.Direction.RIGHT)}.
     *
     * @return the position to the right of this position
     */
    public Position moveRight() {
        return move(Direction.RIGHT);
    }

    /**
     * Convenience method that is equivalent to {@code move(model.Direction.DOWN)}.
     *
     * @return the position below this position
     */
    public Position moveDown() {
        return move(Direction.DOWN);
    }

    /**
     * Convenience method that is equivalent to {@code move(model.Direction.LEFT)}.
     *
     * @return the position to the left of this position
     */
    public Position moveLeft() {
        return move(Direction.LEFT);
    }
    @Override
    public String toString() {
        return String.format("(%d, %d)", row, col);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
