package sharktagger;

import api.jaws.Jaws;
import sharktagger.controller.FavoritesController;
import sharktagger.controller.MenuController;
import sharktagger.controller.SearchController;
import sharktagger.model.UserPreference;

public class Main {
    public static final String PREFERENCES_FILENAME = "preferences.xml";
    private static final String JAWS_KEYS_FILENAME = "jaws_keys.xml";

    public static void main(String[] args) {
        KeyRetriever retriever = new KeyRetriever(JAWS_KEYS_FILENAME);
        Jaws jaws = new Jaws(retriever.getJawsPrivateKey(), retriever.getJawsPublicKey(), true);

        UserPreference preferences = UserPreference.retrieveFromFile(PREFERENCES_FILENAME);

        SearchController sController = new SearchController(preferences, jaws);
        FavoritesController fController = new FavoritesController(preferences, sController, jaws);
        MenuController mController = new MenuController(preferences, sController, fController);

        preferences.addUpdateListener(fController);
        preferences.addUpdateListener(mController);

        mController.open();
    }
}
