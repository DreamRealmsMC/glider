package eu.lenithia.glider.cluster.system;

import dev.dejvokep.boostedyaml.YamlDocument;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.cluster.events.GClusterLoadEvent;
import eu.lenithia.glider.cluster.integrationsystem.templates.ClusterIntegration;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class GCluster {

    private final GliderVelocity glider;
    private YamlDocument clusterConfig;
    private final String clusterName;

    private HashMap<String, ClusterIntegration> clusterInteractions = new HashMap<>();

    public GCluster(GliderVelocity glider,String name, YamlDocument clusterConfig) {
        this.glider = glider;
        this.clusterConfig = clusterConfig;
        this.clusterName = name;
        init();
    }

    private void init() {
        glider.getProxy().getEventManager().fire(new GClusterLoadEvent(this))
                .thenRun(() -> {
                    // Now process the integrations after event handlers have run
                    for (ClusterIntegration clusterIntegration : clusterInteractions.values()) {
                        clusterIntegration.onLoad();
                    }
                    glider.getLogger().info("Cluster {} loaded with {} integrations", clusterName, clusterInteractions.size());
                });
    }

    public void reload() {
        for (ClusterIntegration clusterIntegration : clusterInteractions.values()) {
            clusterIntegration.onReload();
        }
    }

    public void unload() {
        for (ClusterIntegration clusterIntegration : clusterInteractions.values()) {
            clusterIntegration.onUnload();
        }
    }

}
