import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Model implements Runnable {
    final static int FPS = 60;
    static boolean stop;
    static int lifes;
    static int score;
    int size;

    static boolean running;
    JTable arr;
    Controller controller;
    View view;
    Player player;
    ArrayList<Ghost> ghosts;


    public Model(int size) {
        score = 0;
        lifes = 3;
        stop = true;
        this.size = size;
        ghosts = new ArrayList<>();
        Drawable.init();
        arr = new JTable(size, size);
        this.view = new View(size, size, this);
        newMap(size);
        this.controller = new Controller(this, player);
        start();
        new Thread(this).start();
    }

    public void start() {
        new Thread(player).start();
        for (Ghost g : ghosts) new Thread(g).start();
        stop = false;
    }

    public void newMap(int size) {
        int[][] map = MapGenerator.generateMap(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Tile t;
                if (map[i][j] == 1) t = new Tile(map[i][j]);
                else {
                    t = new Tile(0);
                    if (map[i][j] == 2) {
                        this.player = new Player(i, j, this);
                        t.setEntity(player);
                    } else if (map[i][j] == 3) {
                        Ghost g = new Ghost(i, j, this);
                        ghosts.add(g);
                        t.setEntity(g);
                    }
                    if (map[i][j] != 2) t.powerUps.add(new SmallPoint(this, t));
                }
                arr.setValueAt(t, i, j);
            }
        }
    }

    @Override
    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int s =0;
                int m =0;
                int h =0;
                while(running){
                    Util.sleepMs(1000);
                    s++;
                    if(s==60){
                        s=0;
                        m++;
                        if(m==60){
                            m=0;
                            h++;
                        }
                    }
                    Menu.timeLabel.setText(h + ":" + m + ":" + s);
                }
            }
        }).start();
        double targetTime = (double) 1000 / FPS;
        int curr = 0;
        int max = 10;
        running = true;
        while (running) {
            long startTime = System.currentTimeMillis();

            view.repaint();
            curr++;
            if (curr >= max) {
                player.currSprite++;
                for (Ghost g : ghosts) g.currSprite++;
                for (Tile[] tt : arr.arr)
                    for (Tile t : tt)
                        t.currSprite++;
                curr = 0;
            }
            long sleepTime = (long) (targetTime - (System.currentTimeMillis() - startTime));
            if (sleepTime > 0) {
                Util.sleepMs(sleepTime);
            }
        }
    }

    public void playerKilled() {
        stop = true;

        lifes--;

        Menu.livess.setText(String.valueOf(lifes));
        view.repaint();
        if (lifes > 0) {
            for (Ghost g : ghosts) {
                g.kill();
            }
            player.kill();
            ghosts = new ArrayList<>();
            score += player.score;
            arr.kill();
            newMap(size);
            start();
            controller.player = player;
        } else {
            System.out.println("Zabito cie");
            running = false;
            saveScore();


        }
    }

    void saveScore() {
        JFrame name = new JFrame("name");
        name.setLayout(new GridLayout(3,1));
        JTextField jtf = new JTextField();
        JButton save = new JButton("Zapisz");
        save.addActionListener(e -> {
            Menu.ranking.add(new Ranking(jtf.getText(), score));
            name.dispose();
            Menu.endGame();
        });
        name.add(new JLabel("Podaj swoje imie"));
        name.add(jtf);
        name.add(save);
        name.setVisible(true);
        name.pack();
        name.setLocationRelativeTo(null);
    }
}
