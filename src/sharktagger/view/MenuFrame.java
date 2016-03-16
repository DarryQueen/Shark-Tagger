package sharktagger.view;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class MenuFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    /** String constants. */
    public static final String TITLE = "Shark Tagger";
    public static final String JBSEARCH_TEXT = "Search";
    public static final String JBFAVORITES_TEXT = "Favorites";

    /** Internal naming constants. */
    public static final String JBSEARCH_NAME = "sharktagger.view.MenuFrame.jbSearch";
    public static final String JBFAVORITES_NAME = "sharktagger.view.MenuFrame.jbFavorites";

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    /** Instance variables. */
    private JButton jbSearch;
    private JButton jbFavorites;

    private void setupUI() {
        this.setLayout(new GridLayout(0, 1));

        this.add(jbSearch);
        this.add(jbFavorites);
    }

    public MenuFrame(ActionListener listener) {
        super(TITLE);
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jbSearch = new JButton(JBSEARCH_TEXT);
        jbFavorites = new JButton(JBFAVORITES_TEXT);

        // Set names and action listener.
        jbSearch.setName(JBSEARCH_NAME);
        jbFavorites.setName(JBFAVORITES_NAME);
        jbSearch.addActionListener(listener);
        jbFavorites.addActionListener(listener);

        setupUI();
    }
}
