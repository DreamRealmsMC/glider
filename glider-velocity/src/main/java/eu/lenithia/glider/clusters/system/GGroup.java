package eu.lenithia.glider.clusters.system;


import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.events.GGroupLoadEvent;
import eu.lenithia.glider.clusters.integrationsystem.templates.GGroupIntegration;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class GGroup {

    private final GliderVelocity glider;
    private final GCluster cluster;
    private Section groupConfig;
    private final String groupName;

    private final ConcurrentHashMap<String, GGroupIntegration> groupIntegrations = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, GServer> groupServers = new ConcurrentHashMap<>();

    public GGroup(GCluster cluster, String name, Section section, CompletableFuture<Void> loadFuture) {
        this.cluster = cluster;
        this.groupName = name;
        this.groupConfig = section;
        this.glider = cluster.getGlider();
        init(loadFuture);
    }

    private void init(CompletableFuture<Void> loadFuture) {
        List<CompletableFuture<Void>> serverFutures = new ArrayList<>();

        glider.getProxy().getEventManager().fire(new GGroupLoadEvent(this))
                .thenRun(() -> {
                    for (GGroupIntegration groupIntegration : groupIntegrations.values()) {
                        groupIntegration.onLoad();
                    }
                    loadServers(serverFutures);
                    glider.getLogger().info("[GROUP] ({}) {} loaded with {} integrations",
                            cluster.getClusterName(), groupName, groupIntegrations.size());

                    // Wait for all servers to complete loading
                    CompletableFuture.allOf(serverFutures.toArray(new CompletableFuture[0]))
                            .thenRun(() -> loadFuture.complete(null));
                });
    }

    private void loadServers(List<CompletableFuture<Void>> serverFutures) {
        Section serversSection = groupConfig.getSection("servers");
        if (serversSection == null) {
            glider.getLogger().warn("No servers section found in group {} of cluster {}",
                    groupName, cluster.getClusterName());
            return;
        }

        for (String serverName : serversSection.getRoutesAsStrings(false)) {
            Section serverSection = serversSection.getSection(serverName);
            if (serverSection != null) {
                CompletableFuture<Void> serverFuture = new CompletableFuture<>();
                serverFutures.add(serverFuture);
                GServer server = new GServer(this, serverName, serverSection, serverFuture);
                groupServers.put(serverName, server);
            } else {
                glider.getLogger().warn("Server {} configuration is invalid in group {} of cluster {}",
                        serverName, groupName, cluster.getClusterName());
            }
        }
    }

    public void reload() {

    }

    public void unload() {

    }


}
