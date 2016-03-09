package sharktagger.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sharktagger.model.UserPreference;

public class MenuController implements ActionListener {
    /** SearchController instance for opening up search. */
    private SearchController mSearchController;
    /** FavoritesController instance for opening up favorites */
    private FavoritesController mFavoritesController;

    /**
     * Standard constructor.
     * @param pref UserPreference object.
     * @param sController SearchController object.
     * @param fController FavoritesController object.
     */
    public MenuController(UserPreference pref, SearchController sController, FavoritesController fController) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
