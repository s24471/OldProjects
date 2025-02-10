import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Ghost extends Entity {
    static int COUNT = 0;
    static boolean confused = false;

    public Ghost(int row, int column, Model model) {
        super(row, column, model);
        COUNT++;
        maxSprite = 2;
        Random r = new Random();
        intervalMs = 400 + (r.nextInt(3) == 0 ? -1 : 1) * r.nextInt(40);
        spriteNum = COUNT % 3 + 1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                power();
            }
        }).start();
    }

    void power() {
        Util.sleepMs(5000);
        if (isAlive) {
            Random r = new Random();
            if (r.nextDouble() <= 0.25) {
                PowerUp powerUp = PowerUp.getRandPower(model.arr.getValueAt(y, x), model);
                new Thread(powerUp).start();
                model.arr.getValueAt(y, x).powerUps.add(powerUp);
            }
            power();
        }
    }

    @Override
    public void move() {
        Point p;
        if (confused) {
            ArrayList<Point> points = new ArrayList<>();
            if (isValid(y + 1, x)) points.add(new Point(x, y + 1));
            if (isValid(y - 1, x)) points.add(new Point(x, y - 1));
            if (isValid(y, x + 1)) points.add(new Point(x + 1, y));
            if (isValid(y, x - 1)) points.add(new Point(x - 1, y));
            Random r = new Random();
            p = points.get(r.nextInt(points.size()));
        } else {
            Pathfinding pathfinding = new Pathfinding(model.arr.arr);
            p = pathfinding.findPath(new Point(x, y), new Point(model.player.x, model.player.y)).get(1);
        }
        Entity e = model.arr.getValueAt(p.y, p.x).entity;
        if (e != null) {
            if (e instanceof Ghost) {
                return;
            }
            if (e instanceof Player) {
                model.playerKilled();
                return;
            }
        }
        model.arr.move(x, y, p.x, p.y);
        x = p.x;
        y = p.y;

    }
}
