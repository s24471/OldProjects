public class Player extends Entity {
    Direction clastDirection;
    private Direction lastDirection;
    int score;

    public Player(int row, int column, Model model) {
        super(row, column, model);
        spriteNum = 0;
        intervalMs = 400;
        maxSprite=3;
        score = 0;
    }


    @Override
    public void move() {
        if(clastDirection==null && lastDirection == null)return;
        int y2 = y;
        int x2 = x;
        switch (clastDirection) {
            case UP -> y2--;
            case DOWN -> y2++;
            case LEFT -> x2--;
            case RIGHT -> x2++;
        }
        if (isValid(y2, x2)) {
            if (moving(y2, x2)) return;
            lastDirection = clastDirection;
        } else {
            if(lastDirection!=null) {
                y2 = y;
                x2 = x;
                switch (lastDirection) {
                    case UP -> y2--;
                    case DOWN -> y2++;
                    case LEFT -> x2--;
                    case RIGHT -> x2++;
                }
                if (isValid(y2, x2)) {
                    if (moving(y2, x2)) return;
                }
            }
        }
        if(lastDirection!=null)direction = lastDirection.n;
    }

    boolean moving(int y2, int x2) {
        Tile t = model.arr.getValueAt(y2, x2);
        Entity e = t.entity;
        if(e!=null){
            if(e instanceof Ghost){
                model.playerKilled();
                return true;
            }
        }
        while(t.powerUps.size()>0)t.powerUps.get(0).use();
        model.arr.move(x, y, x2, y2);
        x = x2;
        y = y2;
        return false;
    }

    public void addScore(int n){
        score += n;
    }

}
