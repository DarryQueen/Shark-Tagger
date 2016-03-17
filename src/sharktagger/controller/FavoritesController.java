package sharktagger.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;

import api.jaws.Jaws;
import api.jaws.Location;
import api.jaws.Shark;
import sharktagger.model.UserPreference;
import sharktagger.view.FavoritesFrame;

public class FavoritesController implements ActionListener, FocusListener {
    public static final Location KINGS_LOCATION = new Location(51.5119, 0.1161);

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

    private double getDistanceFromKings(Shark shark) {
        Location location = mJaws.getLastLocation(shark.getName());
        return calcDistance(KINGS_LOCATION, location);
    }

    private void refreshFavorites() {
        mFavoritesFrame.clearFavorites();

        List<String> favorites = mUserPreference.getFavorites();
        List<Shark> sharks = new ArrayList<Shark>();

        for (String sharkName : favorites) {
            Shark shark = mJaws.getShark(sharkName);
            sharks.add(shark);
        }

        // Sort the list by distance from King's.
        Collections.sort(sharks, new Comparator<Shark>() {
            @Override
            public int compare(Shark s1, Shark s2) {
                double d1 = getDistanceFromKings(s1);
                double d2 = getDistanceFromKings(s2);

                return Double.compare(d1, d2);
            }
        });

        for (Shark shark : sharks) {
            double distance = getDistanceFromKings(shark);
            mFavoritesFrame.addFavorite(shark, distance);
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

    /**
     * Grab distance between two Location objects in kilometers.
     * @param l1 Location object.
     * @param l2 Location object.
     * @return Double distance in kilometers.
     */
    public static double calcDistance(Location l1, Location l2) {
        double lat1 = l1.getLatitude(), lng1 = l1.getLongitude();
        double lat2 = l2.getLatitude(), lng2 = l2.getLongitude();

        double earthRadius = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius  * c;

        return dist;
    }
}
