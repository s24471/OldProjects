
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener {
    private Direction lastDirection;
    Model model;
    Player player;
    boolean isAlive;

    public Controller(Model model, Player player) {
        this.model = model;
        this.player = player;
        isAlive = true;
    }


    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, 38 -> {
                lastDirection = Direction.UP;
            }
            case KeyEvent.VK_S, 40-> {
                lastDirection = Direction.DOWN;
            }
            case KeyEvent.VK_A, 37 -> {
                lastDirection = Direction.LEFT;
            }
            case KeyEvent.VK_D, 39-> {
                lastDirection = Direction.RIGHT;
            }
            case KeyEvent.VK_Q -> {
                if(e.isControlDown() && e.isShiftDown()) {
                    Model.lifes = 0;
                    model.playerKilled();



                }
            }
        }
        player.clastDirection = lastDirection;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
