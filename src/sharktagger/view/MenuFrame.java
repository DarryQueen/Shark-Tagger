package sharktagger.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

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

    /** Internal naming constants. */
    public static final String JBSEARCH_NAME = "sharktagger.view.MenuFrame.jbSearch";
    public static final String JBFAVORITES_NAME = "sharktagger.view.MenuFrame.jbFavorites";

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    /** Instance variables. */
    private JLabel jlShark;
    private JButton jbSearch;
    private JButton jbFavorites;

private void setupUI() {
    	
    	this.setLayout(new BorderLayout());
    	
    	ImageIcon sharkIcon = new ImageIcon("shark.jpg");
    	Image sharkImage = sharkIcon.getImage();
    	Image scaledSharkImage = sharkImage.getScaledInstance(240, 240, Image.SCALE_SMOOTH);
    	sharkIcon = new ImageIcon(scaledSharkImage);
    	
        jlShark = new JLabel(sharkIcon);
        jlShark.setText("Shark Tracker");
        jlShark.setHorizontalTextPosition(JLabel.CENTER);
        jlShark.setVerticalTextPosition(JLabel.BOTTOM);    	
        this.add(jlShark, BorderLayout.CENTER);
    	
    	JPanel jpMenu = new JPanel(); 
    	jpMenu.setLayout(new GridLayout(0,1));

        jpMenu.add(jbSearch);
        jpMenu.add(jbFavorites);
        
        this.add(jpMenu, BorderLayout.SOUTH);
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
}
