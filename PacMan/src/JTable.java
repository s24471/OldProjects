import javax.swing.table.AbstractTableModel;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class JTable extends AbstractTableModel {

    int rows;
    int columns;
    Tile[][] arr;

    public JTable(int rows, int columns) {
        super();
        this.rows = rows;
        this.columns = columns;
        arr = new Tile[rows][columns];
    }

    @Override
    public int getRowCount() {
        return rows;
    }

    @Override
    public int getColumnCount() {
        return columns;
    }

    @Override
    public Tile getValueAt(int rowIndex, int columnIndex) {
        return arr[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        arr[rowIndex][columnIndex] = (Tile)aValue;
    }
    public void move(int x1, int y1, int x2, int y2){
        arr[y2][x2].setEntity(arr[y1][x1].entity);
        arr[y1][x1].removeEntity();
    }
    public void kill(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                arr[i][j].kill();
            }
        }
    }

    public Point random(){
        ArrayList<Point> p = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(arr[i][j].type==0 && arr[i][j].entity == null)
                    p.add(new Point(i, j));
            }
        }
        Random r = new Random();
        return p.get(r.nextInt(p.size()));
    }
}
