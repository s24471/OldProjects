import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        createFrames(1);
        createFrames(2);
        createFrames(3);

        SwingUtilities.invokeLater(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            frame1.setLocation(0, 0);
            frame2.setLocation(frame1.getX() + frame1.getWidth(), frame1.getY());
            frame3.setLocation(frame2.getX() + frame2.getWidth(), frame2.getY());

            energyPlotFrame1.setLocation(0, frame1.getHeight());
            energyPlotFrame2.setLocation(frame1.getX() + frame1.getWidth(), frame2.getHeight());
            energyPlotFrame3.setLocation(frame2.getX() + frame2.getWidth(), frame3.getHeight());
        });
    }

    JFrame frame1, frame2, frame3;
    EnergyPlotFrame energyPlotFrame1, energyPlotFrame2, energyPlotFrame3;

    public void createFrames(int n) {
        SwingUtilities.invokeLater(() -> {
            String name="";
            switch (n) {
                case 1:
                    name = "Euler";
                    break;
                case 2:
                    name = "Ulepszony Euler";
                    break;
                case 3:
                    name = "RK4";
                    break;
            }
            EnergyPlotFrame energyPlotFrame = new EnergyPlotFrame(name);
            energyPlotFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            energyPlotFrame.setLocationRelativeTo(null);
            energyPlotFrame.setVisible(true);

            JFrame frame = new PendulumSimulationFrame(n, energyPlotFrame);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            switch (n) {
                case 1:
                    frame1 = frame;
                    energyPlotFrame1 = energyPlotFrame;
                    break;
                case 2:
                    frame2 = frame;
                    energyPlotFrame2 = energyPlotFrame;
                    break;
                case 3:
                    frame3 = frame;
                    energyPlotFrame3 = energyPlotFrame;
                    break;
            }
        });
    }
}
