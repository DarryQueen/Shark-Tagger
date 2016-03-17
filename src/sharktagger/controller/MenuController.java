package sharktagger.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sharktagger.model.UserPreference;
import sharktagger.view.MenuFrame;

public class MenuController implements ActionListener {
    /** SearchController instance for opening up search. */
    private SearchController mSearchController;
    /** FavoritesController instance for opening up favorites */
    private FavoritesController mFavoritesController;

    /** MenuFrame instance representing the view. */
    MenuFrame mMenuFrame;

    /**
     * Standard constructor.
     * @param pref UserPreference object.
     * @param sController SearchController object.
     * @param fController FavoritesController object.
     */
    public MenuController(UserPreference pref, SearchController sController, FavoritesController fController) {
        mSearchController = sController;
        mFavoritesController = fController;

        mMenuFrame = new MenuFrame(this);
    }

    /**
     * Open the menu frame.
     */
    public void open() {
        mMenuFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component component = (Component) e.getSource();

        switch (component.getName()) {
        case MenuFrame.JBSEARCH_NAME:
            mSearchController.open();
            break;
        case MenuFrame.JBFAVORITES_NAME:
            mFavoritesController.open();
            break;
        default:
            System.out.println("Unknown action source: " + component.getName());
        }
    }
}
