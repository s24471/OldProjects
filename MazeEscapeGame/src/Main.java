import Game.Controler;
import Game.Drawable;
import Game.Model;
import Game.View;
import Maze.MazeGenerator;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Drawable.initSprites();

        int width = 20;     //im większe rozmiary, tym większy i cięższy labirynt
        int height = 20;
        int zombiesQuantity = 10;
        int zombieIncreaseRate = 15 * 1000;  //czas w [s] * 1000, żeby było w [ms]

        MazeGenerator maze;
        do {
            maze = new MazeGenerator(height, width);
        }
        while (!maze.isGood());
        maze.setFinish();

        JFrame frame = new JFrame("game");
        View view = new View(maze.getBoard());
        frame.add(view);
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Model model = new Model(height, width, maze.getBoard(), view);

        Controler controler = new Controler(model);
        frame.addKeyListener(controler);

        frame.setVisible(true);
        view.thread.start();

        model.initZombie(zombiesQuantity, zombieIncreaseRate);
    }
}
