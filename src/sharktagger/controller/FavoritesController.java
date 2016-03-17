package sharktagger.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import api.jaws.Jaws;
import api.jaws.Shark;
import sharktagger.model.UserPreference;
import sharktagger.view.FavoritesFrame;

public class FavoritesController implements ActionListener, FocusListener {
    /** SearchController instance to keep when opening up favorite shark. */
    private SearchController mSearchController;

    /** UserPrference object. */
    private UserPreference mUserPreference;

    /** Jaws object. */
    private Jaws mJaws;

    /** FavoritesFrame instance representing the view. */
    private FavoritesFrame mFavoritesFrame;

    /**
     * Standard constructor.
     * @param pref UserPreference object.
     * @param sController SearchController object.
     */
    public FavoritesController(UserPreference pref, SearchController sController, Jaws jaws) {
        mSearchController = sController;

        mUserPreference = pref;

        mJaws = jaws;

        mFavoritesFrame = new FavoritesFrame(this, this);
    }

    /**
     * Open the favorites frame.
     */
    public void open() {
        refreshFavorites();
        mFavoritesFrame.setVisible(true);
    }

    private void refreshFavorites() {
        mFavoritesFrame.clearFavorites();

        List<String> favorites = mUserPreference.getFavorites();
        List<Shark> sharks = new ArrayList<Shark>();

        for (String sharkName : favorites) {
            Shark shark = mJaws.getShark(sharkName);
            sharks.add(shark);
        }

        // TODO: Sort the list by distance from King's.

        for (Shark shark : sharks) {
            mFavoritesFrame.addFavorite(shark);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component component = (Component) e.getSource();

        switch (component.getName()) {
        case FavoritesFrame.JBSHARK_NAME:
            JButton jButton = (JButton) component;
            String sharkName = jButton.getActionCommand();

            mFavoritesFrame.setVisible(false);
            mSearchController.open(sharkName);
            break;
        default:
            System.out.println("Unknown action source: " + component.getName());
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        refreshFavorites();
    }

    @Override
    public void focusLost(FocusEvent e) {
        // Do nothing.
    }
}
