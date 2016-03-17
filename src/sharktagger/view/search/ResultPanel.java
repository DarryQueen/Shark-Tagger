package sharktagger.view.search;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

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

    private String getResultText() {
        String text = "";

        text += "Name:\t\t\t\t" + mResult.name + "\n";
        text += "Gender:\t\t\t\t" + mResult.gender + "\n";
        text += "Stage of Life:\t\t\t\t" + mResult.stage + "\n";
        text += "Species:\t\t\t\t" + mResult.species + "\n";
        text += "Length:\t\t\t\t" + mResult.length + "\n";
        text += "Weight:\t\t\t\t" + mResult.weight + "\n";

        text += "\nDescription:\n\n" + mResult.description + "\n";
        text += "\nLast ping: " + mResult.lastPing;

        return text;
    }

    private void setupUI() {
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setBorder(BorderFactory.createLineBorder(Color.gray));

        JTextArea jTextArea = new JTextArea(getResultText());
        jTextArea.setLineWrap(true);
        jTextArea.setEditable(false);
        jTextArea.setBackground(new Color(0, 0, 0, 0));

        JButton jButton = new JButton(mResult.followed ? JBFOLLOW_TEXT_UNFOLLOW : JBFOLLOW_TEXT_FOLLOW);
        jButton.setName(JBFOLLOW_NAME);
        jButton.setActionCommand(mResult.name);
        jButton.addActionListener(mActionListener);

        jTextArea.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        jButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);

        this.add(jTextArea);
        this.add(Box.createHorizontalGlue());
        this.add(jButton);
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
