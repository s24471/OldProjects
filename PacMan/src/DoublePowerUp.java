
import java.awt.Point;
public class DoublePowerUp extends  PowerUp {
    public DoublePowerUp(Model model, Tile t) {
        super(model, t);
        spriteNum = 10;
    }

    @Override
    public void use() {
        super.use();
        model.player.score*=2;
        Menu.points.setText("Wynik tego poziomu: " + String.valueOf(model.player.score));
    }
}

