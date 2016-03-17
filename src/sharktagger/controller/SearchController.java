package sharktagger.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import api.jaws.Jaws;
import api.jaws.Ping;
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

                mSearchFrame.addResult(shark, ping);
            }
        }

        return sharks;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component component = (Component) e.getSource();

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
        default:
            System.out.println("Unknown action source: " + component.getName());
        }
    }
}
