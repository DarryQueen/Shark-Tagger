package sharktagger.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
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
	public static final String LOGO_TITLE = "Shark Tracker";
    public static final String LOGO_FONT = "Arial";

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

		ImageIcon sharkIcon = new ImageIcon(LOGO_PATH);
		Image sharkImage = sharkIcon.getImage();
		Image scaledSharkImage = sharkImage.getScaledInstance(240, 240, Image.SCALE_SMOOTH);
		sharkIcon = new ImageIcon(scaledSharkImage);

		jlShark = new JLabel(sharkIcon);
		jlShark.setText(LOGO_TITLE);
		jlShark.setFont(new Font(LOGO_FONT, Font.BOLD, 32));		
		jlShark.setHorizontalTextPosition(JLabel.CENTER);
		jlShark.setVerticalTextPosition(JLabel.BOTTOM);
		jlShark.setBorder(BorderFactory.createEmptyBorder(20, 10, 5, 10));
		this.add(jlShark, BorderLayout.NORTH);

		JPanel jpMenu = new JPanel();
		jpMenu.setLayout(new GridLayout(0, 1));
		jpMenu.add(jbSearch);
		jpMenu.add(jbFavorites);
	    jpMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		this.add(jpMenu, BorderLayout.CENTER);
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
