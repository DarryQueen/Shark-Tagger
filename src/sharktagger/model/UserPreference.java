package sharktagger.model;

import java.io.File;
import java.util.ArrayList;
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

public class UserPreference {
    private Set<String> favorites;

    public UserPreference() {
        favorites = new HashSet<String>();
    }

    public List<String> getFavorites() {
        return new ArrayList<String>(favorites);
    }

    /**
     * If shark is not in favorites, add it to favorites.
     * Else, remove it from favorites.
     * @param name String name of shark object to toggle.
     * @return boolean true if the shark was added to favorites, false if shark was removed.
     */
    public boolean toggleFavorite(String name) {
        if (favorites.contains(name)) {
            favorites.remove(name);
        } else {
            favorites.add(name);
        }
        return favorites.contains(name);
    }

    /**
     * Returns true if the shark is followed, false otherwise;
     * @param name String name of shark.
     * @return boolean if shark is followed.
     */
    public boolean isFavorite(String name) {
        return favorites.contains(name);
    }

    /**
     * Load user preferences from specified file.
     * If file does not exist, instantiate a new preferences.
     * @param filename String name of the preferences file.
     * @return UserPreference object.
     */
    public static UserPreference retrieveFromFile(String filename) {
        // For now, let's not save anything.
        return new UserPreference();
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

        Element rootElement = document.createElement("preferences");
        document.appendChild(rootElement);

        Element favoritesElement = document.createElement("favorites");
        rootElement.appendChild(favoritesElement);

        for (String sharkName : favorites) {
            Element sharkElement = document.createElement("shark");
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
}
