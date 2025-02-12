package Entities;

import Game.Model;
import Items.Item;
import java.util.ArrayList;

import static Util.Util.*;
import static java.awt.event.KeyEvent.*;

public class Player extends Entity {
    private static final int brightness = 5;         //odległość na jaką świeci latarka
    private int currentDirection;
    private boolean bendForItem;

    public Player(int y, int x, Model model) {      //inicjalizacja gracza
        super(y, x, model);

        setEq(new ArrayList<>());
        bendForItem = false;

        setSpriteName("Player");
        setInterval(200);
    }

    @Override
    public void action() {
        super.action();
        if(bendForItem && getModel().getBoard().getTile(getY(),getX()).getItem() != null)
            pickUpItem(getModel().getBoard().getTile(getY(),getX()).getItem());
        switch (currentDirection) {
            case VK_W -> tryMove(getY()-1, getX());  //w
            case VK_S -> tryMove(getY()+1, getX());  //s
            case VK_A -> tryMove(getY(), getX()-1);  //a
            case VK_D -> tryMove(getY(), getX()+1);  //d
        }
    }

    @Override
    public void tryMove(int y, int x) {             //ruch
        super.tryMove(y, x);
        latarka();                                  //uruchomienie latarki
    }

    public void latarka() {
        for (int y = 0; y < getModel().getBoard().getHeight(); y++) {             //mgła wojny wchędzie
            for (int x = 0; x < getModel().getBoard().getWidth(); x++) {
                getModel().getBoard().getTile(y, x).setVisible(false);
            }
        }
        getModel().getBoard().getTile(getY(), getX()).setVisible(true);                    //pokazanie pola, na którym stoi gracz

        switch (getDirection()) {                                        //oświetlanie pól przed graczem
            case (RIGHT) -> beamOfLight(-1, 0);
            case (LEFT) -> beamOfLight(1, 0);
            case (UP) -> beamOfLight(0, 1);
            case (DOWN) -> beamOfLight(0, -1);
        }


        for (int i = -1; i <= 1; i++) {                              //pokazywanie pól dookoła gracza
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0)
                    continue;
                if(isValidX(getX()+j, getModel().getBoard()) && isValidY(getY()+i, getModel().getBoard()))
                    getModel().getBoard().getTile(getY()+i,getX()+j).setVisible(true);
            }
        }
    }

    public void beamOfLight(int a, int b) {                         //oświetlanie pól przed graczem
        int y;
        int x;
        for (int i = 1; i <= brightness; i++) {
            x = this.getX() + i * -a;
            y = this.getY() + i * -b;

            if(isValidX(x, getModel().getBoard()) && isValidY(y, getModel().getBoard())) {            //oświetlanie pól dokładnie na wprost przed graczem
                getModel().getBoard().getTile(y,x).setVisible(true);
                if(!getModel().getBoard().getTile(y, x).isSeeTrought())
                    return;
            }
            if(isValidX(x+b, getModel().getBoard()) && isValidY(y+a, getModel().getBoard()))    //oświetlanie pola obok gracza
                getModel().getBoard().getTile(y+a,x+b).setVisible(true);
            if(isValidX(x-b, getModel().getBoard()) && isValidY(y-a, getModel().getBoard()))    //oświetlanie pola obok gracza
                getModel().getBoard().getTile(y-a,x-b).setVisible(true);
        }
    }

    public void pickUpItem(Item item){
        getModel().getBoard().getTile(getY(), getX()).setItem(null);
        getEq().add(item);
    }


    //gettery i settery

    public int getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(int currentDirection) {
        this.currentDirection = currentDirection;
    }

    public void setBendForItem(boolean bendForItem) {
        this.bendForItem = bendForItem;
    }

}
