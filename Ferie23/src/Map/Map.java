package Map;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Map extends JPanel {
    public static final int DEF_SIZE = 64;
    public static int SIZE = DEF_SIZE;
    public static final int DEF_WIDTH_SCREEN = 30;
    public static int WIDTH_SCREEN = DEF_WIDTH_SCREEN;
    public static final int DEF_HEIGHT_SCREEN = 15;
    public static int HEIGHT_SCREEN = DEF_HEIGHT_SCREEN;
    public static final int WIDTH_MAP = 50;
    public static final int HEIGHT_MAP = 30;
    public static int WIDTH_WORLD = SIZE*WIDTH_MAP;
    public static int HEIGHT_WORLD = SIZE*HEIGHT_MAP;
    public static final int GAP = 8;
    public static Tile[][] MAP;
    Brain brain;
    public static Player player;

    public Map(Brain brain, Player player){
        this.brain = brain;
        this.player = player;
        this.setPreferredSize(new Dimension(WIDTH_SCREEN * SIZE, HEIGHT_SCREEN * SIZE));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.setDoubleBuffered(true);
        this.addKeyListener(Player.playerKeyListener);
        this.addMouseListener(Player.playerMouseListener);
        this.addMouseWheelListener(Player.playerMouseListener);
        MAP = new Tile[HEIGHT_MAP][WIDTH_MAP];
        try {
            Scanner scanner = new Scanner(new FileReader("src/Map/mapa.txt"));
            for (int i = 0; i < HEIGHT_MAP; i++) {
                String tmp = scanner.nextLine();
                for (int j = 0; j < WIDTH_MAP; j++) {
                    MAP[i][j] = Tile.NEW(i, j, tmp.charAt(j));
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D)g).setStroke(new BasicStroke(5));
        g.setColor(Color.RED);
        //for(Entity e: brain.entities)g.fillRect(e.x, e.y, SIZE, SIZE);

        for (int i = 0; i < HEIGHT_MAP; i++) {
            for (int j = 0; j < WIDTH_MAP; j++) {
                MAP[i][j].draw((Graphics2D) g);
            }
        }
        player.draw((Graphics2D) g);
        for(Entity entity: brain.entities){
            entity.draw((Graphics2D) g);
        }
        for(Visual visual: Brain.visuals){
            visual.draw((Graphics2D) g);
        }

        for(int i=0; i<brain.player.powers.size(); i++){
            g.drawImage(brain.player.powers.get(i).getIcon(), i*(DEF_SIZE + DEF_SIZE /3+GAP),DEF_HEIGHT_SCREEN *(DEF_SIZE-1), DEF_SIZE, DEF_SIZE, null);
            g.setColor(Color.white);
            float progress = brain.player.powers.get(i).getProgress();
            g.fillRect(i*(DEF_SIZE+DEF_SIZE/3+GAP)+DEF_SIZE, DEF_HEIGHT_SCREEN *(DEF_SIZE-1)+DEF_SIZE - (int)(DEF_SIZE*progress) , DEF_SIZE/3, (int)(DEF_SIZE*progress));
            if(brain.player.selected == i){
                g.setColor(Color.BLUE);
                g.drawRect(i*(DEF_SIZE+DEF_SIZE/3+GAP), DEF_HEIGHT_SCREEN *(DEF_SIZE-1), DEF_SIZE, DEF_SIZE);
            }
        }
    }
    public static void zoomInOut(int n){
        int tmpx = SIZE*WIDTH_MAP;
        int tmpy = SIZE*HEIGHT_MAP;
        SIZE += n;
        int tmpx2 = SIZE*WIDTH_MAP;
        int tmpy2 = SIZE*HEIGHT_MAP;
        double multix = (double)tmpx2/tmpx;
        double multiy = (double)tmpy2/tmpy;
        player.x*=multix;
        player.y*=multiy;
        player.speedx = (double)tmpx2/player.defspeedx;
        player.speedy = (double)tmpy2/player.defspeedy;
    }
}
