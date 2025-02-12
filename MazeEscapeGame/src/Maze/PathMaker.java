package Maze;

import java.util.ArrayList;
import java.util.Random;
import Game.Board;
import Tile.*;
import Util.MyPoint;

import static Util.Util.*;

public class PathMaker {
    public static final double WALL_MISSING_CHANCE = 0.3;   //szansa na ilość dziur w ścianach danej ścieżki, im większe tym mniej dziór
    private final double CHANGE_DIRECTION_CHANCE = 0.25;     //1-x -> zmiana szansy na wybór nowego kierunku
    private ArrayList<MyPoint> path;                                //pozycje pól stworzonej ścieżki
    private Random r;
    private int currentDirection;                                   //obecny kierunek tworzenia ścieżki
    private int stepCounter;                                        //licznik długości ścieżki w danym kierunku
    private Board board;

    public PathMaker(MyPoint p, Board board){
        this.board = board;
        path = new ArrayList<>();
        path.add(p);
        r = new Random();
        firstDirection(p.y, p.x);                           //wybór początkowego kierunku;
    }

    public void firstDirection(int y, int x){               //określa kierunek początkowy nowej ściezki
        ArrayList<Integer> options = new ArrayList<>();     //lista zawierająca kierunki możliwe do wyboru
        for (int i : DIRECTIOS){
            currentDirection = i;                           //ustawienie przykładowego kierunku
            MyPoint p = nextStep(y, x);
            if(isValid(p.y, p.x, board))                           //sprawdzenie, czy kierunek jest ok
                options.add(currentDirection);              //dodanie kierunku do listy możliwych kierunków
        }
        currentDirection = options.get(r.nextInt((options.size())));
    }

    public MyPoint nextStep(int y, int x){                  //określenie następnego kroku w obecnym kierunku
        switch (currentDirection){
            case RIGHT -> x++;
            case LEFT -> x--;
            case UP -> y++;
            case DOWN -> y--;
        }
        return new MyPoint(x, y);
    }

    public static boolean isValid(int y, int x, Board board){            //sprawdzenie, czy następne pole może zostać przydzielone
        return isValidX(x, board) &&                          //czy jest na planszy
                isValidY(y, board) &&                         //czy jest na planszy
                board.getTile(y, x) instanceof TileEmpty;    //czy jest puste
    }

    public MyPoint makePath(int y, int x){                  //tworzenie kolejnych punktów ścieżki
        MyPoint p = nextStep(y, x);
        if(!isValid(p.y, p.x, board)){                             //sprawdzenie, czy następne pole jest ok
            end();                                          //zakończenie ścieżki
            return null;
        }
        stepCounter++;
        updateDirection();                                  //wybór kierunku ścieżki
        MyPoint nextMyPoint = new MyPoint(p.x, p.y);        //ustawienie kolejnego pola
        path.add(nextMyPoint);                              //dodanie pola do ścieżki
        return nextMyPoint;
    }

    public void end(){                                      //koniec ścieżki
        for (MyPoint p: path)                               //iteracje po całej ścieżce
            makeWall(p);                                    //budowanie murów dookoła ścieżki
        MyPoint p = path.get(path.size()-1);                //postawienie ściany na końcu ścieżki
        board.change(p.y, p.x, new TileWall(p.y, p.x));
    }

    public void makeWall(MyPoint p){                        //America great again!!
        if(r.nextDouble() > WALL_MISSING_CHANCE)            //czy nie będzie ściany
            return;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(i == 0 && j == 0)                        //pominięcie punktu p+0, ponieważ tam jest ścieżka
                    continue;
                MyPoint breakPoint = new MyPoint(p.x+i, p.y+j);                           //ustawianie ścian, jeżeli są tam puste pola, dookoła ścieżki
                if(isValid(breakPoint.y, breakPoint.x, board))                                         //sprawdzenie, czy można postawić ścianę
                    board.change(breakPoint.y, breakPoint.x, new TileWall(breakPoint.y, breakPoint.x));       //zamiane pustego pola na ścianę
            }
        }
    }

    public void updateDirection(){                          //decyzja o zmianie kierunku
        if(r.nextDouble() > 1 - CHANGE_DIRECTION_CHANCE * stepCounter)          //zmniejszanie szansy na kontynuację ściezki w danym kierunku
            newDirecton();
    }

    public void newDirecton() {                             //wybór nowego kierunku
        int tmp = DIRECTIOS[r.nextInt(4)];            //losowanie nowego kierunku
        if(tmp * -1 == currentDirection)                    //zabezpieczenie przed tworzeniem ścieżki do tyłu
        {
            newDirecton();
            return;
        }
        stepCounter = 0;
        currentDirection = tmp;
    }

    //gettery i settery


    public ArrayList<MyPoint> getPath() {
        return path;
    }
}
