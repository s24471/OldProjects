import javax.swing.*;

public class PendulumSimulationFrame extends JFrame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    public PendulumSimulationFrame(int n, EnergyPlotFrame energyPlotFrame) {
        setTitle("Symulacja Wahadła");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);

        PendulumPanel panel = new PendulumPanel(n, energyPlotFrame);
        add(panel);
    }
}