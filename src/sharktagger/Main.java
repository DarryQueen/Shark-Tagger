package sharktagger;

import sharktagger.controller.FavoritesController;
import sharktagger.controller.MenuController;
import sharktagger.controller.SearchController;
import sharktagger.model.UserPreference;

public class Main {
    private static final String PREFERENCES_FILENAME = "";

    public static void main(String[] args) {
        UserPreference preferences = UserPreference.retrieveFromFile(PREFERENCES_FILENAME);

        SearchController sController = new SearchController(preferences);
        FavoritesController fController = new FavoritesController(preferences, sController);
        MenuController mController = new MenuController(preferences, sController, fController);

        mController.open();
    }
}
