import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Menu {
    static java.util.List<Ranking> ranking = new ArrayList<>();
    static int frameSize = 800;
    static JFrame menu = new JFrame("Menu");
    static JFrame frame;
    static JLabel livess;
    static JLabel points;
    static JPanel info;
    static JLabel timeLabel;

    static void menu(){

        JPanel jp = new JPanel(new GridLayout(3, 1));

        JButton textNewGame = new JButton("New game");
        textNewGame.addActionListener(e -> {
            menu.setVisible(false);
            getSize();
        });
        textNewGame.setBackground(Color.black);
        textNewGame.setForeground(Color.orange);

        JButton textHighScores = new JButton("High Scores");
        textHighScores.addActionListener(e -> highScores());
        textHighScores.setBackground(Color.black);
        textHighScores.setForeground(Color.orange);

        JButton textExit = new JButton("Exit");
        textExit.addActionListener(e -> System.exit(0));
        textExit.setBackground(Color.black);
        textExit.setForeground(Color.orange);
        jp.add(textNewGame);
        jp.add(textHighScores);
        jp.add(textExit);
        menu.add(jp);
        menu.pack();
        menu.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menu.setVisible(true);
        menu.setLocationRelativeTo(null);
        menu.setSize(300,300);
        loadRanking();
    }
    public static void saveRanking() {
        try (PrintWriter writer = new PrintWriter("ranking.txt")) {
            for (Ranking r : ranking) {
                writer.println(r.playerName() + ": " + r.score());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadRanking() {
        List<Ranking> loadedRanking = new ArrayList<>();
        File rankingFile = new File("ranking.txt");
        try {
            if (!rankingFile.exists()) {
                rankingFile.createNewFile();
            }
            Scanner scanner = new Scanner(rankingFile);
            while (scanner.hasNextLine()) {
                String[] linia = scanner.nextLine().split(": ");
                String nazwaGracza = linia[0];
                int wynik = Integer.parseInt(linia[1]);
                loadedRanking.add(new Ranking(nazwaGracza, wynik));
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Wystąpił błąd podczas wczytywania pliku rankingowego: " + e.getMessage());
            e.printStackTrace();
        }
        ranking = loadedRanking;
    }

    static void highScores() {
        loadRanking();
        Collections.sort(ranking);
        JTextArea textArea = new JTextArea(20, 30);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);

        StringBuilder rankingText = new StringBuilder();
        rankingText.append("Ranking:\n");

        for (Ranking r : ranking) {
            rankingText.append(r.playerName()).append(": ").append(r.score()).append("\n");
        }

        textArea.setText(rankingText.toString());

        JOptionPane.showMessageDialog(null, scrollPane, "Ranking", JOptionPane.PLAIN_MESSAGE);
    }

    static void newGame(int size) throws IOException {
        frame = new JFrame("Game");
        frame.setLayout(new BorderLayout());
        info = new JPanel(new GridLayout(1,3));
        Model model = new Model(size);
        Controller c = model.controller;
        View v = model.view;
        points = new JLabel("Wynik tego poziomu: " + Model.score);
        info.add(points);
        JPanel livesP = new JPanel(new GridLayout(1,3));
        livesP.add(new JLabel(new ImageIcon("res/Heart/(1).png")));
        livess = new JLabel(String.valueOf(Model.lifes));
        livesP.add(livess);
        info.add(livesP);
        timeLabel = new JLabel("czas");
        info.add(timeLabel);
        frame.add(info, BorderLayout.NORTH);
        frame.add(v, BorderLayout.CENTER);
        frame.addKeyListener(c);
        frame.pack();
        frame.setSize(frameSize, frameSize);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    static void getSize(){
        JFrame size = new JFrame("Size");
        size.setLayout(new GridLayout(3,1));
        SpinnerNumberModel model = new SpinnerNumberModel(50, 10, 100, 1);
        JSpinner spinner = new JSpinner(model);
        JButton button = new JButton("OK");
        button.addActionListener(e -> {
            try {
                newGame((Integer) spinner.getValue());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            size.dispose();
        });
        size.add(new JLabel("Podaj rozmiar planszy"));
        size.add(spinner);
        size.add(button);
        size.setVisible(true);
        size.pack();
        size.setLocationRelativeTo(null);
    }

    static void endGame(){
        menu.setVisible(true);
        frame.dispose();
        saveRanking();
    }
}
