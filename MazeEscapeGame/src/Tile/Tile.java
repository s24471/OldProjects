package Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import Entities.*;
import Items.*;
import Game.Drawable;
import Game.View;

import static Util.Util.mySleep;

public abstract class Tile implements Drawable, Runnable {
    private int x;
    private int y;
    private String spriteName;
    private String spriteRarity;
    private int spriteCurrent;
    private Entity entity;
    private Item item;
    private boolean isVisible;
    private boolean isWakable;
    private boolean seeTrought;

    public Tile(int y, int x) {
        spriteCurrent = 0;          //pole na początku jest puste
        isWakable = false;          //nie możliwe do chodzenia po (jak ściana lub puste)
        seeTrought = false;         //nie można przez nie patrzeć (jak ściana lub puste)
        this.x = x;
        this.y = y;
        spriteRarity = "";
    }

    public void tileWalkedOn(Entity entity){
        isWakable = false;
        this.entity = entity;
    }
    public void tileWalkedOff(){
        isWakable = true;
        entity = null;
    }

    @Override
    public void run() {
        Random r = new Random();
        while (true){
            action();

            mySleep(500+(100-r.nextInt(200)));
        }

    }

    public void action(){

    }

    @Override
    public void draw(Graphics g, int y, int x) {

        if(!isVisible) {            //jeżeli jest nie widoczne, to rysowanie mgły wojnyw
            g.drawImage(Drawable.sprites.get("FogOfWar").get(0), x, y, View.tileWidth, View.tileHeight, null);
            return;
        }

                                //jeżeli jest widoczne to rysowanie danego pola
        g.drawImage(Drawable.sprites.get(spriteName+spriteRarity).get(spriteCurrent), x, y, View.tileWidth, View.tileHeight, null);

        if(item != null)
            item.draw(g,y,x);
        if (entity != null)
            entity.draw(g, y, x);
    }

    public void calculateRarity(){
        ArrayList<Integer> rarities = Drawable.getRarity(spriteName);

        int rarity = rarities.get(new Random().nextInt(rarities.size()));
        spriteRarity = String.valueOf(rarity);
        Random r = new Random();
        spriteCurrent = r.nextInt(Drawable.sprites.get(spriteName+spriteRarity).size());
    }

    public void animate(){
        spriteCurrent++;
        spriteCurrent %= Drawable.sprites.get(spriteName+spriteRarity).size();
    }




    //settery i gettery

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getSpriteName() {
        return spriteName;
    }

    public void setSpriteName(String spriteName) {
        this.spriteName = spriteName;
    }

    public int getSpriteCurrent() {
        return spriteCurrent;
    }

    public void setSpriteCurrent(int spriteCurrent) {
        this.spriteCurrent = spriteCurrent;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isWakable() {
        return isWakable;
    }

    public void setWakable(boolean wakable) {
        isWakable = wakable;
    }

    public boolean isSeeTrought() {
        return seeTrought;
    }

    public void setSeeTrought(boolean seeTrought) {
        this.seeTrought = seeTrought;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
