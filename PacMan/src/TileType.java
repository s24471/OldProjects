public enum TileType {
    WALL(1),
    EMPTY(0),
    PLAYER_START(2),
    GHOST_SPAWN(3);

    private final int value;

    TileType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
