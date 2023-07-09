import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static jdk.nashorn.internal.objects.Global.Infinity;

public class GUI {

    private final JFrame frame = new JFrame();
    private JButton startButton;
    private JButton pauseButton;
    private XYSeries series;
    private java.util.List<Double> xValues;
    private List<Double> yValues;
    JFreeChart chart;
    XYPlot plot;
    private Monte_Carlo mc;
    private JLabel lblProbability;
    private JLabel lblAvgWaitTime;

    public GUI(Monte_Carlo mc){
        this.mc = mc;
        this.mc.setGUI(this);
        initView();
        graphGui();
        labels();

        // Set the frame properties
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void labels() {
        lblProbability=new JLabel("");
        lblAvgWaitTime=new JLabel("");
        JLabel lblSeparate = new JLabel("|");

        lblAvgWaitTime.setFont(new Font("Calibri", Font.PLAIN, 25));
        lblProbability.setFont(new Font("Calibri", Font.PLAIN, 25));
        // Create a panel to hold the buttons
        JPanel labelPanel = new JPanel();
        labelPanel.add(lblProbability);
        labelPanel.add(lblSeparate);
        labelPanel.add(lblAvgWaitTime);
        frame.add(labelPanel, BorderLayout.NORTH);
    }

    private void graphGui() {
        this.series = new XYSeries("Data");

        XYSeriesCollection dataset = new XYSeriesCollection(this.series);
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
//        frame.setContentPane(chartPanel);

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);

        // Add the chart panel and button panel to the frame
        frame.add(chartPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initView() {
        // Create two buttons using Swing
        startButton = new JButton("Spusti");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                series.clear();
                Thread simulationThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        lblProbability.setText("");
                        lblAvgWaitTime.setText("");
                        mc.simulate(100000000);
                    }
                });
                simulationThread.start();
            }
        });

        pauseButton = new JButton("Zastav");
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mc.setRunning(false);
            }
        });

    }
    private JFreeChart createChart(final XYSeriesCollection dataset) {
        chart = ChartFactory.createXYLineChart(
                "Monte Carlo Simulation",
                "Replications (*1000)",
                "Probability",
                dataset
        );
//        chart.setBackgroundPaint(Color.white);
        plot = chart.getXYPlot();
//        plot.setBackgroundPaint(Color.lightGray);
//        plot.setDomainGridlinePaint(Color.white);
//        plot.setRangeGridlinePaint(Color.white);
        final NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setAutoRange(true);
//        domainAxis.setFixedAutoRange(60000.0);  // 60 seconds
        domainAxis.setLowerMargin(0.0); // set the lower margin to zero
        domainAxis.setUpperMargin(0.0); // set the lower margin to zero

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRange(true);
        plot.getRangeAxis().setAutoRange(true);                            // uncomment
        ((NumberAxis)plot.getRangeAxis()).setAutoRangeIncludesZero(false); // add
        final XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
        return chart;
    }

    public void addPoint(double percentage, int replicationNumber) {
        if (percentage != Infinity) {
            this.series.add(replicationNumber/1000, percentage);
        }

        final double maxY = this.series.getMaxY();
        final double minY = this.series.getMinY();
        final NumberAxis rangeAxis = (NumberAxis) this.chart.getXYPlot().getRangeAxis();
        if (maxY != minY) {
            rangeAxis.setRange(minY, maxY);
        }
    }

    public void setLblProbability(double probability) {
//        DecimalFormat df = new DecimalFormat("####0.00");
        lblProbability.setText("Pravdepodobnost, ze sa skupine B podari prist do 13:00 je " +  /*df.format(*/probability/*)*/ + "%");
    }

    public void setLblAvgWaitTime(double averageWaitingTime) {
        int minutes = (int) averageWaitingTime; // Get the integer part of the decimal minutes
        int seconds = (int) ((averageWaitingTime - minutes) * 60); // Get the number of seconds
        String mmss = String.format("%02d:%02d", minutes, seconds); // Format the output as MM:SS

        lblAvgWaitTime.setText("Priemerne cakanie skubiny B na skupinu A: " +mmss+" minut alebo " + averageWaitingTime);
    }

    public void numberOfPoints() {
        System.out.println(this.series.getItemCount());
    }
}
