package sharktagger.view.search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class ResultPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    /** String constants. */
    public static final String JBFOLLOW_TEXT_FOLLOW = "Follow";
    public static final String JBFOLLOW_TEXT_UNFOLLOW = "Followed";

    /** Internal naming constants. */
    public static final String JBFOLLOW_NAME = "sharktagger.view.SearchFrame.ResultPanel.jbFollow";

    /** Instance variables. */
    private ActionListener mActionListener;

    private Result mResult;

    private String getSpecText() {
        String text = "";

        text += "Name:\t\t\t\t" + mResult.name + "\n";
        text += "Gender:\t\t\t\t" + mResult.gender + "\n";
        text += "Stage of Life:\t\t\t\t" + mResult.stage + "\n";
        text += "Species:\t\t\t\t" + mResult.species + "\n";
        text += "Length:\t\t\t\t" + mResult.length + "\n";
        text += "Weight:\t\t\t\t" + mResult.weight + "\n";

        return text.trim();
    }

    private String getDescriptionText() {
        return "Description:\n\n" + mResult.description;
    }

    private String getPingText() {
        return "Last Ping: " + mResult.lastPing;
    }

    private void setupUI() {
        this.setLayout(new BorderLayout(0, 20));

        Border outline = BorderFactory.createLineBorder(Color.GRAY);
        Border margin = BorderFactory.createEmptyBorder(8, 8, 8, 8);
        this.setBorder(BorderFactory.createCompoundBorder(outline, margin));

        JTextArea jtaSpecs = new JTextArea(getSpecText());
        jtaSpecs.setEditable(false);
        jtaSpecs.setBackground(new Color(0, 0, 0, 0));

        JTextArea jtaDescription = new JTextArea(getDescriptionText());
        jtaDescription.setLineWrap(true);
        jtaDescription.setEditable(false);
        jtaDescription.setBackground(new Color(0, 0, 0, 0));

        JTextArea jtaPing = new JTextArea(getPingText());
        jtaPing.setEditable(false);
        jtaPing.setBackground(new Color(0, 0, 0, 0));

        JButton jButton = new JButton(mResult.followed ? JBFOLLOW_TEXT_UNFOLLOW : JBFOLLOW_TEXT_FOLLOW);
        jButton.setName(JBFOLLOW_NAME);
        jButton.setActionCommand(mResult.name);
        jButton.addActionListener(mActionListener);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(jtaPing, BorderLayout.WEST);
        bottomPanel.add(jButton, BorderLayout.EAST);

        this.add(jtaSpecs, BorderLayout.NORTH);
        this.add(jtaDescription, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    public ResultPanel(ActionListener listener, Result result) {
        super();

        mActionListener = listener;

        mResult = result;

        setupUI();
    }

    public void toggleFollow() {
        mResult.followed = !mResult.followed;

        this.removeAll();
        setupUI();
        repaint(); revalidate();
    }

    /**
     * Objects of this class are not tightly bound to the preference model.
     * Therefore, values of the fields may no longer be consistent with actual values after objects are constructed.
     */
    public static class Result {
        public String name, gender, stage, species, length, weight;
        public String description;
        public String lastPing;

        public boolean followed;

        public Result(String n, String g, String st, String sp, String l, String w, String d, String p, boolean f) {
            name = n;
            gender = g;
            stage = st;
            species = sp;
            length = l;
            weight = w;
            description = d;
            lastPing = p;

            followed = f;
        }
    }
}
