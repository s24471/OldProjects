package Tile;

public class TilePath extends Tile {
    public TilePath(int y, int x) {
        super(y, x);
        setSpriteName("TilePath");
        setWakable(true);           //po ścieżce można chodzić
        setSeeTrought(true);        //przez ścieżkę można patrzeć

        calculateRarity();
    }

}
