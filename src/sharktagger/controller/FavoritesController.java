package sharktagger.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sharktagger.model.UserPreference;

public class FavoritesController implements ActionListener {
    /** SearchController instance to keep when opening up favorite shark. */
    private SearchController mSearchController;

    /**
     * Standard constructor.
     * @param pref UserPreference object.
     * @param sController SearchController object.
     */
    public FavoritesController(UserPreference pref, SearchController sController) {
    }

    /**
     * Open the favorites frame.
     */
    public void open() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
