package eu.lenithia.glider.api;

import eu.lenithia.glider.api.clustersystem.ClusterSystemAPI;
import eu.lenithia.glider.api.command.CommandAPI;
import eu.lenithia.glider.api.sender.SenderAPI;

public interface GliderAPI {
    /**
     * Get the SenderAPI instance
     * @return The SenderAPI for sending players to servers/clusters
     */
    SenderAPI getSenderAPI();

    /**
     * Get the ClusterSystemAPI instance
     * @return The ClusterSystemAPI for accessing clusters
     */
    ClusterSystemAPI getClusterSystemAPI();

    /**
     * Get the CommandAPI instance
     * @return The CommandAPI for registering your own commands to glider
     */
    CommandAPI getCommandAPI();

    /**
     * Get the API version
     * @return API version string
     */
    int getAPIVersion();
}
