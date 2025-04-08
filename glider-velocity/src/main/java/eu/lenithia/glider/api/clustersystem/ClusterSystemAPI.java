package eu.lenithia.glider.api.clustersystem;

import eu.lenithia.glider.clusters.system.GCluster;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public interface ClusterSystemAPI {
    /**
     * Get all registered clusters
     * @return Map of cluster names to cluster objects
     */
    ConcurrentHashMap<String, GCluster> getClusters();

    /**
     * Get a cluster by its name
     * @param name Name of the cluster
     * @return The cluster if found
     */
    Optional<GCluster> getCluster(String name);

    /**
     * Reload all clusters from their configuration files
     */
    void reload();
}
