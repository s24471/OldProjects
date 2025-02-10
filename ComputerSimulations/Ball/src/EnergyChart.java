import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class EnergyChart {
    JFreeChart chart;
    public EnergyChart() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries EpSeries = new XYSeries("Energia potencjalna");
        XYSeries EkSeries = new XYSeries("Energia kinetyczna");
        XYSeries EtSeries = new XYSeries("Energia całkowita");

        dataset.addSeries(EpSeries);
        dataset.addSeries(EkSeries);
        dataset.addSeries(EtSeries);

        chart = ChartFactory.createXYLineChart(
                "Energia kulki",
                "Czas [s]",
                "Energia [J]",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                true
        );
    }
}