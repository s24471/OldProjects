import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class BoardFrame extends JFrame {
    private Board board;
    private Timer timer;
    private int speed = 100;
    private JProgressBar progressBar;
    private JButton restartButton;
    private JButton pauseResumeButton;
    int szer;
    int dl;
    int szerTile;
    int dlTile;

    public BoardFrame(int szer, int dl, Rules rules) {
        this.szer = szer;
        this.dl = dl;
        board = new Board(szer, dl, rules);
        initUI();
    }
    private void initUI() {
        setTitle("BoardFrame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int i = 0; i < board.szer; i++) {
                    for (int j = 0; j < board.dl; j++) {
                        Tile tile = board.tiles[i][j];
                        g.setColor(tile.alive ? Color.WHITE : Color.BLACK);
                        g.fillRect(i * szerTile, j * dlTile, szerTile, dlTile);
                    }
                }
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(board.szer * szer, board.dl * dl);
            }
        };

        boardPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                szerTile = boardPanel.getWidth()/szer;
                dlTile = boardPanel.getHeight()/dl;
                boardPanel.repaint();
            }
        });
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                    int x = e.getX() / szerTile;
                    int y = e.getY() / dlTile;
                    if(x>=0 && x< szer && y>=0 && y<dl)
                        board.tiles[x][y].alive = !board.tiles[x][y].alive;
                    boardPanel.repaint();
            }
        });

        progressBar = new JProgressBar(0, speed);
        restartButton = new JButton("Restart");
        pauseResumeButton = new JButton("Pause");
        JButton speedUp = new JButton("Speed up");
        JButton speedDown = new JButton("Speed down");
        pauseResumeButton = new JButton("Pause");
        speedUp.addActionListener(e -> {
            speed-=10;
            progressBar.setMaximum(speed);
        });
        speedDown.addActionListener(e -> {
            speed+=10;
            progressBar.setMaximum(speed);
        });
        restartButton.addActionListener(e -> {
            timer.stop();
            for (int i = 0; i < board.szer; i++) {
                for (int j = 0; j < board.dl; j++) {
                    board.tiles[i][j].alive = false;
                }
            }
            boardPanel.repaint();
            progressBar.setValue(0);
            pauseResumeButton.setText("Resume");
        });

        pauseResumeButton.addActionListener(e -> {
            if (timer.isRunning()) {
                pause();
            } else {
                resume();
            }
        });

        timer = new Timer(1, new ActionListener() {
            int counter = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                counter++;
                if(counter >=speed) {
                    counter = 0;
                    if (board.nextGen()) {
                        System.out.println("repaint");
                        boardPanel.repaint();
                    }
                }
                progressBar.setValue(counter);
            }

        });
        timer.start();

        JPanel controlPanel = new JPanel();
        controlPanel.add(speedUp);
        controlPanel.add(restartButton);
        controlPanel.add(pauseResumeButton);
        controlPanel.add(speedDown);

        add(progressBar, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
    }
    public void resume(){
        timer.start();
        pauseResumeButton.setText("Pause");
    }public void pause(){
        timer.stop();
        pauseResumeButton.setText("Resume");
    }
}