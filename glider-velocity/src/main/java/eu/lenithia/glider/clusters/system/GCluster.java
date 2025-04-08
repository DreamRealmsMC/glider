package eu.lenithia.glider.clusters.system;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.events.GClusterLoadEvent;
import eu.lenithia.glider.clusters.integrationsystem.templates.GClusterIntegration;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class GCluster {

    private final GliderVelocity glider;
    private YamlDocument clusterConfig;
    private final String clusterName;
    private final String clusterPrefix;

    private final ConcurrentHashMap<String, GClusterIntegration> clusterInteractions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, GGroup> clusterGroups = new ConcurrentHashMap<>();
    private GGroup defaultGroup;

    public GCluster(GliderVelocity glider, String name, YamlDocument clusterConfig, CompletableFuture<Void> loadFuture) {
        this.glider = glider;
        this.clusterConfig = clusterConfig;
        this.clusterName = name;
        this.clusterPrefix = clusterConfig.getString("cluster-settings.settings.prefix");
        init(loadFuture);
    }

    private void init(CompletableFuture<Void> loadFuture) {
        List<CompletableFuture<Void>> groupFutures = new ArrayList<>();

        glider.getProxy().getEventManager().fire(new GClusterLoadEvent(this))
                .thenRun(() -> {
                    for (GClusterIntegration clusterIntegration : clusterInteractions.values()) {
                        clusterIntegration.onLoad();
                    }
                    loadGroups(groupFutures);
                    glider.getLogger().info("[CLUSTER] {} loaded with {} integrations", clusterName, clusterInteractions.size());

                    // Wait for all groups to complete loading
                    CompletableFuture.allOf(groupFutures.toArray(new CompletableFuture[0]))
                            .thenRun(() -> loadFuture.complete(null));
                });
    }

    private void loadGroups(List<CompletableFuture<Void>> groupFutures) {
        Section groupsSection = clusterConfig.getSection("groups");
        if (groupsSection == null) {
            glider.getLogger().warn("No groups section found in cluster {}", clusterName);
            return;
        }

        for (String groupName : groupsSection.getRoutesAsStrings(false)) {
            Section groupSection = groupsSection.getSection(groupName);
            if (groupSection != null) {
                CompletableFuture<Void> groupFuture = new CompletableFuture<>();
                groupFutures.add(groupFuture);
                GGroup group = new GGroup(this, groupName, groupSection, groupFuture);
                clusterGroups.put(groupName, group);
            } else {
                glider.getLogger().warn("Group {} configuration is invalid in cluster {}", groupName, clusterName);
            }
        }

        defaultGroup = clusterGroups.get(getClusterConfig().getString("cluster-settings.settings.defaultGroup", null));

        if (defaultGroup == null) {
            glider.getLogger().error("Default group not found in cluster {}. Please check the configuration.", clusterName);
            defaultGroup = clusterGroups.values().stream().findAny().orElse(null);
        }
    }

    public void reload() {
        YamlDocument newConfig = glider.getClusterSystem().getClusterConfig(clusterName);
        if (newConfig != null) {
            clusterConfig = newConfig;
            for (GClusterIntegration clusterIntegration : clusterInteractions.values()) {
                clusterIntegration.onReload();
            }
            for (GGroup group : clusterGroups.values()) {
                group.reload();
            }
        }

        defaultGroup = clusterGroups.get(getClusterConfig().getString("cluster-settings.settings.defaultGroup", null));
        if (defaultGroup == null) {
            glider.getLogger().error("Default group not found in cluster {} after reload. Assigning a random group.", clusterName);
            defaultGroup = clusterGroups.values().stream().findAny().orElse(null);
        }

    }

    public void unload() {
        for (GClusterIntegration clusterIntegration : clusterInteractions.values()) {
            clusterIntegration.onUnload();
        }
        for (GGroup group : clusterGroups.values()) {
            group.unload();
        }
    }

}
