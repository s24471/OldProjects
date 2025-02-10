import java.awt.*;

public abstract class Entity implements Runnable, Drawable{
    int direction;
    int x;
    int y;
    boolean isAlive;
    int intervalMs;
    int spriteNum;
    int currSprite;
    int maxSprite;
    Model model;

    public Entity(int y, int x, Model model) {
        this.model = model;
        this.x = x;
        this.y = y;
        isAlive = true;
        intervalMs = 1;
        currSprite = 0;
        maxSprite = 1;
    }

    @Override
    public void run() {
        while(isAlive) {
            move();

            Util.sleepMs(intervalMs);
        }
    }

    public void kill(){
        isAlive = false;
    }

    public void move(){}

    public static boolean collision(Entity e1, Entity e2){
        return e1.x == e2.x && e1.y == e2.y;
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(sprites[spriteNum].get(currSprite%maxSprite + maxSprite*direction), x, y, View.tileWidth, View.tileHeight, null);

    }
    public boolean isValid(int y2, int x2) {
        return !(y2 < 0 || y2 >= model.size || x2 < 0 || x2 >= model.size || model.arr.getValueAt(y2, x2).type != 0);
    }
}
