

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Simulation sim = new Simulation();
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeriesCollection energyDataset = new XYSeriesCollection();

        XYSeries ballPositionSeries = new XYSeries("Pozycja kuli");
        XYSeries ballEdgeSeries = new XYSeries("Krawędź kuli");
        XYSeries EpSeries = new XYSeries("Energia potencjalna");
        XYSeries EkSeries = new XYSeries("Energia kinetyczna");
        XYSeries EtSeries = new XYSeries("Energia całkowita");

        //SimulationChart simulationChart = new SimulationChart();

        EnergyChart energyChart = new EnergyChart();
        JFreeChart chart = ChartFactory.createXYLineChart("name", "t", "a", dataset, PlotOrientation.VERTICAL, true, true, false);
        JFreeChart chart2 = ChartFactory.createXYLineChart("name", "t", "a", energyDataset, PlotOrientation.VERTICAL, true, true, false);


        dataset.addSeries(ballPositionSeries);
        dataset.addSeries(ballEdgeSeries);

        energyDataset.addSeries(EpSeries);
        energyDataset.addSeries(EkSeries);
        energyDataset.addSeries(EtSeries);
        JFrame frame = new ChartFrame("Wykres", chart);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        JFrame energyFrame = new ChartFrame("Energia", chart2);
        energyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        energyFrame.pack();
        energyFrame.setVisible(true);
        while (sim.t.doubleValue() <= 3.35 || true) { // wykres będzie rysowany dla pierwszych 10 sekund symulacji
            //simulationChart.addDataPoint(sim.x2, sim.y2);
            EpSeries.add(sim.t, sim.Ep);
            EkSeries.add(sim.t, sim.Ek);
            EtSeries.add(sim.t, sim.Et);
            System.out.println(sim.get1());
            System.out.println(sim.get2());
            ballPositionSeries.add(sim.x, sim.y);
            ballEdgeSeries.add(sim.x2, sim.y2);

            sim.calc();
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    }
}
