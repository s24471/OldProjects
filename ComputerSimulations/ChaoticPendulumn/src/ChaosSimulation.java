import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChaosSimulation extends JPanel {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private static final int NUM_PENDULUMS = 10000;
    private static final double GRAVITY = 9.81;
    private static final double LENGTH1 = 100.0;
    private static final double LENGTH2 = 150.0;
    private static final double MASS1 = 10.0;
    private static final double MASS2 = 1.0;
    private static final double DELTA_T = 0.1;

    private double[] theta1 = new double[NUM_PENDULUMS];
    private double[] theta2 = new double[NUM_PENDULUMS];
    private double[] omega1 = new double[NUM_PENDULUMS];
    private double[] omega2 = new double[NUM_PENDULUMS];

    public ChaosSimulation() {
        for (int i = 0; i < NUM_PENDULUMS; i++) {
            theta1[i] = (i*0.00001) + Math.PI * 0.85;
            theta2[i] = (i*0.00001+Math.random()*0.01) + Math.PI * 0.85;
            omega1[i] = 0;
            omega2[i] = 0;
        }

        JFrame frame = new JFrame("Chaos Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);

        while (true) {
            update();
            repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        for (int i = 0; i < NUM_PENDULUMS; i++) {
            double alpha1 = -GRAVITY * (2 * MASS1 + MASS2) * Math.sin(theta1[i])
                    - MASS2 * GRAVITY * Math.sin(theta1[i] - 2 * theta2[i])
                    - 2 * Math.sin(theta1[i] - theta2[i]) * MASS2 * (omega2[i] * omega2[i] * LENGTH2 + omega1[i] * omega1[i] * LENGTH1 * Math.cos(theta1[i] - theta2[i]));
            alpha1 /= LENGTH1 * (2 * MASS1 + MASS2 - MASS2 * Math.cos(2 * theta1[i] - 2 * theta2[i]));

            double alpha2 = 2 * Math.sin(theta1[i] - theta2[i]) * (omega1[i] * omega1[i] * LENGTH1 * (MASS1 + MASS2)
                    + GRAVITY * (MASS1 + MASS2) * Math.cos(theta1[i]) + omega2[i] * omega2[i] * LENGTH2 * MASS2 * Math.cos(theta1[i] - theta2[i]));
            alpha2 /= LENGTH2 * (2 * MASS1 + MASS2 - MASS2 * Math.cos(2 * theta1[i] - 2 * theta2[i]));

            omega1[i] += alpha1 * DELTA_T;
            omega2[i] += alpha2 * DELTA_T;
            theta1[i] += omega1[i] * DELTA_T;
            theta2[i] += omega2[i] * DELTA_T;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < NUM_PENDULUMS; i++) {
            int colorValue = (int)(255/NUM_PENDULUMS*i);
            Color color = new Color(colorValue, colorValue, colorValue);
            g.setColor(color);

            int x1 = (int) (WIDTH / 2 + LENGTH1 * Math.sin(theta1[i]));
            int y1 = (int) (HEIGHT / 2 + LENGTH1 * Math.cos(theta1[i]));
            int x2 = (int) (x1 + LENGTH2 * Math.sin(theta2[i]));
            int y2 = (int) (y1 + LENGTH2 * Math.cos(theta2[i]));
            g.drawLine((int) (WIDTH / 2), (int) (HEIGHT / 2), x1, y1);
            g.drawLine(x1, y1, x2, y2);
        }
    }

    public static void main(String[] args) {
        new ChaosSimulation();
    }
}
