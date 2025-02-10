import java.awt.*;
import java.util.ArrayList;

public class Tile implements Drawable{
    Entity entity;
    int type;
    static int maxSprite = 3;
    int currSprite;
    ArrayList<PowerUp> powerUps;

    public Tile(int type) {
        this.type = type;
        entity = null;
        currSprite = 0;
        powerUps = new ArrayList<>();
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void removeEntity(){
        entity = null;
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        if(type==0){
            //g.setColor(Color.darkGray);
            System.out.println(sprites.length);
            System.out.println(sprites[4].size());
            System.out.println(currSprite);
            System.out.println(maxSprite);
            g.drawImage(sprites[4].get(currSprite%maxSprite), x, y, View.tileWidth, View.tileHeight, null);
        }
        else g.fillRect(x, y, View.tileWidth, View.tileHeight);
        ArrayList<PowerUp> power = new ArrayList<>(powerUps);
        for(PowerUp p : power){
            p.draw(g, x, y);
        }
        if(entity != null)entity.draw(g, x, y);
    }
    public void kill(){
        while(powerUps.size()>0)powerUps.get(0).kill();
    }
}
