package sharktagger.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
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
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import api.jaws.Jaws;
import api.jaws.Ping;
import api.jaws.Shark;
import sharktagger.controller.SearchController;
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
    private static final String[] RANGE_OPTIONS = { RANGE_DAY, RANGE_WEEK, RANGE_MONTH };

    public static final String GENDER_MALE = "Male";
    public static final String GENDER_FEMALE = "Female";
    private static final String[] GENDER_OPTIONS = { OPTION_ALL, GENDER_MALE, GENDER_FEMALE };

    public static final String STAGE_MATURE = "Mature";
    public static final String STAGE_IMMATURE = "Immature";
    public static final String STAGE_UNDETERMINED = "Undetermined";
    private static final String[] STAGE_OPTIONS = { OPTION_ALL, STAGE_MATURE, STAGE_IMMATURE, STAGE_UNDETERMINED };

    public static final String LOGO_PATH = "shark.jpg";
    public static final String LOGO_TITLE = "Shark Tracker";
    public static final String LOGO_FONT = "Arial";
    private static final String RANGE_TITLE = "Tracking Range";
    private static final String GENDER_TITLE = "Gender";
    private static final String LIFE_STAGE_TITLE = "Stage of Life";
    private static final String TAG_LOCATION_TITLE = "Tag Location";

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
    private JLabel jlShark;
    private JLabel jlSharkTrackerTitle;
    private JLabel jlTrackingRange;
    private JLabel jlGender;
    private JLabel jlStage;
    private JLabel jlLocation;
    private JLabel jlAcknowledgement;

    private JPanel jpResults;
    private JPanel contentPane;

    private void setupUI() {
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(new BorderLayout());

        // Left side query.
        JPanel optionsPane = new JPanel();
        optionsPane.setLayout(new BorderLayout());

        JPanel queryPanel = new JPanel();
        queryPanel.setLayout(new GridLayout(0, 1));

        jlSharkTrackerTitle = new JLabel(LOGO_TITLE);
        jlTrackingRange = new JLabel(RANGE_TITLE);
        jlGender = new JLabel(GENDER_TITLE);
        jlStage = new JLabel(LIFE_STAGE_TITLE);
        jlLocation = new JLabel(TAG_LOCATION_TITLE);

        queryPanel.add(jlSharkTrackerTitle);
        queryPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        queryPanel.add(jlTrackingRange);
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

        optionsPane.add(queryPanel, BorderLayout.NORTH);
        optionsPane.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        ImageIcon sharkIcon = new ImageIcon(LOGO_PATH);
        Image sharkImage = sharkIcon.getImage();
        Image scaledSharkImage = sharkImage.getScaledInstance(240, 240, Image.SCALE_SMOOTH);
        sharkIcon = new ImageIcon(scaledSharkImage);

        jlShark = new JLabel(sharkIcon);
        jlShark.setText(LOGO_TITLE);
        jlShark.setFont(new Font(LOGO_FONT, Font.BOLD, 32));
        jlShark.setHorizontalTextPosition(JLabel.CENTER);
        jlShark.setVerticalTextPosition(JLabel.BOTTOM);

        optionsPane.add(jlShark, BorderLayout.SOUTH);

        // Right side search results.
        jpResults.setLayout(new BoxLayout(jpResults, BoxLayout.PAGE_AXIS));
        jpResults.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane resultsScrollPane = new JScrollPane(jpResults, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // JSplitPane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(DIVIDER_LOCATION);
        splitPane.setLeftComponent(optionsPane);
        splitPane.setRightComponent(resultsScrollPane);
        splitPane.setEnabled(false);
        splitPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        contentPane.add(splitPane, BorderLayout.CENTER);

        // Acknowledgement.
        jlAcknowledgement = new JLabel(((SearchController) mActionListener).getMJaws().getAcknowledgement());
        jlAcknowledgement.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        this.add(jlAcknowledgement, BorderLayout.SOUTH);

        contentPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
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
        ResultPanel.Result result = new ResultPanel.Result(shark.getName(), shark.getGender(), shark.getStageOfLife(),
                shark.getSpecies(), shark.getLength(), shark.getWeight(), shark.getDescription(), lastPing, followed);
        ResultPanel resultPanel = new ResultPanel(mActionListener, result);
        jpResults.add(resultPanel);
        repaint();
        revalidate();
    }

    public void clearResults() {
        jpResults.removeAll();
        repaint();
        revalidate();
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
