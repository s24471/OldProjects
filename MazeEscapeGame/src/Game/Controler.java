package Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.awt.event.KeyEvent.*;

public class Controler implements KeyListener {
    private Model model;

    public Controler(Model model) {
        this.model = model;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int currentKey = e.getKeyCode();
        if (currentKey == VK_W || currentKey == VK_S || currentKey == VK_A || currentKey == VK_D)
            model.getPlayer().setCurrentDirection(currentKey);

        if(currentKey == VK_E)
            model.getPlayer().setBendForItem(true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int currentKey = e.getKeyCode();
        if (model.getPlayer().getCurrentDirection() == currentKey)
            model.getPlayer().setCurrentDirection(0);

        if(currentKey == VK_E)
            model.getPlayer().setBendForItem(false);
    }

}
