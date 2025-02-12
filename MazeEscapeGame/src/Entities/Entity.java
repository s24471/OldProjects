package Entities;

import Game.Drawable;
import Game.Model;

import java.awt.*;
import java.util.ArrayList;

import Game.View;
import Items.Item;

import static Util.Util.*;

abstract public class Entity implements Runnable, Drawable {
    private boolean isAlive;
    private int x;
    private int y;
    private Model model;
    private int interval;
    private String spriteName;
    private String spriteDirection;
    private int spriteCurrent;
    private int direction;

    private ArrayList<Item> eq;

    public Entity(int y, int x, Model model) {
        this.model = model;
        this.x = x;
        this.y = y;
        direction = UP;         //kierunek do patrzenia się przy pojawieniu
        spriteDirection = "Up";
        spriteCurrent = 0;
        interval = 1000;
        isAlive = true;
        model.getBoard().getTile(y, x).setWakable(false);

        model.addEntity(this);
    }

    @Override
    public void run() {
        while (isAlive) {
            action();

            mySleep(interval);
        }
    }

    public void tryMove(int y, int x) {                  //sprawdzenie, czy można się ruszyć na wybrane polew, x zamienione z y!!!
        direction = (x - this.x) - 2 * (y - this.y);      //równanie określające kierunek na podstawie nowych i starych koordynatów
        spriteDirection = switch (direction) {
            case UP -> "Up";
            case DOWN -> "Down";
            case RIGHT -> "Right";
            case LEFT -> "Left";
            default -> throw new RuntimeException("nieoczekiwana wartość \"direction\"");
        };
        if (isValidToMove(y, x)) {
            move(y, x);
        }
    }

    public void move(int y, int x) {
        model.getBoard().getTile(this.y, this.x).tileWalkedOff();   //zejście ze starego pola
        model.getBoard().getTile(y, x).tileWalkedOn(this);      //wejście na nowe pole

        this.x = x;
        this.y = y;
    }

    public boolean isValidToMove(int y, int x) {         //sprawdzenie, czy pole jest możliwe do chodzenia po
        return isValidX(x, model.getBoard()) &&      //to powinno zapobiegać wyjściu za plansz, ale nie działa
                isValidY(y, model.getBoard()) &&
                model.getBoard().getTile(y, x).isWakable();
    }

    public void action() {

    }

    @Override
    public void draw(Graphics g, int y, int x) {         //rysowanie odpowiedniego sprite'a
        g.drawImage(Drawable.sprites.get(spriteName + spriteDirection).get(spriteCurrent), x, y, View.tileWidth, View.tileHeight, null);

    }

    //gettery i settery


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setSpriteName(String spriteName) {
        this.spriteName = spriteName;
    }

    public int getDirection() {
        return direction;
    }


    public Model getModel() {
        return model;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ArrayList<Item> getEq() {
        return eq;
    }

    public void setEq(ArrayList<Item> eq) {
        this.eq = eq;
    }
}
