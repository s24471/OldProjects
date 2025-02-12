package Util;

import Game.Board;

import static java.lang.Thread.sleep;

abstract public class Util {
    public static final int RIGHT = 1;      //nie zmieniać!!!!
    public static final int LEFT = -1;      //nie zmieniać!!!!
    public static final int UP = 2;         //nie zmieniać!!!!
    public static final int DOWN = -2;      //nie zmieniać!!!!
    public final static int[] DIRECTIOS = {RIGHT, LEFT, UP, DOWN};

    public static void mySleep(long ms){
        try {
            sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isValidX(int x, Board board){
        return x>=0 && x < board.getWidth();
    }

    public static boolean isValidY(int y, Board board){
        return y >= 0 && y < board.getHeight();
    }

}
