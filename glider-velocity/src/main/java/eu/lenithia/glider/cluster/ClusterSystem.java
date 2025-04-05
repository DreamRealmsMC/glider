package eu.lenithia.glider.cluster;

import dev.dejvokep.boostedyaml.YamlDocument;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.cluster.system.GCluster;
import eu.lenithia.glider.utils.ConfigLoader;
import lombok.Getter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class ClusterSystem {

    private final GliderVelocity glider;

    @Getter
    private HashMap<String, GCluster> clusters = new HashMap<>();


    public ClusterSystem(GliderVelocity glider) {
        this.glider = glider;

        initialize();
    }

    private void initialize() {
        File clusterFolder = new File(glider.getDataDirectory().toFile(), "clusters");
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

            try {
                YamlDocument exampleCluster = ConfigLoader.getUnversionedConfig(clusterFolder.toPath(), "example", getClass().getResourceAsStream("/clusters/example.yml"));
                addCluster("example", exampleCluster);
            } catch (Exception e) {
                glider.getLogger().error("Failed to create example cluster file.", e);
            }
            return;
        }

        for (File clusterFile : clusterFiles) {
            try {
                YamlDocument clusterConfig = ConfigLoader.getUnversionedConfig(clusterFolder.toPath(), clusterFile.getName().replaceFirst("\\.ya?ml$", ""), Files.newInputStream(clusterFile.toPath()));
                addCluster(clusterFile.getName().replaceFirst("\\.ya?ml$", ""), clusterConfig);
            } catch (Exception e) {
                glider.getLogger().error("Failed to load cluster file: {}", clusterFile.getName(), e);
            }
        }
    }

    private void addCluster(String name, YamlDocument clusterConfig) {
        glider.getLogger().info("Adding cluster to list: {}", name);
        GCluster cluster = new GCluster(glider, name ,clusterConfig);
        clusters.put(name, cluster);
    }

    public void reload() {
        File clusterFolder = new File(glider.getDataDirectory().toFile(), "clusters");
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

        for (File clusterFile : clusterFiles) {
            String clusterName = clusterFile.getName().replaceFirst("\\.ya?ml$", "");
            if (clusters.containsKey(clusterName)) {
                clusters.get(clusterName).reload();
            } else {
                try {
                    YamlDocument clusterConfig = ConfigLoader.getUnversionedConfig(clusterFolder.toPath(), clusterName, Files.newInputStream(clusterFile.toPath()));
                    addCluster(clusterName, clusterConfig);
                } catch (Exception e) {
                    glider.getLogger().error("Failed to load cluster file: {}", clusterFile.getName(), e);
                }
            }
        }
    }


}
