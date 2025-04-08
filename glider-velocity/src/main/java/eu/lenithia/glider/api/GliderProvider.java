package eu.lenithia.glider.api;

public class GliderProvider {

    private static GliderAPI api;

    /**
     * Sets the API instance (called by Glider)
     * @param api The API implementation
     */
    public static void setAPI(GliderAPI api) {
        GliderProvider.api = api;
    }

    /**
     * Gets the API instance
     * @return The Glider API
     * @throws IllegalStateException if Glider is not loaded
     */
    public static GliderAPI getAPI() {
        if (api == null) {
            throw new IllegalStateException("Glider API is not available. Is Glider plugin loaded?");
        }
        return api;
    }

}
