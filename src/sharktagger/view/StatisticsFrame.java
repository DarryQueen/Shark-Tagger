package sharktagger.view;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import api.jaws.Shark;

public class StatisticsFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    /** String constants. */
    public static final String TITLE = "Statistics";

    /** Pie Chart Title */
    public static final String GENDER_PIECHART_TITLE = "Gender";
    public static final String STAGE_PIECHART_TITLE = "Stage of Life";
    public static final String LOCATION_PIECHART_TITLE = "Tag Location";

    /** Empty Message */
    private static final String STATISTICS_EMPTY_MESSAGE = "No sharks are found with selected option set.";

    public static final int WIDTH = 500;
    public static final int HEIGHT = 1000;

    /** Instance variables. */
    private JFreeChart chartGender;
    private DefaultPieDataset dataGender;
    private ChartPanel chpGender;
    private JFreeChart chartStage;
    private DefaultPieDataset dataStage;
    private ChartPanel chpStage;
    private JFreeChart chartLocation;
    private DefaultPieDataset dataLocation;
    private ChartPanel chpLocation;

    private int maleCount;
    private int femaleCount;
    private int matureCount;
    private int immatureCount;
    private int undeterminedCount;
    HashMap<String, Integer> locationMap;

    public StatisticsFrame(int maleCount, int femaleCount, int matureCount, int immatureCount, int undeterminedCount,
            HashMap<String, Integer> locationMap) {
        super(TITLE);
        this.setSize(WIDTH, HEIGHT);

        this.maleCount = maleCount;
        this.femaleCount = femaleCount;
        this.matureCount = matureCount;
        this.immatureCount = immatureCount;
        this.undeterminedCount = undeterminedCount;
        this.locationMap = locationMap;

        setupUI();
    }

    public void setupUI() {
        if (locationMap.size() > 0) {
            this.setLayout(new GridLayout(3, 1));
            createDataset();
            createChart();
            chpGender = new ChartPanel(chartGender);
            chpStage = new ChartPanel(chartStage);
            chpLocation = new ChartPanel(chartLocation);
            this.add(chpGender);
            this.add(chpStage);
            this.add(chpLocation);
        } else {
            JLabel emptyMessage = new JLabel(STATISTICS_EMPTY_MESSAGE);
            emptyMessage.setHorizontalAlignment(JLabel.CENTER);
            this.add(emptyMessage);
        }
    }

    private void createDataset() {
        dataGender = new DefaultPieDataset();
        if (maleCount > 0)
            dataGender.setValue(SearchFrame.GENDER_MALE, maleCount);
        if (femaleCount > 0)
            dataGender.setValue(SearchFrame.GENDER_FEMALE, femaleCount);

        dataStage = new DefaultPieDataset();
        if (matureCount > 0)
            dataStage.setValue(SearchFrame.STAGE_MATURE, matureCount);
        if (immatureCount > 0)
            dataStage.setValue(SearchFrame.STAGE_IMMATURE, immatureCount);
        if (undeterminedCount > 0)
            dataStage.setValue(SearchFrame.STAGE_UNDETERMINED, undeterminedCount);

        dataLocation = new DefaultPieDataset();
        for (Entry<String, Integer> entry : locationMap.entrySet()) {
            dataLocation.setValue(entry.getKey(), entry.getValue());
        }
    }

    private void createChart() {
        chartGender = ChartFactory.createPieChart(GENDER_PIECHART_TITLE, dataGender);
        chartStage = ChartFactory.createPieChart(STAGE_PIECHART_TITLE, dataStage);
        chartLocation = ChartFactory.createPieChart(LOCATION_PIECHART_TITLE, dataLocation);
    }

    public void addResult(List<Shark> sharkList) {
        for (Shark s : sharkList) {
            if (s.getGender().equals(SearchFrame.STAGE_MATURE)) {
                maleCount++;
            } else {
                femaleCount++;
            }

            if (s.getStageOfLife().equals(SearchFrame.STAGE_MATURE)) {
                matureCount++;
            } else if (s.getStageOfLife().equals(SearchFrame.STAGE_IMMATURE)) {
                immatureCount++;
            } else {
                undeterminedCount++;
            }

            String location = s.getTagLocation();
            if (locationMap.containsKey(location)) {
                locationMap.replace(location, (locationMap.get(location) + 1));
            } else {
                locationMap.put(location, 1);
            }
        }
    }

}
