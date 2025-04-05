package eu.lenithia.glider.cluster.integrationsystem.templates;

import dev.dejvokep.boostedyaml.YamlDocument;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.cluster.system.GCluster;
import lombok.Getter;

@Getter
public abstract class ClusterIntegration {

    private final GCluster cluster;

    public ClusterIntegration(GCluster cluster) {
        this.cluster = cluster;
    }

    public YamlDocument getClusterConfig() {
        return cluster.getClusterConfig();
    }

    public GliderVelocity getGlider() {
        return cluster.getGlider();
    }

    public abstract void onLoad();

    public abstract void onReload();

    public abstract void onUnload();

}
