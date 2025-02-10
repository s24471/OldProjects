public class Board {
    int szer;
    int dl;
    Rules rules;
    Tile[][] tiles;

    public Board(int szer, int dl, Rules rules) {
        this.szer = szer;
        this.dl = dl;
        this.rules = rules;
        tiles = new Tile[szer][dl];
        for (int i = 0; i < szer; i++) {
            for (int j = 0; j < dl; j++) {
                tiles[i][j] = new Tile();
            }
        }
    }

    public boolean nextGen(){
        boolean[][] copy = new boolean[szer][dl];
        for (int i = 0; i < szer; i++) {
            for (int j = 0; j < dl; j++) {
                copy[i][j] = false;
                copy[i][j] = rules.isAlive(countN(i, j),tiles[i][j].alive);
            }
        }
        boolean changed = false;
        for (int i = 0; i < szer; i++) {
            for (int j = 0; j < dl; j++) {
                if(tiles[i][j].alive!=copy[i][j]){
                    tiles[i][j].alive = copy[i][j];
                    changed = true;
                }
            }
        }
        return changed;
    }

    public int borderX(int x){
        if(x<0)return szer-1;
        if(x>=szer)return 0;
        return x;
    }
    public int borderY(int y){
        if(y<0)return dl-1;
        if(y>=dl)return 0;
        return y;
    }
    public int countN(int x, int y){
        int sum =0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(i == 0 && j == 0)continue;
                if(tiles[borderX(x+i)][borderY(y+j)].alive){
                    sum++;
                }
            }
        }
        return sum;
    }
}
