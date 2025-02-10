import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class EnergyPlotFrame extends JFrame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private XYSeries potentialEnergySeries;
    private XYSeries kineticEnergySeries;
    private XYSeries totalEnergySeries;

    public EnergyPlotFrame(String name) {
        setTitle("Energy Plot");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);

        potentialEnergySeries = new XYSeries("Potencjalna Energia");
        kineticEnergySeries = new XYSeries("Kinetyczna Energia");
        totalEnergySeries = new XYSeries("Calkowita Energia");

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(potentialEnergySeries);
        dataset.addSeries(kineticEnergySeries);
        dataset.addSeries(totalEnergySeries);

        JFreeChart chart = ChartFactory.createXYLineChart(name, "Czas", "Energia", dataset, PlotOrientation.VERTICAL, true, true, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        setContentPane(chartPanel);
    }

    public void addDataPoint(double time, double potentialEnergy, double kineticEnergy, double totalEnergy) {
        potentialEnergySeries.add(time, potentialEnergy);
        kineticEnergySeries.add(time, kineticEnergy);
        totalEnergySeries.add(time, totalEnergy);
    }
}
