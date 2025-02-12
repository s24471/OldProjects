package Tile;

import Entities.Entity;
import Entities.Player;
import Game.Model;

public class TileDoor extends Tile{
    public TileDoor(int y, int x) {
        super(y, x);
        setSpriteName("TileDoor");
        setWakable(true);
    }

    @Override
    public void tileWalkedOn(Entity entity) {
        super.tileWalkedOn(entity);
        winnig(entity.getModel());
    }


    public void winnig(Model model){
        if(getEntity() instanceof Player && getEntity().getEq().contains(model.getKey())){
            System.out.println("ez win");
            System.exit(0);
        }
    }


    @Override
    public void action() {
        super.action();
        animate();
    }



}
