package Project_4;

public class Sticker {
    Logic logic;
    Visiual visiual;

    public Sticker(Logic logic, Visiual visiual) {
        this.logic = logic;
        this.visiual = visiual;
        logic.addActionListener(new ActionListener() {
            @Override
            public void onAction() {
                refresh();
            }
        });
        logic.addKeyCheckListener(new KeyCheckListener() {
            @Override
            public void onKeyCheck() {
                logic.kierunek = visiual.refresh();
            }
        });
        logic.addFoodEatenListener(new FoodEatenListener() {
            @Override
            public void onFoodEaten() {
                visiual.addPoint(logic.listaWeza.size());
            }
        });
        refresh();
    }


    public void refresh() {
        for (int i = 0; i < Main.ROW; i++) {
            for (int j = 0; j < Main.COL; j++) {
                if((int)visiual.tableModel.getValueAt(i, j) != logic.tablica[i][j])
                    visiual.tableModel.setValueAt(logic.tablica[i][j], i, j);
            }
        }
        visiual.jtable.repaint();
    }
}
