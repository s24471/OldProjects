package Entities;

import Game.Model;
import Tile.TileDoor;
import java.util.ArrayList;
import java.util.Random;

import static Util.Util.*;

public class Zombie extends Entity {
    private final double CHANGE_DIRECTION_CHANCE = 0.025;
    private int stepCounter;
    private int currentDirection;

    public Zombie(int y, int x, Model model) {
        super(y, x, model);
        stepCounter = 0;
        currentDirection = UP;
        setSpriteName("Zombie");
        setInterval(1000);
    }

    @Override
    public void action() {
        super.action();

        updateDirection();
        switch (currentDirection) {
            case RIGHT -> tryMove(getY(), getX() + 1);
            case LEFT -> tryMove(getY(), getX() - 1);
            case UP -> tryMove(getY() - 1, getX());
            case DOWN -> tryMove(getY() + 1, getX());
        }
        stepCounter++;
    }

    @Override
    public void tryMove(int y, int x) {
        if (getModel().getBoard().getTile(y, x).getEntity() instanceof Player) {
            eatPlayer();
        }

        if (!isValidToMove(y, x)) {
            newDirecton();
            switch (currentDirection) {
                case UP -> {
                    y = getModel().getBoard().getTile(getY(), getX()).getY() - 1;
                    x = getX();
                }
                case DOWN -> {
                    y = getModel().getBoard().getTile(getY(), getX()).getY() + 1;
                    x = getX();
                }
                case RIGHT -> {
                    y = getY();
                    x = getModel().getBoard().getTile(getY(), getX()).getX() + 1;
                }
                case LEFT -> {
                    y = getY();
                    x = getModel().getBoard().getTile(getY(), getX()).getX() - 1;
                }
            }
        }
        super.tryMove(y, x);
    }


    @Override
    public boolean isValidToMove(int y, int x) {
        if (getModel().getBoard().getTile(y, x).getEntity() instanceof Player)
            return true;
        return super.isValidToMove(y, x) && !(getModel().getBoard().getTile(y, x) instanceof TileDoor);

    }

    public void updateDirection() {
        for (int i = -1; i <= 1; i++) {
            if (i == 0)
                continue;
            if (getModel().getBoard().getTile(getY() + i, getX()).getEntity() instanceof Player) {
                currentDirection = - 2 * i;           //jeżeli player na polu obok zombie, to kierunek w jego stronę
                return;
            }
        }
        for (int i = -1; i <= 1; i++) {
            if (i == 0)
                continue;
            if (getModel().getBoard().getTile(getY(), getX()+i).getEntity() instanceof Player) {
                currentDirection = i;           //jeżeli player na polu obok zombie, to kierunek w jego stronę
                return;
            }
        }
        changeDirection();                                  //jeżeli nie, to po staremu
    }

    public void changeDirection() {
        Random r = new Random();
        double d = r.nextDouble();
        if (d > 1 - CHANGE_DIRECTION_CHANCE * stepCounter) {
            stepCounter = 0;
            newDirecton();
        }
    }

    public void newDirecton() {
        Random r = new Random();
        ArrayList<Integer> posibleDir = new ArrayList<>();

        if (isValidToMove(getY(), getX() + 1))
            posibleDir.add(RIGHT);
        if (isValidToMove(getY(), getX() - 1))
            posibleDir.add(LEFT);
        if (isValidToMove(getY() - 1, getX()))
            posibleDir.add(UP);
        if (isValidToMove(getY() + 1, getX()))
            posibleDir.add(DOWN);

        if (posibleDir.size() == 0)
            return;

        if (posibleDir.size() > 1) {
            posibleDir.remove((Object) (currentDirection * -1));
        }
        currentDirection = posibleDir.get(r.nextInt(posibleDir.size()));
    }

    public void eatPlayer() {
        System.out.println("Zostałeś pożarty!!");
        System.exit(0);
    }
}
