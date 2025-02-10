import java.awt.*;
import java.util.Random;

public abstract class PowerUp implements Drawable, Runnable {
    int timeLeft;
    int currSprite;
    int maxSprite;
    int spriteNum;
    boolean isAlive;
    Tile tile;
    Model model;

    public PowerUp(Model m, Tile t) {
        this.model = m;
        this.tile = t;
        isAlive = true;
        timeLeft = 100;
        currSprite = 0;
        maxSprite = 1;
        spriteNum = 5;
    }

    @Override
    public void run() {
        while (timeLeft > 0 && isAlive) {
            Util.sleepMs(1000);
            timeLeft--;
        }
        isAlive = false;
    }

    public void use() {
        kill();
    }

    public void kill(){
        isAlive = false;
        tile.powerUps.remove(this);
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(sprites[spriteNum].get(currSprite%maxSprite), x, y, View.tileWidth, View.tileHeight, null);
    }

    public static PowerUp getRandPower(Tile t, Model m){
        Random r = new Random();
        switch (r.nextInt(5)){
            case 0:
                return new HeartPowerUp(m, t);
            case 1:
                return new ConfusePowerUp(m, t);
            case 2:
                return new FramePowerUp(m, t);
            case 3:
                return new SpeedPowerUp(m,t);
            case 4:
                return new DoublePowerUp(m,t);
        }
        return null;
    }

}
