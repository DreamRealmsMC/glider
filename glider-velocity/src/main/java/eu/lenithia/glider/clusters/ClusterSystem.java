package eu.lenithia.glider.clusters;

import dev.dejvokep.boostedyaml.YamlDocument;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.system.GCluster;
import eu.lenithia.glider.clusters.system.GGroup;
import eu.lenithia.glider.utils.ConfigLoader;
import lombok.Getter;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ClusterSystem {

    private final GliderVelocity glider;
    private final File clusterFolder;

    @Getter
    private ConcurrentHashMap<String, GCluster> clusters = new ConcurrentHashMap<>();

    public ClusterSystem(GliderVelocity glider) {
        this.glider = glider;
        this.clusterFolder = new File(glider.getDataDirectory().toFile(), "clusters");
        initialize();
    }

    private void initialize() {
        glider.getLogger().info("Cluster system initializing");
        if (!clusterFolder.exists() && !clusterFolder.mkdirs()) {
            glider.getLogger().error("Failed to create clusters directory.");
            glider.getLogger().error("Server is shutting down (fix write permissions).");
            glider.getProxy().shutdown();
            return;
        }

        File[] clusterFiles = clusterFolder.listFiles((dir, name) -> name.endsWith(".yml") || name.endsWith(".yaml"));
        if (clusterFiles == null || clusterFiles.length == 0) {
            glider.getLogger().warn("No valid cluster configuration files found.");
            glider.getLogger().warn("Creating an example cluster file.");
            glider.getLogger().warn("Please edit it and restart the server.");

            try {
                YamlDocument exampleCluster = ConfigLoader.getUnversionedConfig(clusterFolder.toPath(), "example", getClass().getResourceAsStream("/clusters/example.yml"));
                CompletableFuture<Void> future = new CompletableFuture<>();
                addCluster("example", exampleCluster, future);
            } catch (Exception e) {
                glider.getLogger().error("Failed to create example cluster file.", e);
            }
            return;
        }

        List<CompletableFuture<Void>> clusterFutures = new ArrayList<>();

        for (File clusterFile : clusterFiles) {
            String clusterName = clusterFile.getName().replaceFirst("\\.ya?ml$", "");
            YamlDocument config = getClusterConfig(clusterName);
            if (config != null) {
                CompletableFuture<Void> future = new CompletableFuture<>();
                clusterFutures.add(future);
                addCluster(clusterName, config, future);
            }
        }

        CompletableFuture.allOf(clusterFutures.toArray(new CompletableFuture[0]))
                .thenRun(() -> {
                    glider.getLogger().info("------------------------------------------");
                    printClusters();
                    glider.getLogger().info("------------------------------------------");
                });
    }

    public void addCluster(String name, YamlDocument clusterConfig, CompletableFuture<Void> future) {
        GCluster cluster = new GCluster(glider, name, clusterConfig, future);
        clusters.put(name, cluster);
    }

    public boolean removeCluster(String name) {
        GCluster cluster = clusters.remove(name);
        if (cluster != null) {
            cluster.unload();
            glider.getLogger().info("Cluster {} removed", name);
            return true;
        } else {
            glider.getLogger().warn("Cluster {} not found", name);
            return false;
        }
    }

    public void removeCluster(GCluster cluster) {
        String name = cluster.getClusterName();
        clusters.remove(name);
        cluster.unload();
        glider.getLogger().info("Cluster {} removed", name);
    }

    public void printClusters() {
        int clusterCount = clusters.size();
        String clusterNames = String.join(", ", clusters.keySet());

        int serverCount = 0;
        for (GCluster cluster : clusters.values()) {
            for (GGroup group : cluster.getClusterGroups().values()) {
                serverCount += group.getGroupServers().size();
            }
        }

        glider.getLogger().info("Loaded {} clusters ({}) with {} servers",
                clusterCount, clusterNames, serverCount);
    }

    public YamlDocument getClusterConfig(String name) {
        try {
            // First check for .yml extension
            File file = new File(clusterFolder, name + ".yml");
            if (!file.exists()) {
                // Try with .yaml extension
                file = new File(clusterFolder, name + ".yaml");
                if (!file.exists()) {
                    throw new IllegalArgumentException("Cluster file for '" + name + "' does not exist");
                }
            }
            return ConfigLoader.getUnversionedConfig(clusterFolder.toPath(), name, Files.newInputStream(file.toPath()));
        } catch (Exception e) {
            glider.getLogger().error("Failed to load cluster config for: {}", name, e);
            return null;
        }
    }

    public void reload() {
        if (!clusterFolder.exists()) {
            glider.getLogger().error("Clusters directory does not exist.");
            glider.getLogger().error("What the fuck did you do with it?");
            return;
        }

        File[] clusterFiles = clusterFolder.listFiles((dir, name) -> name.endsWith(".yml") || name.endsWith(".yaml"));
        if (clusterFiles == null || clusterFiles.length == 0) {
            glider.getLogger().warn("No valid cluster configuration files found. WTF??");
            return;
        }

        List<CompletableFuture<Void>> reloadFutures = new ArrayList<>();

        for (File clusterFile : clusterFiles) {
            String clusterName = clusterFile.getName().replaceFirst("\\.ya?ml$", "");
            if (clusters.containsKey(clusterName)) {
                clusters.get(clusterName).reload();
            } else {
                YamlDocument config = getClusterConfig(clusterName);
                if (config != null) {
                    CompletableFuture<Void> future = new CompletableFuture<>();
                    reloadFutures.add(future);
                    addCluster(clusterName, config, future);
                }
            }
        }

        if (!reloadFutures.isEmpty()) {
            CompletableFuture.allOf(reloadFutures.toArray(new CompletableFuture[0]))
                    .thenRun(() -> {
                        glider.getLogger().info("------------------------------------------");
                        printClusters();
                        glider.getLogger().info("------------------------------------------");
                    });
        }
    }
}