import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public interface Drawable {


    // 0 - Player, {1..3} ghosts, 4 - empty tile, 5 - point, 6 - heart, 7- frame, 8- speed, 9 - confuse
    ArrayList<BufferedImage>[] sprites = new ArrayList[11];

    static void init() {
        int tmp = 0;
        if (sprites[tmp] == null) sprites[tmp++] = load("res/Player/");
        if (sprites[tmp] == null) sprites[tmp++] = load("res/Ghost1/");
        if (sprites[tmp] == null) sprites[tmp++] = load("res/Ghost2/");
        if (sprites[tmp] == null) sprites[tmp++] = load("res/Ghost3/");
        if (sprites[tmp] == null) sprites[tmp++] = load("res/EmptyTile/");
        if (sprites[tmp] == null) sprites[tmp++] = load("res/Point/");
        if (sprites[tmp] == null) sprites[tmp++] = load("res/Heart/");
        if (sprites[tmp] == null) sprites[tmp++] = load("res/Frame/");
        if (sprites[tmp] == null) sprites[tmp++] = load("res/Speed/");
        if (sprites[tmp] == null) sprites[tmp++] = load("res/Confuse/");
        if (sprites[tmp] == null) sprites[tmp++] = load("res/Double/");
    }

    static ArrayList<BufferedImage> load(String path) {
        ArrayList<BufferedImage> images = new ArrayList<>();

        File folder = new File(path);
        File[] files = folder.listFiles();
        System.out.println(path);
        if (files != null) {
            // Sortuj pliki po nazwie w kolejności alfabetycznej
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if(o1.getName().length() == o2.getName().length())return o1.getName().compareTo(o2.getName());
                    return o1.getName().length() - o2.getName().length();
                }
            });

            for (File file : files) {
                if (file.isFile()) {
                    try {
                        BufferedImage image = ImageIO.read(file);
                        System.out.println(file.getAbsolutePath());
                        images.add(image);
                    } catch (IOException e) {
                        System.out.println("Error loading image: " + file.getName());
                    }
                }
            }
        }
        return images;
    }


    void draw(Graphics g, int x, int y);
}
