import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

public class MovingFrame {
    private JFrame frame;
    private JButton button;
    private boolean isAlive;
    private int x, y;
    private int directionX, directionY;

    public MovingFrame() {
        isAlive = true;
        frame = new JFrame("Moving Frame");
        frame.setSize(500, 500);
        frame.setLayout(null);

        button = new JButton("Kill Me");
        button.setBounds(50, 50, 100, 50);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isAlive = false;
                frame.setVisible(false);
            }
        });

        frame.add(button);
        frame.setVisible(true);

        x = frame.getX();
        y = frame.getY();
        directionX = 1;
        directionY = 1;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(Model.running && isAlive) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                        moveFrame();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    private void moveFrame() {
        x += directionX;
        y += directionY;

        if (x <= 0 || x >= Toolkit.getDefaultToolkit().getScreenSize().getWidth() - frame.getWidth()) {
            directionX *= -1;
        }

        if (y <= 0 || y >= Toolkit.getDefaultToolkit().getScreenSize().getHeight() - frame.getHeight()) {
            directionY *= -1;
        }

        frame.setLocation(x, y);
    }
}
