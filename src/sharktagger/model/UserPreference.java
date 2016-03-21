package sharktagger.model;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UserPreference {
    private static final String ROOT_ELEMENT_NAME = "preferences";
    private static final String LAST_UPDATED_ELEMENT_NAME = "last_updated";
    private static final String FAVORITES_ELEMENT_NAME = "favorites";
    private static final String FAVORITE_SHARK_ELEMENT_NAME = "shark";

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Set<String> mFavorites;
    private Date mLastUpdated;

    private Set<PreferenceUpdateListener> mUpdateListeners;

    public UserPreference() {
        mFavorites = new HashSet<String>();
        mLastUpdated = new Date();

        mUpdateListeners = new HashSet<PreferenceUpdateListener>();
    }

    public List<String> getFavorites() {
        return new ArrayList<String>(mFavorites);
    }

    public void addUpdateListener(PreferenceUpdateListener listener) {
        mUpdateListeners.add(listener);
    }

    /**
     * If shark is not in favorites, add it to favorites.
     * Else, remove it from favorites.
     * @param name String name of shark object to toggle.
     * @return boolean true if the shark was added to favorites, false if shark was removed.
     */
    public boolean toggleFavorite(String name) {
        if (mFavorites.contains(name)) {
            mFavorites.remove(name);

            // Update favorites.
            for (PreferenceUpdateListener listener : mUpdateListeners) {
                listener.favoriteRemoved(name);
            }
        } else {
            mFavorites.add(name);

            // Update favorites.
            for (PreferenceUpdateListener listener : mUpdateListeners) {
                listener.favoriteAdded(name);
            }
        }
        return mFavorites.contains(name);
    }

    /**
     * Returns true if the shark is followed, false otherwise.
     * @param name String name of shark.
     * @return boolean if shark is followed.
     */
    public boolean isFavorite(String name) {
        return mFavorites.contains(name);
    }

    /**
     * Returns true if any sharks are favorited, false otherwise.
     * @return boolean true if any sharks are favorited, false otherwise.
     */
    public boolean hasFavorites() {
        return !mFavorites.isEmpty();
    }

    /**
     * Load user preferences from specified file.
     * If file does not exist, instantiate a new preferences.
     * @param filename String name of the preferences file.
     * @return UserPreference object.
     */
    public static UserPreference retrieveFromFile(String filename) {
        UserPreference userPreference = new UserPreference();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        Document document;
        try {
            db = dbf.newDocumentBuilder();
            document = db.parse(new File(filename));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return userPreference;
        } catch (SAXException e) {
            e.printStackTrace();
            return userPreference;
        } catch (IOException e) {
            return userPreference;
        }

        Element rootElement = document.getDocumentElement();

        Element timeUpdatedElement = (Element) rootElement.getElementsByTagName(LAST_UPDATED_ELEMENT_NAME).item(0);
        String timeUpdated = timeUpdatedElement.getTextContent();
        try {
            userPreference.mLastUpdated = DATE_FORMATTER.parse(timeUpdated);
        } catch (ParseException e) {
            System.out.println("Preference file corrupted. Erase and try again.");
            System.exit(-1);
        }

        Element favoritesElement = (Element) rootElement.getElementsByTagName(FAVORITES_ELEMENT_NAME).item(0);
        NodeList favoritesList = favoritesElement.getElementsByTagName(FAVORITE_SHARK_ELEMENT_NAME);

        for (int i = 0; i < favoritesList.getLength(); i++) {
            Element sharkElement = (Element) favoritesList.item(i);
            String sharkName = sharkElement.getTextContent();

            userPreference.mFavorites.add(sharkName);
        }

        return userPreference;
    }

    /**
     * Save these user preferences to a file.
     * @return Success.
     */
    public boolean saveToFile(String filename) {
        // Write contents into document.
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        Document document = db.newDocument();

        Element rootElement = document.createElement(ROOT_ELEMENT_NAME);
        document.appendChild(rootElement);

        Element timeUpdatedElement = document.createElement(LAST_UPDATED_ELEMENT_NAME);
        timeUpdatedElement.appendChild(document.createTextNode(DATE_FORMATTER.format(mLastUpdated)));
        rootElement.appendChild(timeUpdatedElement);

        Element favoritesElement = document.createElement(FAVORITES_ELEMENT_NAME);
        rootElement.appendChild(favoritesElement);

        for (String sharkName : mFavorites) {
            Element sharkElement = document.createElement(FAVORITE_SHARK_ELEMENT_NAME);
            sharkElement.appendChild(document.createTextNode(sharkName));
            favoritesElement.appendChild(sharkElement);
        }

        // Write the document into XML file.
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t;
        try {
            t = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filename));
            t.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            return false;
        } catch (TransformerException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static interface PreferenceUpdateListener {
        public void favoriteAdded(String name);
        public void favoriteRemoved(String name);
    }
}
