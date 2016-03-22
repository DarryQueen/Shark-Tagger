package sharktagger.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import api.jaws.Ping;
import api.jaws.Shark;
import sharktagger.view.search.ResultPanel;
import sharktagger.view.search.SharkOfTheDayPanel;

public class SearchFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    /** String constants. */
    public static final String TITLE = "Search";
    public static final String JBSEARCH_TEXT = "Search";

    public static final String DROPDOWN_TITLE = "Shark Tracker";
    public static final String RANGE_DROPDOWN_TITLE = "Tracking Range";
    public static final String GENDER_DROPDOWN_TITLE = "Gender";
    public static final String STAGE_DROPDOWN_TITLE = "Stage of Life";
    public static final String LOCATION_DROPDOWN_TITLE = "Tag Location";

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

    private String mAcknowledgement;

    private void setupUI() {
        this.setLayout(new BorderLayout());

        // Left side query.
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BorderLayout());

        JPanel queryPanel = new JPanel();
        queryPanel.setLayout(new GridLayout(0, 1));

        JLabel jlDropdown = new JLabel(DROPDOWN_TITLE);
        JLabel jlRange = new JLabel(RANGE_DROPDOWN_TITLE);
        JLabel jlGender = new JLabel(GENDER_DROPDOWN_TITLE);
        JLabel jlStage = new JLabel(STAGE_DROPDOWN_TITLE);
        JLabel jlLocation = new JLabel(LOCATION_DROPDOWN_TITLE);

        queryPanel.add(jlDropdown);
        queryPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        queryPanel.add(jlRange);
        queryPanel.add(jcbRange);
        queryPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        queryPanel.add(jlGender);
        queryPanel.add(jcbGender);
        queryPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        queryPanel.add(jlStage);
        queryPanel.add(jcbStage);
        queryPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        queryPanel.add(jlLocation);
        queryPanel.add(jcbLocation);
        queryPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        queryPanel.add(jbSearch);

        optionsPanel.add(queryPanel, BorderLayout.NORTH);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        ImageIcon sharkIcon = new ImageIcon(MenuFrame.LOGO_PATH);
        Image sharkImage = sharkIcon.getImage();
        Image scaledSharkImage = sharkImage.getScaledInstance(240, 240, Image.SCALE_SMOOTH);
        sharkIcon = new ImageIcon(scaledSharkImage);

        JLabel jlShark = new JLabel(sharkIcon);
        optionsPanel.add(jlShark, BorderLayout.SOUTH);

        // Right side search results.
        jpResults.setLayout(new BoxLayout(jpResults, BoxLayout.PAGE_AXIS));
        jpResults.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane resultsScrollPane = new JScrollPane(jpResults, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // JSplitPane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(DIVIDER_LOCATION);
        splitPane.setLeftComponent(optionsPanel);
        splitPane.setRightComponent(resultsScrollPane);
        splitPane.setEnabled(false);
        splitPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        this.add(splitPane, BorderLayout.CENTER);

        // Acknowledgement.
        JLabel jlAcknowledgement = new JLabel(mAcknowledgement);
        jlAcknowledgement.setForeground(Color.GRAY);
        jlAcknowledgement.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        this.add(jlAcknowledgement, BorderLayout.SOUTH);

        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));
    }

    public SearchFrame(ActionListener listener, List<String> locations, String acknowledgement) {
        super(TITLE);
        this.setSize(WIDTH, HEIGHT);

        locations.add(0, OPTION_ALL);
        String[] locationsArray = new String[locations.size()];

        mActionListener = listener;
        mAcknowledgement = acknowledgement;

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

    public void setSharkOfTheDay(String name, String videoLink) {
        jpResults.removeAll();
        jpResults.add(new SharkOfTheDayPanel(mActionListener, name, videoLink));
        repaint(); revalidate();
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
