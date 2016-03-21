package sharktagger.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class MenuFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    /** String constants. */
    public static final String TITLE = "Shark Tagger";
    public static final String JBSEARCH_TEXT = "Search";
    public static final String JBFAVORITES_TEXT = "Favorites";

    public static final String LOGO_PATH = "shark.jpg";
    public static final int LOGO_IMAGELENGTH = 180;

    /** Internal naming constants. */
    public static final String JBSEARCH_NAME = "sharktagger.view.MenuFrame.jbSearch";
    public static final String JBFAVORITES_NAME = "sharktagger.view.MenuFrame.jbFavorites";

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    /** Instance variables. */
    private JButton jbSearch;
    private JButton jbFavorites;

    private void setupUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        ImageIcon sharkIcon = new ImageIcon(LOGO_PATH);
        Image sharkImage = sharkIcon.getImage();
        Image scaledSharkImage = sharkImage.getScaledInstance(LOGO_IMAGELENGTH, LOGO_IMAGELENGTH, Image.SCALE_SMOOTH);
        sharkIcon = new ImageIcon(scaledSharkImage);

        JLabel jlShark = new JLabel(sharkIcon);
        jlShark.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jpMenu = new JPanel();
        jpMenu.setLayout(new GridLayout(0, 1));
        jpMenu.add(jbSearch);
        jpMenu.add(jbFavorites);

        mainPanel.add(jlShark);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 60)));
        mainPanel.add(jpMenu);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        this.add(mainPanel);
    }

    public MenuFrame(ActionListener actionListener, WindowListener windowListener) {
        super(TITLE);
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Set window listener.
        this.addWindowListener(windowListener);

        jbSearch = new JButton(JBSEARCH_TEXT);
        jbFavorites = new JButton(JBFAVORITES_TEXT);

        // Set names and action listener.
        jbSearch.setName(JBSEARCH_NAME);
        jbFavorites.setName(JBFAVORITES_NAME);
        jbSearch.addActionListener(actionListener);
        jbFavorites.addActionListener(actionListener);

        setupUI();
    }

    public void setFavoritesButtonEnabled(boolean enable) {
        jbFavorites.setEnabled(enable);
    }
}
