import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.MathContext;

public class PendulumPanel extends JPanel {
    private static final BigDecimal G = new BigDecimal("10");
    private static final BigDecimal L = BigDecimal.ONE;
    private static final BigDecimal DT = new BigDecimal("0.01");
    private static final BigDecimal DAMPING_FACTOR = new BigDecimal("0.1");
    private static final BigDecimal INIT_THETA = BigDecimal.valueOf(Math.PI / 4);
    private static final BigDecimal INIT_OMEGA = BigDecimal.ZERO;
    private BigDecimal theta;
    private BigDecimal omega;
    private int mode;
    BigDecimal elapsedTime = new BigDecimal(0);
    EnergyPlotFrame energyPlotFrame;
    public PendulumPanel(int n, EnergyPlotFrame energyPlotFrame) {
        this.energyPlotFrame = energyPlotFrame;
        mode = n;
        theta = INIT_THETA;
        omega = INIT_OMEGA;

        Thread simulationThread = new Thread(() -> {
            while (true) {
                BigDecimal alpha = (G.divide(L, 6, BigDecimal.ROUND_HALF_UP)).negate().multiply(new BigDecimal(Math.sin(theta.doubleValue()))).subtract(DAMPING_FACTOR.multiply(omega)).setScale(6, BigDecimal.ROUND_HALF_UP);

                switch (mode) {
                    case 1:
                        theta = theta.add(omega.multiply(DT)).setScale(6, BigDecimal.ROUND_HALF_UP);
                        omega = omega.add(alpha.multiply(DT)).setScale(6, BigDecimal.ROUND_HALF_UP);
                        break;
                    case 2:
                        BigDecimal thetaMid = theta.add(omega.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128));
                        BigDecimal omegaMid = omega.add(alpha.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128));
                        BigDecimal alphaMid = (G.divide(L, 6, BigDecimal.ROUND_HALF_UP)).negate().multiply(new BigDecimal(Math.sin(thetaMid.doubleValue()))).subtract(DAMPING_FACTOR.multiply(omegaMid)).setScale(6, BigDecimal.ROUND_HALF_UP);
                        theta = theta.add(omegaMid.multiply(DT)).setScale(6, BigDecimal.ROUND_HALF_UP);
                        omega = omega.add(alphaMid.multiply(DT)).setScale(6, BigDecimal.ROUND_HALF_UP);
                        break;
                    case 3:
                        BigDecimal k1_theta = omega;
                        BigDecimal k1_omega = alpha;
                        BigDecimal k2_theta = omega.add(k1_omega.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128));
                        BigDecimal k2_omega = (G.divide(L, 6, BigDecimal.ROUND_HALF_UP)).negate().multiply(new BigDecimal(Math.sin(theta.add(k1_theta.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128)).doubleValue()))).subtract(DAMPING_FACTOR.multiply(omega.add(k1_omega.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128)))).setScale(6, BigDecimal.ROUND_HALF_UP);
                        BigDecimal k3_theta = omega.add(k2_omega.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128));
                        BigDecimal k3_omega = (G.divide(L, 6, BigDecimal.ROUND_HALF_UP)).negate().multiply(new BigDecimal(Math.sin(theta.add(k2_theta.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128)).doubleValue()))).subtract(DAMPING_FACTOR.multiply(omega.add(k2_omega.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128)))).setScale(6, BigDecimal.ROUND_HALF_UP);
                        BigDecimal k4_theta = omega.add(k3_omega.multiply(DT));
                        BigDecimal k4_omega = (G.divide(L, 6, BigDecimal.ROUND_HALF_UP)).negate().multiply(new BigDecimal(Math.sin(theta.add(k3_theta.multiply(DT)).doubleValue()))).subtract(DAMPING_FACTOR.multiply(omega.add(k3_omega.multiply(DT)))).setScale(6, BigDecimal.ROUND_HALF_UP);
                        theta = theta.add(k1_theta.add(k2_theta.multiply(BigDecimal.valueOf(2))).add(k3_theta.multiply(BigDecimal.valueOf(2))).add(k4_theta).multiply(DT).divide(BigDecimal.valueOf(6), MathContext.DECIMAL128)).setScale(6, BigDecimal.ROUND_HALF_UP);
                        omega = omega.add(k1_omega.add(k2_omega.multiply(BigDecimal.valueOf(2))).add(k3_omega.multiply(BigDecimal.valueOf(2))).add(k4_omega).multiply(DT).divide(BigDecimal.valueOf(6), MathContext.DECIMAL128)).setScale(6, BigDecimal.ROUND_HALF_UP);
                        break;
                }
                double potentialEnergy = G.doubleValue() * L.doubleValue() * (1 - Math.cos(theta.doubleValue()));
                double kineticEnergy = 0.5 * Math.pow(L.doubleValue() * omega.doubleValue(),2);
                double totalEnergy = potentialEnergy + kineticEnergy;

                energyPlotFrame.addDataPoint(elapsedTime.doubleValue(), potentialEnergy, kineticEnergy, totalEnergy);
                repaint();

                try {
                    int time = (int)(DT.multiply(BigDecimal.valueOf(1000)).doubleValue() / 2);
                    elapsedTime = elapsedTime.add(BigDecimal.valueOf(time));
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        simulationThread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2 - getHeight() / 10;

        int x = centerX + (int) (L.multiply(new BigDecimal(Math.sin(theta.doubleValue()))).multiply(BigDecimal.valueOf(centerY)).doubleValue());
        int y = centerY + (int) (L.multiply(new BigDecimal(Math.cos(theta.doubleValue()))).multiply(BigDecimal.valueOf(centerY)).doubleValue());

        g2.setStroke(new BasicStroke(2));
        g2.drawLine(centerX, centerY, x, y);
        g2.fillOval(x - 10, y - 10, 20, 20);
    }
}








/*import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

public class PendulumPanel extends JPanel implements ActionListener {
    private static final BigDecimal G = new BigDecimal("100");
    private static final BigDecimal L = BigDecimal.ONE;
    private static final BigDecimal DT = new BigDecimal("0.01");
    private static final BigDecimal DAMPING_FACTOR = new BigDecimal("0.1");
    private static final BigDecimal INIT_THETA = BigDecimal.valueOf(Math.PI / 4);
    private static final BigDecimal INIT_OMEGA = BigDecimal.ZERO;
    private BigDecimal theta;
    private BigDecimal omega;
    private int mode;
    private Timer timer;
    private JPanel energyPanel;
    private JPanel potentialEnergyPanel;
    private JPanel kineticEnergyPanel;
    JPanel totalEnergyPanel;

    List<BigDecimal[]> energyData;
    public PendulumPanel(int n) {
        mode = n;
        theta = INIT_THETA;
        omega = INIT_OMEGA;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JPanel fillerPanel = new JPanel();
        fillerPanel.setPreferredSize(new Dimension(getWidth(), 10));
        add(fillerPanel);
        energyData = new ArrayList<>();
        energyPanel = new EnergyGraphPanel();
        add(energyPanel, BorderLayout.SOUTH);
        // create energy panel
        energyPanel = new JPanel(new GridLayout(1, 3));
        energyPanel.setPreferredSize(new Dimension(getWidth(), getHeight() / 2));
        add(energyPanel, BorderLayout.SOUTH);

        // create potential energy panel
        potentialEnergyPanel = new JPanel();
        energyPanel.add(potentialEnergyPanel);

        // create kinetic energy panel
        kineticEnergyPanel = new JPanel();
        energyPanel.add(kineticEnergyPanel);

        // create total energy panel
        totalEnergyPanel = new JPanel();
        energyPanel.add(totalEnergyPanel);

        timer = new Timer((int) (DT.multiply(BigDecimal.valueOf(1000)).doubleValue()), this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int centerX = getWidth() / 2;
        int centerY = (getHeight() - energyPanel.getHeight()) / 2;


        int x = centerX + (int) (L.multiply(new BigDecimal(Math.sin(theta.doubleValue()))).multiply(BigDecimal.valueOf(centerY)).doubleValue());
        int y = centerY + (int) (L.multiply(new BigDecimal(Math.cos(theta.doubleValue()))).multiply(BigDecimal.valueOf(centerY)).doubleValue());

        g2.setStroke(new BasicStroke(2));
        g2.drawLine(centerX, centerY, x, y);
        g2.fillOval(x - 10, y - 10, 20, 20);

        // calculate energy
        BigDecimal potentialEnergy = G.multiply(L.subtract(L.multiply(new BigDecimal(Math.cos(theta.doubleValue())))));
        BigDecimal kineticEnergy = L.pow(2).multiply(omega.pow(2)).divide(new BigDecimal(2), MathContext.DECIMAL128);
        BigDecimal totalEnergy = potentialEnergy.add(kineticEnergy);
        energyData.add(new BigDecimal[]{potentialEnergy, kineticEnergy, totalEnergy});
        energyPanel.repaint();

        // draw potential energy
        Graphics2D potentialEnergyGraphics = (Graphics2D) potentialEnergyPanel.getGraphics();
        potentialEnergyGraphics.setColor(Color.RED);
        potentialEnergyGraphics.fillRect(0, 0, potentialEnergyPanel.getWidth(), potentialEnergyPanel.getHeight() - 20);
        int potentialEnergyHeight = (int) (potentialEnergy.divide(new BigDecimal(200), MathContext.DECIMAL128).multiply(new BigDecimal(getHeight() / 4)).doubleValue());
        potentialEnergyGraphics.setColor(Color.BLACK);
        potentialEnergyGraphics.drawLine(0, potentialEnergyPanel.getHeight() - 20, potentialEnergyPanel.getWidth(), potentialEnergyPanel.getHeight() - 20);
        potentialEnergyGraphics.drawString("PE", 5, 15);
        potentialEnergyGraphics.drawString(String.format("%.2f", potentialEnergy.doubleValue()), 5, potentialEnergyPanel.getHeight() - 5 - potentialEnergyHeight);
        potentialEnergyGraphics.setColor(Color.RED);
        potentialEnergyGraphics.fillRect(0, potentialEnergyPanel.getHeight() - 20 - potentialEnergyHeight, potentialEnergyPanel.getWidth(), potentialEnergyHeight);
        // draw kinetic energy
        Graphics2D kineticEnergyGraphics = (Graphics2D) kineticEnergyPanel.getGraphics();
        kineticEnergyGraphics.setColor(Color.GREEN);
        kineticEnergyGraphics.fillRect(0, 0, kineticEnergyPanel.getWidth(), kineticEnergyPanel.getHeight() - 20);
        int kineticEnergyHeight = (int) (kineticEnergy.divide(new BigDecimal(200), MathContext.DECIMAL128).multiply(new BigDecimal(getHeight() / 4)).doubleValue());
        kineticEnergyGraphics.setColor(Color.BLACK);
        kineticEnergyGraphics.drawLine(0, kineticEnergyPanel.getHeight() - 20, kineticEnergyPanel.getWidth(), kineticEnergyPanel.getHeight() - 20);
        kineticEnergyGraphics.drawString("KE", 5, 15);
        kineticEnergyGraphics.drawString(String.format("%.2f", kineticEnergy.doubleValue()), 5, kineticEnergyPanel.getHeight() - 5 - kineticEnergyHeight);
        kineticEnergyGraphics.setColor(Color.GREEN);
        kineticEnergyGraphics.fillRect(0, kineticEnergyPanel.getHeight() - 20 - kineticEnergyHeight, kineticEnergyPanel.getWidth(), kineticEnergyHeight);

        // draw total energy
        Graphics2D totalEnergyGraphics = (Graphics2D) totalEnergyPanel.getGraphics();
        totalEnergyGraphics.setColor(Color.BLUE);
        totalEnergyGraphics.fillRect(0, 0, totalEnergyPanel.getWidth(), totalEnergyPanel.getHeight() - 20);
        int totalEnergyHeight = (int) (totalEnergy.divide(new BigDecimal(200), MathContext.DECIMAL128).multiply(new BigDecimal(getHeight() / 4)).doubleValue());
        totalEnergyGraphics.setColor(Color.BLACK);
        totalEnergyGraphics.drawLine(0, totalEnergyPanel.getHeight() - 20, totalEnergyPanel.getWidth(), totalEnergyPanel.getHeight() - 20);
        totalEnergyGraphics.drawString("E", 5, 15);
        totalEnergyGraphics.drawString(String.format("%.2f", totalEnergy.doubleValue()), 5, totalEnergyPanel.getHeight() - 5 - totalEnergyHeight);
        totalEnergyGraphics.setColor(Color.BLUE);
        totalEnergyGraphics.fillRect(0, totalEnergyPanel.getHeight() - 20 - totalEnergyHeight, totalEnergyPanel.getWidth(), totalEnergyHeight);

        potentialEnergyPanel.paintComponents(potentialEnergyGraphics);
        kineticEnergyPanel.paintComponents(kineticEnergyGraphics);
        totalEnergyPanel.paintComponents(totalEnergyGraphics);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        BigDecimal alpha = (G.divide(L, 6, BigDecimal.ROUND_HALF_UP)).negate().multiply(new BigDecimal(Math.sin(theta.doubleValue()))).subtract(DAMPING_FACTOR.multiply(omega)).setScale(6, BigDecimal.ROUND_HALF_UP);

        switch (mode) {
            case 1:
                theta = theta.add(omega.multiply(DT)).setScale(6, BigDecimal.ROUND_HALF_UP);
                omega = omega.add(alpha.multiply(DT)).setScale(6, BigDecimal.ROUND_HALF_UP);
                break;
            case 2:
                BigDecimal thetaMid = theta.add(omega.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128));
                BigDecimal omegaMid = omega.add(alpha.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128));
                BigDecimal alphaMid = (G.divide(L, 6, BigDecimal.ROUND_HALF_UP)).negate().multiply(new BigDecimal(Math.sin(thetaMid.doubleValue()))).subtract(DAMPING_FACTOR.multiply(omegaMid)).setScale(6, BigDecimal.ROUND_HALF_UP);
                theta = theta.add(omegaMid.multiply(DT)).setScale(6, BigDecimal.ROUND_HALF_UP);
                omega = omega.add(alphaMid.multiply(DT)).setScale(6, BigDecimal.ROUND_HALF_UP);
                break;
            case 3:
                // RK4 method
                BigDecimal k1_theta = omega;
                BigDecimal k1_omega = alpha;
                BigDecimal k2_theta = omega.add(k1_omega.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128));
                BigDecimal k2_omega = (G.divide(L, 6, BigDecimal.ROUND_HALF_UP)).negate().multiply(new BigDecimal(Math.sin(theta.add(k1_theta.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128)).doubleValue()))).subtract(DAMPING_FACTOR.multiply(omega.add(k1_omega.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128)))).setScale(6, BigDecimal.ROUND_HALF_UP);
                BigDecimal k3_theta = omega.add(k2_omega.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128));
                BigDecimal k3_omega = (G.divide(L, 6, BigDecimal.ROUND_HALF_UP)).negate().multiply(new BigDecimal(Math.sin(theta.add(k2_theta.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128)).doubleValue()))).subtract(DAMPING_FACTOR.multiply(omega.add(k2_omega.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128)))).setScale(6, BigDecimal.ROUND_HALF_UP);
                BigDecimal k4_theta = omega.add(k3_omega.multiply(DT));
                BigDecimal k4_omega = (G.divide(L, 6, BigDecimal.ROUND_HALF_UP)).negate().multiply(new BigDecimal(Math.sin(theta.add(k3_theta.multiply(DT)).doubleValue()))).subtract(DAMPING_FACTOR.multiply(omega.add(k3_omega.multiply(DT)))).setScale(6, BigDecimal.ROUND_HALF_UP);
                theta = theta.add(k1_theta.add(k2_theta.multiply(BigDecimal.valueOf(2))).add(k3_theta.multiply(BigDecimal.valueOf(2))).add(k4_theta).multiply(DT).divide(BigDecimal.valueOf(6), MathContext.DECIMAL128)).setScale(6, BigDecimal.ROUND_HALF_UP);
                omega = omega.add(k1_omega.add(k2_omega.multiply(BigDecimal.valueOf(2))).add(k3_omega.multiply(BigDecimal.valueOf(2))).add(k4_omega).multiply(DT).divide(BigDecimal.valueOf(6), MathContext.DECIMAL128)).setScale(6, BigDecimal.ROUND_HALF_UP);
                break;
        }
        repaint();
    }
}

class EnergyGraphPanel extends JPanel {
    private static final int MARGIN = 00;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        g2.setColor(Color.BLACK);
        g2.drawLine(MARGIN, MARGIN, MARGIN, height - MARGIN);
        g2.drawLine(MARGIN, height - MARGIN, width - MARGIN, height - MARGIN);

        if (((PendulumPanel) getParent()).energyData.size() > 1) {
            for (int i = 0; i < 3; i++) {
                switch (i) {
                    case 0:
                        g2.setColor(Color.RED);
                        break;
                    case 1:
                        g2.setColor(Color.GREEN);
                        break;
                    case 2:
                        g2.setColor(Color.BLUE);
                        break;
                }

                for (int j = 1; j < ((PendulumPanel) getParent()).energyData.size(); j++) {
                    int x1 = (j - 1) * width / (((PendulumPanel) getParent()).energyData.size() - 1);
                    int x2 = j * width / (((PendulumPanel) getParent()).energyData.size() - 1);
                    int y1 = height - MARGIN - (((PendulumPanel) getParent()).energyData.get(j - 1)[i].intValue() * (height - 2 * MARGIN) / 200);
                    int y2 = height - MARGIN - (((PendulumPanel) getParent()).energyData.get(j)[i].intValue() * (height - 2 * MARGIN) / 200);

                    g2.drawLine(x1 + MARGIN, y1, x2 + MARGIN, y2);
                }
            }
        }
    }
}*//*



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.MathContext;

public class PendulumPanel extends JPanel implements ActionListener {
    private EnergyPlotFrame energyPlotFrame;
    private static final BigDecimal G = new BigDecimal("10");
    private static final BigDecimal L = BigDecimal.ONE;
    private static final BigDecimal DT = new BigDecimal("0.005");
    private static final BigDecimal DAMPING_FACTOR = new BigDecimal("0");
    private static final BigDecimal INIT_THETA = BigDecimal.valueOf(Math.PI / 4);
    private static final BigDecimal INIT_OMEGA = BigDecimal.ZERO;
    private BigDecimal theta;
    private BigDecimal omega;
    private int mode;
    private Timer timer;
    private BigDecimal elapsedTime = BigDecimal.ZERO;


    public PendulumPanel(int n, EnergyPlotFrame energyPlotFrame) {
        this.energyPlotFrame = energyPlotFrame;
        mode = n;
        theta = INIT_THETA;
        omega = INIT_OMEGA;
        Thread simulationThread = new Thread(() -> {
            while (true) {
                // Simulation code here

                try {
                    Thread.sleep((int) (DT.multiply(BigDecimal.valueOf(1000)).doubleValue() / 2)); // Adjust the sleep time to control the simulation speed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        simulationThread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        int x = centerX + (int) (L.multiply(new BigDecimal(Math.sin(theta.doubleValue()))).multiply(BigDecimal.valueOf(centerY)).doubleValue());
        int y = centerY + (int) (L.multiply(new BigDecimal(Math.cos(theta.doubleValue()))).multiply(BigDecimal.valueOf(centerY)).doubleValue());

        g2.setStroke(new BasicStroke(2));
        g2.drawLine(centerX, centerY, x, y);
        g2.fillOval(x - 10, y - 10, 20, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        elapsedTime = elapsedTime.add(DT);
        BigDecimal alpha = (G.divide(L, 6, BigDecimal.ROUND_HALF_UP)).negate().multiply(new BigDecimal(Math.sin(theta.doubleValue()))).subtract(DAMPING_FACTOR.multiply(omega)).setScale(6, BigDecimal.ROUND_HALF_UP);

        switch (mode) {
            case 1:
                theta = theta.add(omega.multiply(DT)).setScale(6, BigDecimal.ROUND_HALF_UP);
                omega = omega.add(alpha.multiply(DT)).setScale(6, BigDecimal.ROUND_HALF_UP);
                break;
            case 2:
                BigDecimal thetaMid = theta.add(omega.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128));
                BigDecimal omegaMid = omega.add(alpha.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128));
                BigDecimal alphaMid = (G.divide(L, 6, BigDecimal.ROUND_HALF_UP)).negate().multiply(new BigDecimal(Math.sin(thetaMid.doubleValue()))).subtract(DAMPING_FACTOR.multiply(omegaMid)).setScale(6, BigDecimal.ROUND_HALF_UP);
                theta = theta.add(omegaMid.multiply(DT)).setScale(6, BigDecimal.ROUND_HALF_UP);
                omega = omega.add(alphaMid.multiply(DT)).setScale(6, BigDecimal.ROUND_HALF_UP);
                break;
            case 3:
                BigDecimal k1_theta = omega;
                BigDecimal k1_omega = alpha;
                BigDecimal k2_theta = omega.add(k1_omega.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128));
                BigDecimal k2_omega = (G.divide(L, 6, BigDecimal.ROUND_HALF_UP)).negate().multiply(new BigDecimal(Math.sin(theta.add(k1_theta.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128)).doubleValue()))).subtract(DAMPING_FACTOR.multiply(omega.add(k1_omega.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128)))).setScale(6, BigDecimal.ROUND_HALF_UP);
                BigDecimal k3_theta = omega.add(k2_omega.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128));
                BigDecimal k3_omega = (G.divide(L, 6, BigDecimal.ROUND_HALF_UP)).negate().multiply(new BigDecimal(Math.sin(theta.add(k2_theta.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128)).doubleValue()))).subtract(DAMPING_FACTOR.multiply(omega.add(k2_omega.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128)))).setScale(6, BigDecimal.ROUND_HALF_UP);
                BigDecimal k4_theta = omega.add(k3_omega.multiply(DT));
                BigDecimal k4_omega = (G.divide(L, 6, BigDecimal.ROUND_HALF_UP)).negate().multiply(new BigDecimal(Math.sin(theta.add(k3_theta.multiply(DT)).doubleValue()))).subtract(DAMPING_FACTOR.multiply(omega.add(k3_omega.multiply(DT)))).setScale(6, BigDecimal.ROUND_HALF_UP);
                theta = theta.add(k1_theta.add(k2_theta.multiply(BigDecimal.valueOf(2))).add(k3_theta.multiply(BigDecimal.valueOf(2))).add(k4_theta).multiply(DT).divide(BigDecimal.valueOf(6), MathContext.DECIMAL128)).setScale(6, BigDecimal.ROUND_HALF_UP);
                omega = omega.add(k1_omega.add(k2_omega.multiply(BigDecimal.valueOf(2))).add(k3_omega.multiply(BigDecimal.valueOf(2))).add(k4_omega).multiply(DT).divide(BigDecimal.valueOf(6), MathContext.DECIMAL128)).setScale(6, BigDecimal.ROUND_HALF_UP);
                break;
        }
        double potentialEnergy = G.doubleValue() * L.doubleValue() * (1 - Math.cos(theta.doubleValue()));
        double kineticEnergy = 0.5 * Math.pow(L.doubleValue() * omega.doubleValue(),2);
        double totalEnergy = potentialEnergy + kineticEnergy;
        double time = timer.getDelay() * timer.getActionListeners()[0].hashCode() / 1000.0;
        energyPlotFrame.addDataPoint(elapsedTime.doubleValue(), potentialEnergy, kineticEnergy, totalEnergy);
        repaint();
    }
}
*/
