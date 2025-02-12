package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import static Util.Util.*;
import static java.lang.System.currentTimeMillis;

public class View extends JPanel implements Runnable{
    public static final int FPS = 30;
    public static int tileWidth;
    public static int tileHeight;
    private Board board;
    public Thread thread;

    public View(Board board) {
        this.board = board;
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustTileSize();
            }
        });
        this.thread = new Thread(this);
        mySleep(100);
    }

    @Override
    public void paint(Graphics g) {
        repaint(g);
    }

    public void repaint(Graphics g){
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                board.getTile(i, j).draw(g, i*tileHeight+20, j*tileWidth+20);    //wypełnienie planszy danymi polami
            }
        }
    }

    private void adjustTileSize() {              //dopasowanie rozmiaru pola
        int panelWidth = getWidth()-40;
        int panelHeight = getHeight()-40;

        tileHeight = panelHeight / (board.getHeight());
        tileWidth = panelWidth / (board.getWidth());

        repaint();
    }

    @Override
    public void run() {                     //odświerzanie obrazuaaaaaaaaaaaaaa z daną częstotliwością
        long interval = 1000/FPS;
        while (true) {
            long time1 = currentTimeMillis();
            repaint();
            long time2 = currentTimeMillis()-time1;
            if(interval-time2>0)
                mySleep(interval-time2);
        }
    }

}