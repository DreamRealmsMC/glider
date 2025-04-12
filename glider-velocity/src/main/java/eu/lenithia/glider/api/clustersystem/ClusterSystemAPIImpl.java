package eu.lenithia.glider.api.clustersystem;

import com.velocitypowered.api.proxy.Player;
import dev.dejvokep.boostedyaml.YamlDocument;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.system.GCluster;
import eu.lenithia.glider.clusters.system.GGroup;
import eu.lenithia.glider.clusters.system.GServer;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ClusterSystemAPIImpl implements ClusterSystemAPI {

    private final GliderVelocity glider;

    public ClusterSystemAPIImpl(GliderVelocity glider) {
        this.glider = glider;
    }

    @Override
    public ConcurrentHashMap<String, GCluster> getClusters() {
        return null;
    }

    @Override
    public Optional<GCluster> getCluster(String name) {
        return Optional.empty();
    }

    @Override
    public void reload() {

    }

    @Override
    public Optional<GCluster> getCluster(Player player) {
        return Optional.empty();
    }

    @Override
    public Optional<GGroup> getGroup(Player player) {
        return Optional.empty();
    }

    @Override
    public Optional<GServer> getServer(Player player) {
        return Optional.empty();
    }

    @Override
    public void addCluster(String name, YamlDocument clusterConfig, CompletableFuture<Void> future) {

    }

    @Override
    public boolean unloadCluster(String name) {
        return false;
    }

    @Override
    public void unloadCluster(GCluster cluster) {

    }
}
