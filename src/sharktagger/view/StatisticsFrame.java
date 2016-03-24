package sharktagger.view;

import java.awt.GridLayout;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class StatisticsFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    /** String constants. */
    public static final String TITLE = "Statistics";

    public static final String GENDER_PIECHART_TITLE = "Gender";
    public static final String STAGE_PIECHART_TITLE = "Stage of Life";
    public static final String LOCATION_PIECHART_TITLE = "Tag Location";

    private static final String STATISTICS_EMPTY_TEXT = "No sharks are found with selected option set.";
    private static final String STATISTICS_UPDATING_TEXT = "Updating statistics...";

    public static final int WIDTH = 500;
    public static final int HEIGHT = 1000;

    /** Instance variables. */
    private Statistic mStatistic;

    private JLabel jlEmpty;
    private JLabel jlUpdating;
    private JPanel jpMain;

    public void setupUI() {
        jlEmpty.setHorizontalAlignment(JLabel.CENTER);
        jlUpdating.setHorizontalAlignment(JLabel.CENTER);

        jpMain.setLayout(new GridLayout(0, 1));
    }

    public StatisticsFrame() {
        super(TITLE);
        this.setSize(WIDTH, HEIGHT);

        jlEmpty = new JLabel(STATISTICS_EMPTY_TEXT);
        jlUpdating = new JLabel(STATISTICS_UPDATING_TEXT);
        jpMain = new JPanel();

        setupUI();

        setStatistic(null);
    }

    public void setUpdating() {
        this.getContentPane().removeAll();

        this.add(jlUpdating);
    }

    public void setStatistic(Statistic stat) {
        mStatistic = stat;

        this.getContentPane().removeAll();
        jpMain.removeAll();
        if (mStatistic == null || mStatistic.isEmpty()) {
            this.add(jlEmpty);
            return;
        }

        List<JFreeChart> charts = getCharts();
        for (JFreeChart chart : charts) {
            ChartPanel chartPanel = new ChartPanel(chart);
            jpMain.add(chartPanel);
        }
        this.add(jpMain);
        repaint(); revalidate();
    }

    private List<JFreeChart> getCharts() {
        DefaultPieDataset dataGender, dataStage, dataLocation;

        dataGender = new DefaultPieDataset();
        if (mStatistic.getMaleCount() > 0)
            dataGender.setValue(SearchFrame.GENDER_MALE, mStatistic.getMaleCount());
        if (mStatistic.getFemaleCount() > 0)
            dataGender.setValue(SearchFrame.GENDER_FEMALE, mStatistic.getFemaleCount());

        dataStage = new DefaultPieDataset();
        if (mStatistic.getMatureCount() > 0)
            dataStage.setValue(SearchFrame.STAGE_MATURE, mStatistic.getMatureCount());
        if (mStatistic.getImmatureCount() > 0)
            dataStage.setValue(SearchFrame.STAGE_IMMATURE, mStatistic.getImmatureCount());
        if (mStatistic.getUndeterminedCount() > 0)
            dataStage.setValue(SearchFrame.STAGE_UNDETERMINED, mStatistic.getUndeterminedCount());

        dataLocation = new DefaultPieDataset();
        Enumeration<String> keys = mStatistic.locationCount.keys();
        String key;
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            dataLocation.setValue(key, mStatistic.locationCount.get(key));
        }

        List<JFreeChart> charts = new LinkedList<JFreeChart>();
        charts.add(ChartFactory.createPieChart(GENDER_PIECHART_TITLE, dataGender));
        charts.add(ChartFactory.createPieChart(STAGE_PIECHART_TITLE, dataStage));
        charts.add(ChartFactory.createPieChart(LOCATION_PIECHART_TITLE, dataLocation));

        return charts;
    }

    public static class Statistic {
        public Dictionary<String, Integer> genderCount, stageCount, locationCount;

        public Statistic(Dictionary<String, Integer> gc, Dictionary<String, Integer> sc, Dictionary<String, Integer> lc) {
            genderCount = gc;
            stageCount = sc;
            locationCount = lc;
        }

        public boolean isEmpty() {
            return locationCount.size() == 0;
        }

        public int getMaleCount() {
            return genderCount.get(SearchFrame.GENDER_MALE);
        }
        public int getFemaleCount() {
            return genderCount.get(SearchFrame.GENDER_FEMALE);
        }

        public int getMatureCount() {
            return stageCount.get(SearchFrame.STAGE_MATURE);
        }
        public int getImmatureCount() {
            return stageCount.get(SearchFrame.STAGE_IMMATURE);
        }
        public int getUndeterminedCount() {
            return stageCount.get(SearchFrame.STAGE_UNDETERMINED);
        }
    }
}
