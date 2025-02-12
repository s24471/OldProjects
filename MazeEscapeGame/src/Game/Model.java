package Game;

import Entities.*;
import Items.*;
import Tile.Tile;

import java.util.ArrayList;
import java.util.Random;

import static Util.Util.mySleep;

public class Model {
    private Board board;
    private View view;

    private ArrayList<Entity> entities;
    private Player player;
    private Zombie zombie;
    private Key key;


    public Model(int height, int width, Board board, View view) {
        this.view = view;
        this.board = board;
        board.setHeight(height);
        board.setWidth(width);

        entities = new ArrayList<>();

        initPlayer();
        player.latarka();

        board.startTiles();

        placeKey();
    }

    private void initPlayer() {
        Random r = new Random();
        ArrayList<Tile> tmp = board.getWakableTiles();
        Tile t = tmp.get(r.nextInt(tmp.size()));        //losowanie pola startowego
        player = new Player(t.getY(), t.getX(), this);                  //stworzenie gracza
        t.setEntity(player);                              //dodanie go do pola, na którym ma się pojawić
    }

    public void initZombie(int zombiesQuantity, int zombieIncreaseRate) {
        for (int i = 0; i < zombiesQuantity; i++) {
            Random r = new Random();
            ArrayList<Tile> tmp = board.getWakableTiles();
            Tile t = tmp.get(r.nextInt(tmp.size()));            //losowanie pola startowego
            zombie = new Zombie(t.getY(), t.getX(), this);                  //stworzenie zombie
            t.setEntity(zombie);
            t.setWakable(false);
            mySleep(zombieIncreaseRate);
        }
    }

    public void placeKey() {
        Random r = new Random();
        ArrayList<Tile> tmp = board.getWakableTiles();
        Tile t = tmp.get(r.nextInt(tmp.size()));            //losowanie pola startowego
        key = new Key(t.getY(), t.getX(), this);                  //stworzenie zombie
        t.setItem(key);
    }

    public void addEntity(Entity entity) {       //po dodaniu do modelu od razu now wątek
        entities.add(entity);
        new Thread(entity).start();
    }

    //gettery i settery
    public Board getBoard() {
        return board;
    }

    public Player getPlayer() {
        return player;
    }


    public Key getKey() {
        return key;
    }
}
