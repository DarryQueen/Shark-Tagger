package sharktagger.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import sharktagger.Main;
import sharktagger.model.UserPreference;
import sharktagger.view.MenuFrame;

public class MenuController implements ActionListener, WindowListener, UserPreference.PreferenceUpdateListener {
    /** SearchController instance for opening up search. */
    private SearchController mSearchController;
    /** FavoritesController instance for opening up favorites */
    private FavoritesController mFavoritesController;

    /** UserPrference object. */
    private UserPreference mUserPreference;

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

        mUserPreference = pref;

        mMenuFrame = new MenuFrame(this, this);
        mMenuFrame.setFavoritesButtonEnabled(mUserPreference.hasFavorites());
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

    @Override
    public void windowClosing(WindowEvent e) {
        mUserPreference.saveToFile(Main.PREFERENCES_FILENAME);
    }

    @Override
    public void favoriteAdded(String name) {
        mMenuFrame.setFavoritesButtonEnabled(true);
    }
    @Override
    public void favoriteRemoved(String name) {
        mMenuFrame.setFavoritesButtonEnabled(mUserPreference.hasFavorites());
    }

    /**
     * Ignore these WindowListener methods, because we don't use them.
     */
    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}
}
