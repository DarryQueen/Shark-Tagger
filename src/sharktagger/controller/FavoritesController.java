package sharktagger.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;

import api.jaws.Jaws;
import api.jaws.Location;
import api.jaws.Shark;
import sharktagger.controller.favorites.Locator;
import sharktagger.model.UserPreference;
import sharktagger.view.FavoritesFrame;

public class FavoritesController implements ActionListener, UserPreference.PreferenceUpdateListener {
    public static final Location KINGS_LOCATION = new Location(51.5119, 0.1161);

    /** SearchController instance to keep when opening up favorite shark. */
    private SearchController mSearchController;

    /** UserPrference object. */
    private UserPreference mUserPreference;

    /** Jaws object. */
    private Jaws mJaws;

    /** FavoritesFrame instance representing the view. */
    private FavoritesFrame mFavoritesFrame;

    /** Refresh atomicity. */
    private Semaphore mRefreshSemaphore;

    private Locator mLocator;

    /**
     * Standard constructor.
     * @param pref UserPreference object.
     * @param sController SearchController object.
     */
    public FavoritesController(UserPreference pref, SearchController sController, Jaws jaws) {
        mSearchController = sController;

        mUserPreference = pref;

        mJaws = jaws;

        mFavoritesFrame = new FavoritesFrame(this);

        mRefreshSemaphore = new Semaphore(1);

        mLocator = new Locator();

        // Refresh the favorites panel.
        refreshFavorites();
    }

    /**
     * Open the favorites frame.
     */
    public void open() {
        mFavoritesFrame.setVisible(true);
    }

    private Runnable mRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                mRefreshSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mFavoritesFrame.setUpdating();

            List<String> favorites = mUserPreference.getFavorites();
            List<Shark> sharks = new ArrayList<Shark>();
            Dictionary<String, Location> locations = new Hashtable<String, Location>();
            Dictionary<String, Double> distances = new Hashtable<String, Double>();

            for (String sharkName : favorites) {
                Shark shark = mJaws.getShark(sharkName);
                Location location = mJaws.getLastLocation(sharkName);
                sharks.add(shark);
                locations.put(sharkName, location);
                distances.put(sharkName, getDistanceFromKings(location));
            }

            // Sort the list by distance from King's.
            Collections.sort(sharks, new Comparator<Shark>() {
                @Override
                public int compare(Shark s1, Shark s2) {
                    double d1 = distances.get(s1.getName());
                    double d2 = distances.get(s2.getName());

                    return Double.compare(d1, d2);
                }
            });

            mFavoritesFrame.clearFavorites();
            for (Shark shark : sharks) {
                boolean isOnLand = mLocator.isOnLand(locations.get(shark.getName()));
                double distance = distances.get(shark.getName());
                mFavoritesFrame.addFavorite(shark, distance, isOnLand);
            }

            mRefreshSemaphore.release();
        }
    };
    private void refreshFavorites() {
        new Thread(mRefreshRunnable).start();
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

    private static double getDistanceFromKings(Location location) {
        return calcDistance(KINGS_LOCATION, location);
    }

    /**
     * Grab distance between two Location objects in kilometers.
     * @param l1 Location object.
     * @param l2 Location object.
     * @return Double distance in kilometers.
     */
    private static double calcDistance(Location l1, Location l2) {
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

    @Override
    public void favoriteAdded(String name) {
        refreshFavorites();
    }
    @Override
    public void favoriteRemoved(String name) {
        refreshFavorites();
    }
}
