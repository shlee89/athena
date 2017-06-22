package athena.api;

/**
 * ValidationParams supports additional functionalities.
 * In initial version of the Athena, it just supports to save detection entries with a detection result.
 * This will could be implemented.
 * Not used Yet!
 * Created by seunghyeon on 4/7/16.
 */
public class ValidationParams {
    public boolean storeToDB;

    /**
     * Validation parameters.
     *
     * @param storeToDB true or false.
     */
    public ValidationParams(boolean storeToDB) {
        this.storeToDB = storeToDB;
    }

    /**
     * Get database properties.
     *
     * @return true or false.
     */
    public boolean isStoreToDB() {
        return storeToDB;
    }
}
