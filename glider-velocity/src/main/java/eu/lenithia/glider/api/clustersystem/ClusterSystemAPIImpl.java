package eu.lenithia.glider.api.clustersystem;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
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
        return glider.getClusterSystem().getClusters();
    }

    @Override
    public Optional<GCluster> getCluster(String name) {
        return Optional.ofNullable(glider.getClusterSystem().getClusters().get(name));
    }

    @Override
    public void reload() {
        glider.getClusterSystem().reload();
    }

    @Override
    public Optional<GCluster> getCluster(Player player) {
        if (!player.getCurrentServer().isPresent()) {
            return Optional.empty();
        }

        String serverName = player.getCurrentServer().get().getServerInfo().getName();

        for (GCluster cluster : getClusters().values()) {
            for (GGroup group : cluster.getClusterGroups().values()) {
                for (GServer server : group.getGroupServers().values()) {
                    String expectedName = cluster.getClusterPrefix() + group.getGroupPrefix() + server.getServerID();
                    if (expectedName.equals(serverName)) {
                        return Optional.of(cluster);
                    }
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<GGroup> getGroup(Player player) {
        if (player.getCurrentServer().isEmpty()) {
            return Optional.empty();
        }

        String serverName = player.getCurrentServer().get().getServerInfo().getName();

        for (GCluster cluster : getClusters().values()) {
            for (GGroup group : cluster.getClusterGroups().values()) {
                for (GServer server : group.getGroupServers().values()) {
                    String expectedName = cluster.getClusterPrefix() + group.getGroupPrefix() + server.getServerID();
                    if (expectedName.equals(serverName)) {
                        return Optional.of(group);
                    }
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<GServer> getServer(Player player) {
        if (!player.getCurrentServer().isPresent()) {
            return Optional.empty();
        }

        String serverName = player.getCurrentServer().get().getServerInfo().getName();

        for (GCluster cluster : getClusters().values()) {
            for (GGroup group : cluster.getClusterGroups().values()) {
                for (GServer server : group.getGroupServers().values()) {
                    String expectedName = cluster.getClusterPrefix() + group.getGroupPrefix() + server.getServerID();
                    if (expectedName.equals(serverName)) {
                        return Optional.of(server);
                    }
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public void addCluster(String name, YamlDocument clusterConfig, CompletableFuture<Void> future) {
        glider.getClusterSystem().addCluster(name, clusterConfig, future);
    }

    @Override
    public boolean unloadCluster(String name) {;
        return glider.getClusterSystem().removeCluster(name);
    }

    @Override
    public void unloadCluster(GCluster cluster) {
        glider.getClusterSystem().removeCluster(cluster);
    }
}