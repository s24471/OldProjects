package Tile;

public class TileWall extends Tile{
    public TileWall(int y, int x) {
        super(y, x);
        setSpriteName("TileWall");

        calculateRarity();
    }

}
