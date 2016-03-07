package sharktagger.model;

import java.util.Set;

public class UserPreference {
    private Set<Shark> favorites;

    /**
     * If shark is not in favorites, add it to favorites.
     * Else, remove it from favorites.
     * @param shark Shark object to toggle.
     */
    public void toggleFavorite(Shark shark) {
    }

    /**
     * Load user preferences from specified file.
     * If file does not exist, instantiate a new preferences.
     * @param filename String name of the preferences file.
     * @return UserPreference object.
     */
    public static UserPreference retrieveFromFile(String filename) {
        // Default return value.
        return null;
    }

    /**
     * Save these user preferences to a file.
     * @return Success.
     */
    public boolean saveToFile() {
        // Default return value.
        return false;
    }
}
