package model;

public enum Square {
    NONE,
    PLAYER,
    ENEMY;

    public Square nextTurn() {
        return switch(this) {
            case PLAYER -> Square.ENEMY;
            case ENEMY -> Square.PLAYER;
            default -> Square.NONE;
        };
    }
}
