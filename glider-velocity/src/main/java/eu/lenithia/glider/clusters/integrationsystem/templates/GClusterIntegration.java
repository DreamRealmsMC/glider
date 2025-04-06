package eu.lenithia.glider.clusters.integrationsystem.templates;

import dev.dejvokep.boostedyaml.YamlDocument;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.system.GCluster;
import lombok.Getter;

@Getter
public abstract class GClusterIntegration {

    private final GCluster cluster;

    public GClusterIntegration(GCluster cluster) {
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
