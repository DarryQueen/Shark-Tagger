package sharktagger.model;

import java.util.HashSet;
import java.util.Set;

public class UserPreference {
    private Set<String> favorites;

    public UserPreference() {
        favorites = new HashSet<String>();
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
    public boolean saveToFile() {
        // Default return value.
        return false;
    }
}
