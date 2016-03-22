package sharktagger.view.search;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SharkOfTheDayPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    /** String constants. */
    public static final String HEADER_TEXT = "Shark of the Day";
    public static final String FILLER_LINK_TEXT = "No link available";

    /** Internal naming constants. */
    public static final String JBSOTD_NAME = "sharktagger.view.SearchFrame.jbSotd";

    /** Instance variables. */
    private ActionListener mActionListener;

    private String mName;
    private String mLink;

    private void setupUI() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        // Create labels.
        JLabel jlHeader = new JLabel(HEADER_TEXT);
        JLabel jlSotd = new JLabel(mName.trim());
        JButton jbLink = new JButton(mLink == null ? FILLER_LINK_TEXT : mLink);

        // Style labels.
        jlHeader.setFont(jlHeader.getFont().deriveFont(30.0f));
        jlSotd.setFont(jlSotd.getFont().deriveFont(80.0f));

        jbLink.setMargin(new Insets(0, 0, 0, 0));
        jbLink.setContentAreaFilled(false);
        jbLink.setBorderPainted(false);
        jbLink.setOpaque(false);

        if (mLink != null) {
            jbLink.setForeground(Color.BLUE);
        }

        jbLink.setName(JBSOTD_NAME);
        jbLink.setActionCommand(mLink);
        jbLink.addActionListener(mActionListener);

        // Center labels.
        this.add(Box.createVerticalGlue());

        Component[] labels = {jlHeader, jlSotd, Box.createRigidArea(new Dimension(0, 30)), jbLink};
        for (Component label : labels) {
            JPanel horizontalPanel = new JPanel();
            horizontalPanel.setLayout(new BoxLayout(horizontalPanel, BoxLayout.LINE_AXIS));
            horizontalPanel.add(Box.createHorizontalGlue());
            horizontalPanel.add(label);
            horizontalPanel.add(Box.createHorizontalGlue());
            this.add(horizontalPanel);
        }

        this.add(Box.createVerticalGlue());
    }

    public SharkOfTheDayPanel(ActionListener listener, String name, String videoLink) {
        super();

        mActionListener = listener;

        mName = name;
        mLink = videoLink;

        setupUI();
    }
}
