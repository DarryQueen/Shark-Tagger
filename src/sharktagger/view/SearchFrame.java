package sharktagger.view;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;

public class SearchFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    /** String constants. */
    public static final String TITLE = "Search";
    public static final String JBSEARCH_TEXT = "Search";

    /** Internal naming constants. */
    public static final String JBSEARCH_NAME = "sharktagger.view.SearchFrame.jbSearch";

    /** Dropdown constants. */
    private static final String OPTION_ALL = "All";

    private static final String RANGE_DAY = "Last 24 Hours";
    private static final String RANGE_WEEK = "Last Week";
    private static final String RANGE_MONTH = "Last Month";
    private static final String[] RANGE_OPTIONS = {RANGE_DAY, RANGE_WEEK, RANGE_MONTH};

    private static final String GENDER_MALE = "Male";
    private static final String GENDER_FEMALE = "Female";
    private static final String[] GENDER_OPTIONS = {OPTION_ALL, GENDER_MALE, GENDER_FEMALE};

    private static final String STAGE_MATURE = "Mature";
    private static final String STAGE_IMMATURE = "Immature";
    private static final String STAGE_UNDETERMINED = "Undetermined";
    private static final String[] STAGE_OPTIONS = {OPTION_ALL, STAGE_MATURE, STAGE_IMMATURE, STAGE_UNDETERMINED};

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    private static final int DIVIDER_LOCATION = 300;

    /** Instance variables. */
    private ActionListener mActionListener;

    private JComboBox<String> jcbRange;
    private JComboBox<String> jcbGender;
    private JComboBox<String> jcbStage;
    private JComboBox<String> jcbLocation;
    private JButton jbSearch;

    private JTextPane jtpResults;

    private void setupUI() {
        // Left side query.
        JPanel queryPanel = new JPanel();

        queryPanel.add(jcbRange);
        queryPanel.add(jcbGender);
        queryPanel.add(jcbStage);
        queryPanel.add(jcbLocation);
        queryPanel.add(jbSearch);

        // Right side search results.
        JPanel resultsPanel = new JPanel();
        resultsPanel.add(jtpResults);
        JScrollPane resultsScrollPane = new JScrollPane(resultsPanel);

        // TODO: Setup the JTextPane.

        // JSplitPane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(DIVIDER_LOCATION);
        splitPane.setLeftComponent(queryPanel);
        splitPane.setRightComponent(resultsScrollPane);

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

        jtpResults = new JTextPane();

        setupUI();
    }
}
