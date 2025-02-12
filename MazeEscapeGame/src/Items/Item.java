package Items;

import Game.Drawable;
import Game.Model;
import Game.View;

import java.awt.*;

public abstract class Item implements Drawable {
    private Model model;
    int y;
    int x;
    boolean isPickedUp;
    private String spriteName;
    private int spriteCurrent;

    public Item(int y, int x, Model model) {
        this.y = y;
        this.x = x;
        spriteCurrent = 0;
        isPickedUp = false;
        this.model = model;
    }


    public void use(){

    }

    @Override
    public void draw(Graphics g, int y, int x) {
        g.drawImage(Drawable.sprites.get(spriteName).get(spriteCurrent), x, y, View.tileWidth, View.tileHeight, null);
    }

//getery i settery

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public boolean isPickedUp() {
        return isPickedUp;
    }

    public void setPickedUp(boolean pickedUp) {
        isPickedUp = pickedUp;
    }

    public String getSpriteName() {
        return spriteName;
    }

    public void setSpriteName(String spriteName) {
        this.spriteName = spriteName;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
