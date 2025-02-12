package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface Drawable {
    HashMap<String, ArrayList<BufferedImage>> sprites = new HashMap<>();    //trzyma nazwę obrazka i obrazek
    static void initSprites(){
        File res = new File("res/sprites/");               //wczytanie folderu ze sprite'ami
        File[] files = res.listFiles();                             //tablicowanie elementów obecnego folderu
        if(files != null){
            for(File f : files){                                    //przejście po całym folderze
                loadSprites(f, f.getName());
            }
        }
    }

    static void loadSprites(File folder, String name){              //wczytywanie sprite'ów do hashmapy
        ArrayList<BufferedImage> tmp = new ArrayList<>();
        File[] files = folder.listFiles();                          //tablicowanie elementów obecnego folderu
        if(files != null) {
            for (File f : files) {                                  //przejście po całym folderze
                if(f.isDirectory())                                 //jezeli to folder, to rekurencja
                    loadSprites(f, name+f.getName());
                else if(f.isFile()){                                //jeżeli to plik, to zapis do tmp
                    try {
                        BufferedImage image = ImageIO.read(f);
                        System.out.println(f.getAbsolutePath());
                        tmp.add(image);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        if(tmp.size() > 0)                                          //jeżeli coś jest w tmp, to dodanie tmp do hashmapy sprite'ów
            sprites.put(name, tmp);
    }

    void draw(Graphics g, int y, int x);

    static ArrayList<Integer> getRarity(String tileName){
        ArrayList<Integer> rarities = new ArrayList<>();
        for (Map.Entry<String, ArrayList<BufferedImage>> a : sprites.entrySet()) {
            if(a.getKey().startsWith(tileName)){
                int rar = Integer.valueOf(a.getKey().substring(tileName.length()));
                for (int i = 0; i < rar; i++) {
                    rarities.add(rar);
                }
            }
        }
        return rarities;
    }
    public static void main(String[] args) {
        initSprites();
        for(Map.Entry<String, ArrayList<BufferedImage>> a : sprites.entrySet()){
            System.out.println(a.getKey()+" "+a.getValue().size());
        }
    }
}
