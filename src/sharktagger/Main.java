package sharktagger;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import api.jaws.Jaws;
import sharktagger.controller.FavoritesController;
import sharktagger.controller.MenuController;
import sharktagger.controller.SearchController;
import sharktagger.model.UserPreference;

public class Main {
    private static final String PREFERENCES_FILENAME = "";
    private static final String JAWS_KEYS_FILENAME = "jaws_keys.xml";

    private static Document getKeysDocument(String filename) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            return db.parse(new File(filename));
        } catch (Exception e) {
            // I really don't care.
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Document document = getKeysDocument(JAWS_KEYS_FILENAME);
        Element jawsElement = (Element) document.getDocumentElement().getElementsByTagName("jaws").item(0);
        String publicKey = jawsElement.getElementsByTagName("public").item(0).getTextContent();
        String privateKey = jawsElement.getElementsByTagName("private").item(0).getTextContent();
        Jaws jaws = new Jaws(privateKey, publicKey, true);

        UserPreference preferences = UserPreference.retrieveFromFile(PREFERENCES_FILENAME);

        SearchController sController = new SearchController(preferences);
        FavoritesController fController = new FavoritesController(preferences, sController);
        MenuController mController = new MenuController(preferences, sController, fController);

        mController.open();
    }
}
