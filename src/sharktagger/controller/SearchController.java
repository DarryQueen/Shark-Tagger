package sharktagger.controller;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;

import api.jaws.Jaws;
import api.jaws.Ping;
import api.jaws.Shark;
import sharktagger.controller.search.SharkOfTheDay;
import sharktagger.model.UserPreference;
import sharktagger.view.SearchFrame;
import sharktagger.view.search.ResultPanel;
import sharktagger.view.search.SharkOfTheDayPanel;

public class SearchController implements ActionListener {
    public static final String INVALID_VIDEO_LINK = "No footage available for this shark :-(";

    /** UserPrference object. */
    private UserPreference mUserPreference;

    /** Jaws object. */
    private Jaws mJaws;

    /** SearchFrame instance representing the view. */
    private SearchFrame mSearchFrame;

    /** Shark of the day. */
    private Shark mSharkOfTheDay;
    private String mSharkVideoLink;

    /**
     * Standard constructor.
     * @param pref UserPreference object.
     */
    public SearchController(UserPreference pref, Jaws jaws) {
        mUserPreference = pref;

        mJaws = jaws;
        List<String> locations = jaws.getTagLocations();
        String acknowledgement = jaws.getAcknowledgement();

        mSharkOfTheDay = new SharkOfTheDay(jaws, pref.getLastUpdated()).getSharkOfTheDay();

        mSharkVideoLink = null;
        String link = mJaws.getVideo(mSharkOfTheDay.getName());
        if (!link.equals(INVALID_VIDEO_LINK)) {
            mSharkVideoLink = link;
        }

        mSearchFrame = new SearchFrame(this, locations, acknowledgement);
    }

    /**
     * Open the search frame to the Shark of the Day.
     */
    public void open() {
        mSearchFrame.setSharkOfTheDay(mSharkOfTheDay.getName(), mSharkVideoLink);

        mSearchFrame.setVisible(true);
    }

    /**
     * Open the search frame and load the results with the given shark.
     * @param sharkName String name of shark to display in result.
     */
    public void open(String sharkName) {
        Shark shark = mJaws.getShark(sharkName);

        mSearchFrame.clearResults();
        mSearchFrame.addResult(shark, null, mUserPreference.isFavorite(sharkName));

        open();
    }

    private List<Shark> performQuery(SearchFrame.Query query) {
        // Initialize an empty list.
        List<Shark> sharks = new LinkedList<Shark>();
        List<Ping> pings = new ArrayList<Ping>();

        // Time query.
        switch (query.range) {
        case SearchFrame.RANGE_DAY:
            pings = mJaws.past24Hours();
            break;
        case SearchFrame.RANGE_WEEK:
            pings = mJaws.pastWeek();
            break;
        case SearchFrame.RANGE_MONTH:
            pings = mJaws.pastMonth();
            break;
        default:
            System.out.println("Unknown time range: " + query.range);
        }

        // Sort pings, latest first.
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Collections.sort(pings, new Comparator<Ping>() {
            @Override
            public int compare(Ping p1, Ping p2) {
                Date date1 = null, date2 = null;
                try {
                    date1 = formatter.parse(p1.getTime());
                    date2 = formatter.parse(p2.getTime());
                } catch (Exception e) {
                    // I really don't care.
                    e.printStackTrace();
                }
                return -1 * date1.compareTo(date2);
            }
        });

        // Filter.
        Set<String> seenNames = new HashSet<String>();
        for (Ping ping : pings) {
            Shark shark = mJaws.getShark(ping.getName());

            // Dropdown strings just happen to match, so we get lucky. This is hackish.
            boolean genderMatch = query.gender == SearchFrame.OPTION_ALL || query.gender.equals(shark.getGender());
            boolean stageMatch = query.stage == SearchFrame.OPTION_ALL || query.stage.equals(shark.getStageOfLife());
            boolean locationMatch = query.location == SearchFrame.OPTION_ALL || query.location.equals(shark.getTagLocation());
            boolean newShark = !seenNames.contains(shark.getName());

            if (genderMatch && stageMatch && locationMatch && newShark) {
                sharks.add(shark);
                seenNames.add(shark.getName());

                mSearchFrame.addResult(shark, ping, mUserPreference.isFavorite(shark.getName()));
            }
        }

        return sharks;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component component = (Component) e.getSource();

        JButton jButton;
        switch (component.getName()) {
        case SearchFrame.JBSEARCH_NAME:
            SearchFrame.Query query = mSearchFrame.getQuery();

            // Do not interrupt UI while these queries are happening.
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    mSearchFrame.clearResults();
                    performQuery(query);
                }
            };

            new Thread(r).start();
            break;
        case ResultPanel.JBFOLLOW_NAME:
            jButton = (JButton) component;
            ResultPanel resultPanel = (ResultPanel) jButton.getParent().getParent();
            String sharkName = jButton.getActionCommand();

            mUserPreference.toggleFavorite(sharkName);
            resultPanel.toggleFollow();

            break;
        case SharkOfTheDayPanel.JBSOTD_NAME:
            jButton = (JButton) component;
            if (Desktop.isDesktopSupported()) {
                try {
                    String link = jButton.getActionCommand();
                    if (link != null) {
                        Desktop.getDesktop().browse(new URI(link));
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (URISyntaxException ex) {
                }
            }

            break;
        default:
            System.out.println("Unknown action source: " + component.getName());
        }
    }
}
