package sharktagger.view;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import api.jaws.Ping;
import api.jaws.Shark;
import sharktagger.view.search.ResultPanel;

public class SearchFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    /** String constants. */
    public static final String TITLE = "Search";
    public static final String JBSEARCH_TEXT = "Search";

    /** Internal naming constants. */
    public static final String JBSEARCH_NAME = "sharktagger.view.SearchFrame.jbSearch";

    /** Dropdown constants. */
    public static final String OPTION_ALL = "All";

    public static final String RANGE_DAY = "Last 24 Hours";
    public static final String RANGE_WEEK = "Last Week";
    public static final String RANGE_MONTH = "Last Month";
    private static final String[] RANGE_OPTIONS = {RANGE_DAY, RANGE_WEEK, RANGE_MONTH};

    public static final String GENDER_MALE = "Male";
    public static final String GENDER_FEMALE = "Female";
    private static final String[] GENDER_OPTIONS = {OPTION_ALL, GENDER_MALE, GENDER_FEMALE};

    public static final String STAGE_MATURE = "Mature";
    public static final String STAGE_IMMATURE = "Immature";
    public static final String STAGE_UNDETERMINED = "Undetermined";
    private static final String[] STAGE_OPTIONS = {OPTION_ALL, STAGE_MATURE, STAGE_IMMATURE, STAGE_UNDETERMINED};

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 1000;
    public static final int DIVIDER_LOCATION = 300;

    /** Instance variables. */
    private ActionListener mActionListener;

    private JComboBox<String> jcbRange;
    private JComboBox<String> jcbGender;
    private JComboBox<String> jcbStage;
    private JComboBox<String> jcbLocation;
    private JButton jbSearch;

    private JPanel jpResults;

    private void setupUI() {
        // Left side query.
        JPanel queryPanel = new JPanel();

        queryPanel.add(jcbRange);
        queryPanel.add(jcbGender);
        queryPanel.add(jcbStage);
        queryPanel.add(jcbLocation);
        queryPanel.add(jbSearch);

        // Right side search results.
        jpResults.setLayout(new BoxLayout(jpResults, BoxLayout.PAGE_AXIS));
        JScrollPane resultsScrollPane = new JScrollPane(jpResults, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // JSplitPane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(DIVIDER_LOCATION);
        splitPane.setLeftComponent(queryPanel);
        splitPane.setRightComponent(resultsScrollPane);
        splitPane.setEnabled(false);

        this.add(splitPane);
    }

    public SearchFrame(ActionListener listener, List<String> locations) {
        super(TITLE);
        this.setSize(WIDTH, HEIGHT);

        locations.add(0, OPTION_ALL);
        String[] locationsArray = new String[locations.size()];

        mActionListener = listener;

        jcbRange = new JComboBox<String>(RANGE_OPTIONS);
        jcbGender = new JComboBox<String>(GENDER_OPTIONS);
        jcbStage = new JComboBox<String>(STAGE_OPTIONS);
        jcbLocation = new JComboBox<String>(locations.toArray(locationsArray));
        jbSearch = new JButton(JBSEARCH_TEXT);

        // Set names and action listener.
        jbSearch.setName(JBSEARCH_NAME);
        jbSearch.addActionListener(listener);

        jpResults = new JPanel();

        setupUI();
    }

    public void addResult(Shark shark, Ping ping, boolean followed) {
        String lastPing = ping != null ? ping.getTime() : "Unknown";
        ResultPanel.Result result = new ResultPanel.Result(shark.getName(), shark.getGender(), shark.getStageOfLife(), shark.getSpecies(), shark.getLength(), shark.getWeight(), shark.getDescription(), lastPing, followed);
        ResultPanel resultPanel = new ResultPanel(mActionListener, result);
        jpResults.add(resultPanel);
        repaint(); revalidate();
    }

    public void clearResults() {
        jpResults.removeAll();
        repaint(); revalidate();
    }

    public Query getQuery() {
        String range = (String) jcbRange.getSelectedItem();
        String gender = (String) jcbGender.getSelectedItem();
        String stage = (String) jcbStage.getSelectedItem();
        String location = (String) jcbLocation.getSelectedItem();
        return new Query(range, gender, stage, location);
    }

    public static class Query {
        public String range, gender, stage, location;

        public Query(String r, String g, String s, String l) {
            range = r;
            gender = g;
            stage = s;
            location = l;
        }
    }
}
