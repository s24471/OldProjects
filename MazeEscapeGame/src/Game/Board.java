package Game;

import java.util.ArrayList;
import Tile.*;

import static Util.Util.*;

public class Board {
    private int width;          //do "j" w forach i "x" tak ogólnie (x lewo-prawo)
    private int height;         //do "i: w forach i "y" tak ogólnie (y góra-dól)
    private Tile[][] maze;

    public Board(int height, int width) {
        this.maze = new Tile[height][width];            //inicjalizacja planszy o danych rozmiarach
        this.width = width;
        this.height = height;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 || i == height - 1 || j == 0 || j == width - 1) {
                    maze[i][j] = new TileWall(i, j);        //ustawianie ścian na krańcach planszy
                } else
                    maze[i][j] = new TileEmpty(i, j);       //wypełnienei planszy wolnymi polami
            }
        }
    }

    public void change(int y, int x, Tile t) {           //zamiana pola z dowolnego (pustego) na ścianę/ścieżkę/puste (na puste się nigdy nie zmienia)
        maze[y][x] = t;
        mySleep(0);                                 //szybkość tworzenia labiryntu
    }

    public ArrayList<Tile> getWakableTiles() {           //wybór możliwych pól startowych
        ArrayList<Tile> tmp = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (maze[i][j].isWakable() && !(maze[i][j] instanceof TileDoor))
                    tmp.add(maze[i][j]);
            }
        }
        return tmp;
    }

    public void startTiles(){                    //żeby tile się animowały
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                new Thread(maze[y][x]).start();
            }
        }
    }

    //settery i gettery

    public Tile getTile(int y, int x) {
        return maze[y][x];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
