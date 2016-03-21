package sharktagger.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import api.jaws.Shark;

public class FavoritesFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    /** String constants. */
    public static final String TITLE = "Favorites";
    public static final String HEADER_TEXT = "Your favourite sharks are this far away from you right now:";
    public static final String UPDATING_TEXT = "Updating favourites...";

    /** Internal naming constants. */
    public static final String JBSHARK_NAME = "sharktagger.view.FavoritesFrame.jbShark";

    public static final int WIDTH = 600;
    public static final int HEIGHT = 300;

    /** Instance variables. */
    private ActionListener mActionListener;

    private JPanel jpFavorites;

    private void setupUI() {
        this.setLayout(new BorderLayout());

        JLabel header = new JLabel(HEADER_TEXT);

        jpFavorites.setLayout(new BoxLayout(jpFavorites, BoxLayout.PAGE_AXIS));
        jpFavorites.setBackground(Color.WHITE);

        this.add(header, BorderLayout.NORTH);
        this.add(jpFavorites, BorderLayout.CENTER);
    }

    public FavoritesFrame(ActionListener actionListener) {
        super(TITLE);
        this.setSize(WIDTH, HEIGHT);

        mActionListener = actionListener;

        jpFavorites = new JPanel();

        setupUI();
    }

    private String getSharkText(Shark shark, double distance) {
        String sharkText = shark.getName() + ": " + distance + " km.";

        return sharkText;
    }

    public void addFavorite(Shark shark, double distance) {
        String sharkText = getSharkText(shark, distance);
        JButton jButton = new JButton(sharkText);
        jButton.setMargin(new Insets(0, 0, 0, 0));
        jButton.setContentAreaFilled(false);
        jButton.setBorderPainted(false);
        jButton.setOpaque(false);

        jButton.setName(JBSHARK_NAME);
        jButton.setActionCommand(shark.getName());
        jButton.addActionListener(mActionListener);

        jpFavorites.add(jButton);
        repaint(); revalidate();
    }

    public void clearFavorites() {
        jpFavorites.removeAll();
        repaint(); revalidate();
    }

    public void setUpdating() {
        jpFavorites.removeAll();

        JLabel jlUpdating = new JLabel(UPDATING_TEXT);
        jpFavorites.add(jlUpdating);

        repaint(); revalidate();
    }
}
