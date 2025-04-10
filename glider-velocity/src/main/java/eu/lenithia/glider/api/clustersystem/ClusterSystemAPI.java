package eu.lenithia.glider.api.clustersystem;

import com.velocitypowered.api.proxy.Player;
import dev.dejvokep.boostedyaml.YamlDocument;
import eu.lenithia.glider.clusters.system.GCluster;
import eu.lenithia.glider.clusters.system.GGroup;
import eu.lenithia.glider.clusters.system.GServer;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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

    /**
     * Get a cluster that the player is currently in
     * @param player
     * @return GCluster
     */
    Optional<GCluster> getCluster(Player player);

    /**
     * Get a group that the player is currently in
     * @param player
     * @return Ggroup
     */
    Optional<GGroup> getGroup(Player player);

    /**
     * Get a server that the player is currently in
     * @param player
     * @return GServer
     */
    Optional<GServer> getServer(Player player);

    /**
     * Initialize a cluster
     * @param name Name of the cluster
     * @param clusterConfig Configuration of the cluster
     * @param future Future to complete when the cluster is initialized
     */
    void addCluster(String name, YamlDocument clusterConfig, CompletableFuture<Void> future);

    boolean removeCluster(String name);
    void removeCluster(GCluster cluster);
}
