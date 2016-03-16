package sharktagger.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import api.jaws.Jaws;
import api.jaws.Shark;
import sharktagger.model.UserPreference;
import sharktagger.view.SearchFrame;

public class SearchController implements ActionListener {
    /** Jaws object. */
    private Jaws mJaws;

    /** SearchFrame instance representing the view. */
    private SearchFrame mSearchFrame;

    /**
     * Standard constructor.
     * @param pref UserPreference object.
     */
    public SearchController(UserPreference pref, Jaws jaws) {
        mJaws = jaws;
        List<String> locations = jaws.getTagLocations();

        mSearchFrame = new SearchFrame(this, locations);
    }

    /**
     * Open the search frame.
     */
    public void open() {
        mSearchFrame.setVisible(true);
    }

    /**
     * Open the search frame and load the results with the given shark.
     * @param shark Shark to display in the results.
     */
    public void open(Shark shark) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
