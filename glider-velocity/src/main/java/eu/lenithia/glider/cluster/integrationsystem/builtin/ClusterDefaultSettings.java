package eu.lenithia.glider.cluster.integrationsystem.builtin;

import dev.dejvokep.boostedyaml.YamlDocument;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.cluster.integrationsystem.templates.ClusterIntegration;
import eu.lenithia.glider.cluster.system.GCluster;

public class ClusterDefaultSettings extends ClusterIntegration {


    public ClusterDefaultSettings(GCluster cluster) {
        super(cluster);
    }

    @Override
    public void onLoad() {
        YamlDocument confing = getClusterConfig();
        getGlider().getLogger().info("Loading default settings integration");
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onUnload() {

    }
}
