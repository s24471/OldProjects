package Project_4;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

// 0 - trawa
// 1 - jedzenie
// 2 - waz
public class Logic implements Runnable {
    int[][] tablica;
    ArrayList<int[]> listaWeza;
    int x;
    int y;
    Kierunek kierunek;
    Kierunek prevKierunek;
    Thread thread;
    boolean isAvlie;
    int dlugoscWeza;
    ArrayList<MoveListener> moveListeners;
    ArrayList<FoodEatenListener> foodEatenListeners;
    ArrayList<OutOfBorderListener> outOfBorderListeners;
    ArrayList<CannibalismListener> cannibalismListeners;
    ArrayList<ActionListener> actionListeners;
    ArrayList<KeyCheckListener> keyCheckListeners;
    ArrayList<EndOfGameListener> endOfGameListeners;

    public Logic() {
        moveListeners = new ArrayList<>();
        foodEatenListeners = new ArrayList<>();
        outOfBorderListeners = new ArrayList<>();
        cannibalismListeners = new ArrayList<>();
        actionListeners = new ArrayList<>();
        keyCheckListeners = new ArrayList<>();
        endOfGameListeners = new ArrayList<>();


        addCannibalismListener(new CannibalismListener() {
            @Override
            public void onCannibalism() {
                System.out.println("Zjadłeś sam siebie");
                endGame();
            }
        });

        addOutOfBorderListener(new OutOfBorderListener() {
            @Override
            public void onOutOfBorder() {
                System.out.println("Wyszedles poza plansze");
                endGame();
            }
        });

        addFoodEatenListener(new FoodEatenListener() {
            @Override
            public void onFoodEaten() {
                spawnFood(1);
            }
        });

        addActionListener(new ActionListener() {
            @Override
            public void onAction() {
                action();
            }
        });
        //this.listaWeza = new ArrayList<int[2]>();
        tablica = new int[Main.ROW][Main.COL];
        isAvlie = true;
        kierunek = Kierunek.down;
        prevKierunek = Kierunek.down;
        dlugoscWeza = 2;

        for (int i = 0; i < Main.ROW; i++) {
            for (int j = 0; j < Main.COL; j++) {
                tablica[i][j] = 0;
            }
        }

        spawnFood(6);

        dlugoscWeza = 2;

        y = Main.ROW / 2;
        x = Main.COL / 2;
        tablica[y][x] = 2; //głowa start
        tablica[y - 1][x] = 3; //ciało start
        listaWeza = new ArrayList<>();
        listaWeza.add(new int[]{y - 1, x});
        listaWeza.add(new int[]{y, x});
        thread = new Thread(this);
        thread.start();

    }

    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
            }
            fireAction();
        }
    }

    public void fireAction() {
        for (ActionListener o : actionListeners)
            o.onAction();
    }

    public void addActionListener(ActionListener actionListener) {
        actionListeners.add(actionListener);
    }

    public void addCannibalismListener(CannibalismListener cannibalismListener) {
        cannibalismListeners.add(cannibalismListener);
    }

    public void addFoodEatenListener(FoodEatenListener foodEatenListener) {
        foodEatenListeners.add(foodEatenListener);
    }

    public void addMoveListener(MoveListener moveListener) {
        moveListeners.add(moveListener);
    }

    public void addOutOfBorderListener(OutOfBorderListener outOfBorderListener) {
        outOfBorderListeners.add(outOfBorderListener);
    }

    public void addKeyCheckListener(KeyCheckListener keyCheckListener) {
        keyCheckListeners.add(keyCheckListener);
    }

    public void fireKeyCheck() {
        for (KeyCheckListener o : keyCheckListeners)
            o.onKeyCheck();
    }

    public void action() {
        fireKeyCheck();
        if (kierunek == Kierunek.down) {
            y += 1;
        } else if (kierunek == Kierunek.up) {
            y -= 1;
        } else if (kierunek == Kierunek.right) {
            x += 1;
        } else if (kierunek == Kierunek.left) {
            x -= 1;
        }

        switch (check(y, x)) {
            case border:
                outOfBorder();
                break;

            case nothing:
                move();
                break;

            case food:
                eatFood();
                break;

            case snake:
                cannibalism();
                break;

        }

    }

    public void cannibalism() {
        fireOnCannibalism();
    }

    public void fireEndOfGame(){
        for(EndOfGameListener o: endOfGameListeners)
            o.onEndOfGame();
    }
    public void addEndOfGameListener(EndOfGameListener endOfGameListener){
        endOfGameListeners.add(endOfGameListener);
    }
    public void endGame() {
        fireEndOfGame();
        System.out.println("Game Ended");
        System.exit(0);
    }

    public void outOfBorder() {
        fireOutOfBorder();
    }

    public void fireOutOfBorder() {
        for (OutOfBorderListener o : outOfBorderListeners)
            o.onOutOfBorder();
    }

    public void fireMove() {
        for (MoveListener o : moveListeners)
            o.onMove();
    }

    public void fireFoodEaten() {
        for (FoodEatenListener o : foodEatenListeners)
            o.onFoodEaten();
    }

    public void fireOnCannibalism() {
        for (CannibalismListener o : cannibalismListeners)
            o.onCannibalism();
    }

    public void move() {
        tablica[y][x] = 2;
        tablica[listaWeza.get(0)[0]][listaWeza.get(0)[1]] = 0;
        listaWeza.remove(0);
        tablica[listaWeza.get(listaWeza.size() - 1)[0]][listaWeza.get(listaWeza.size() - 1)[1]] = 3;
        listaWeza.add(new int[]{y, x});
        fireMove();
    }


    public void eatFood() {
        tablica[y][x] = 2;
        tablica[listaWeza.get(listaWeza.size() - 1)[0]][listaWeza.get(listaWeza.size() - 1)[1]] = 3;
        listaWeza.add(new int[]{y, x});
        fireFoodEaten();
    }


    public void spawnFood(int ile) {
        ArrayList<int[]> wolnePola = new ArrayList<>();
        for (int i = 0; i < tablica.length; i++) {
            for (int j = 0; j < tablica[0].length; j++) {
                if (tablica[i][j] == 0) {
                    wolnePola.add(new int[]{i, j});
                }
            }
        }
        Random r = new Random();
        while (ile > 0 && wolnePola.size() > 0) {
            int n = r.nextInt(wolnePola.size());
            tablica[wolnePola.get(n)[0]][wolnePola.get(n)[1]] = 1;
            ile--;
        }
    }

    public Pole check(int y, int x) {
        if (y >= Main.ROW || x >= Main.COL || y < 0 || x < 0) {
            return Pole.border;

        } else if (tablica[y][x] == 0) {
            return Pole.nothing;

        } else if (tablica[y][x] == 2) {
            return Pole.snake;

        } else if (tablica[y][x] == 1){
            return Pole.food;
        }else{
            return Pole.snake;
        }

    }

}