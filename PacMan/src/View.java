import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class View extends JPanel {
    int rows;
    int columns;
    Model model;
    static int tileHeight;
    static int tileWidth;
    public View(int rows, int columns, Model model) {
        super();
        this.model = model;
        this.rows = rows;
        this.columns = columns;
        initGui();
    }

    public void initGui(){
        setBackground(Color.black);
        adjustTileSize();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustTileSize();
            }
        });
    }



    private void adjustTileSize() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        tileWidth = panelWidth / columns;
        tileHeight = panelHeight / rows;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                model.arr.getValueAt(x, y).draw(g, y*tileWidth, x*tileHeight);
            }
        }
    }
}

