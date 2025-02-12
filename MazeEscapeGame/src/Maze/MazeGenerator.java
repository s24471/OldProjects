package Maze;

import java.util.ArrayList;
import java.util.Random;
import Game.Board;
import Tile.*;
import Util.MyPoint;

public class MazeGenerator {
    private final static double VALIDATION_PERCENTAGE = (double)3 / 7;
    private PathMaker pathMaker;
    private ArrayList<MyPoint> pointsToStartPath;
    private Board board;

    public MazeGenerator(int height, int width) {        //tworzenie pustej planszy o danych wymiarach
        pointsToStartPath = new ArrayList<>();
        board = new Board(height, width);
        pathMaking(new MyPoint(width / 2, height / 2));     //puntk startowy generowania labiryntu na środku planszzy
    }

    private void pathMaking(MyPoint p) {           //tworzenie ścieżek dopuki są punkty początkowe
        do {
            pathMaker = new PathMaker(p, board);
            newPath(p);
            p = filterStartPoints();                    //ustawienie nowego punktu początkowego ścieżki
        } while (p != null);
    }

    private void newPath(MyPoint p) {              //tworzenie nowych scieżek
        do {
            board.change(p.y, p.x, new TilePath(p.y, p.x));   //zamiana pustego pola na ściezkę
            p = pathMaker.makePath(p.y, p.x);           //tworzenie kolejnych punktów ścieżki
        } while (p != null);
    }

    private MyPoint filterStartPoints() {          //aktualizacja listy punktów początkowych kolejnych ścieżek
        ArrayList<MyPoint> tmp = new ArrayList<>(pointsToStartPath);     //tymczasowa stara lista możliwych punktów początkowych
        pointsToStartPath = new ArrayList<>();          //wyczyszczenie straj listy
        Random r = new Random();
        for (MyPoint p : pathMaker.getPath()) {               //dodanie nowych punktów starowwych
            if (isValid(p))
                pointsToStartPath.add(p);
        }
        for (MyPoint p : tmp) {                          //zaktualizowanie starych punktów startowych i dodanie ich do nowej listy
            if (isValid(p))
                pointsToStartPath.add(p);
        }
        if (pointsToStartPath.size() == 0)               //brak kolejnych punktów startowych -> koniec tworzenia labiryntu
            return null;
        return pointsToStartPath.get(r.nextInt(pointsToStartPath.size()));     //losowanie nowego punktu startowego w nowej listy
    }

    private boolean isValid(MyPoint p) {           //sprawdzienie, który z punktów starej ścieżki, może być punktem początkowym nowej ścieżki
        return (PathMaker.isValid(p.y, p.x + 1, board)) ||
                (PathMaker.isValid(p.y, p.x - 1, board)) ||
                (PathMaker.isValid(p.y + 1, p.x, board)) ||
                (PathMaker.isValid(p.y - 1, p.x, board));          //można kiedyś zmienić, żeby korzystało z Util
    }

    public void setFinish() {                //dodać coś, jeżeli path nie dojdzie do granicy mapy.
        ArrayList<Tile> tmp = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < board.getHeight(); i++) {
            if (board.getTile(i, 1).isWakable())
                tmp.add(board.getTile(i, 0));
            if (board.getTile(i, board.getWidth() - 2).isWakable())
                tmp.add(board.getTile(i, board.getWidth() - 1));
        }
        for (int i = 0; i < board.getWidth(); i++) {
            if (board.getTile(1, i).isWakable())
                tmp.add(board.getTile(0, i));
            if (board.getTile(i, board.getHeight() - 2).isWakable())
                tmp.add(board.getTile(i, board.getHeight() - 1));
        }
        Tile finishTile = tmp.get(r.nextInt(tmp.size()));
        board.change(finishTile.getY(), finishTile.getX(), new TileDoor(finishTile.getY(), finishTile.getX()));
    }

    public boolean isGood() {
        return (double) board.getWakableTiles().size() / (board.getHeight() * board.getWidth()) > VALIDATION_PERCENTAGE;
    }


    //gettery i settery

    public Board getBoard() {
        return board;
    }
}
