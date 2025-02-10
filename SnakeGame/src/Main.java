package Project_4;

import javax.swing.*;

public class Main extends JFrame{
    public final static int ROW = 26;
    public final static int COL = 15;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    public Main(){
        Logic logika = new Logic();
        Visiual visiual = new Visiual();
        Sticker Gra = new Sticker(logika,visiual);
    }
}
