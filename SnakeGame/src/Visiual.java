package Project_4;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.awt.event.KeyEvent.*;

public class Visiual {
    public static int ROW = Main.ROW;
    public static int COL = Main.COL;
    JTable jtable;
    JPanel panel;
    JFrame frame;
    Kierunek kierunek;
    Kierunek prevKierunek;
    DefaultTableModel tableModel;

    ImageIcon grassIcon;
    ImageIcon headIcon;
    ImageIcon bodyIcon;
    ImageIcon foodIcon;

    JLabel pointsLabel;
    int point;
    public Visiual() {
        point = 0;
        prevKierunek = Kierunek.down;
        kierunek = Kierunek.down;
        grassIcon = new ImageIcon("res/Grass.png");
        headIcon = new ImageIcon("res/Head.png");
        bodyIcon = new ImageIcon("res/Body.png");
        foodIcon = new ImageIcon("res/Food.png");
        frame = new JFrame();
        panel = new JPanel();
        tableModel = new DefaultTableModel(ROW, COL);
        jtable = new JTable(tableModel);
        jtable.setShowGrid(true);
        jtable.setEnabled(false);
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case VK_DOWN:
                        if (prevKierunek != Kierunek.up) {
                            kierunek = Kierunek.down;
                        }
                        break;
                    case VK_UP:
                        if (prevKierunek != Kierunek.down) {
                            kierunek = Kierunek.up;
                        }
                        break;
                    case VK_LEFT:
                        if (prevKierunek != Kierunek.right) {
                            kierunek = Kierunek.left;
                        }
                        break;
                    case VK_RIGHT:
                        if (prevKierunek != Kierunek.left) {
                            kierunek = Kierunek.right;
                        }
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        jtable.setRowHeight(50);
        jtable.getColumnModel().getColumn(0).setPreferredWidth(50);
        jtable.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = frame.getSize();
                int cellSize;

                if (size.height / (ROW + 1) > size.width / (COL + 1)) {
                    cellSize = size.width / (COL + 1);
                } else {
                    cellSize = size.height / (ROW + 1);
                }
                jtable.setRowHeight(cellSize);

                for (int i = 0; i < COL; i++) {
                    jtable.getColumnModel().getColumn(i).setMaxWidth(cellSize);
                }
            }
        });
        jtable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                int intValue = (int) value;
                JLabel cellRenderer = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                ImageIcon img;
                switch (intValue) {
                    case 0:
                        img = grassIcon;
                        break;
                    case 1:
                        img = foodIcon;
                        break;
                    case 2:
                        img = headIcon;
                        break;
                    default:
                        img = bodyIcon;
                        break;
                }

                // Scale the image to fit the cell size
                Dimension size = jtable.getSize();
                int cellSize;

                if (size.height / (ROW + 1) > size.width / (COL + 1)) {
                    cellSize = size.width / (COL + 1);
                } else {
                    cellSize = size.height / (ROW + 1);
                }
                Image scaledImage = img.getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
                img = new ImageIcon(scaledImage);

                cellRenderer.setIcon(img);
                cellRenderer.setText("");

                return cellRenderer;
            }
        });
        pointsLabel = new JLabel("Points: 0");


        resetModel();
        frame.add(jtable);

        frame.setTitle("Snake");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.add(pointsLabel);
        frame.getContentPane().add(panel, BorderLayout.NORTH);
        jtable.setBackground(Color.black);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jtable.setDoubleBuffered(true);
        panel.setDoubleBuffered(true);

    }

    public Kierunek refresh() {
        prevKierunek = kierunek;
        return kierunek;
    }
    public void addPoint(int n){
        point+=n;
        pointsLabel.setText("Points: " + point);
    }
    private void resetModel() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                tableModel.setValueAt(0, i, j);
            }
        }
    }


}
